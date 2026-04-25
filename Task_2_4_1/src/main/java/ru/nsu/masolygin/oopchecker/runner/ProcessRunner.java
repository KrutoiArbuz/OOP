package ru.nsu.masolygin.oopchecker.runner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Универсальный исполнитель внешних процессов с одновременным чтением stdout/stderr, принудительным
 * убийством по таймауту и поддержкой переменных окружения.
 */
public class ProcessRunner {

    private final Duration defaultTimeout;
    private final Map<String, String> extraEnv;

    /**
     * Создаёт исполнитель с таймаутом 5 минут и пустыми дополнительными переменными окружения.
     */
    public ProcessRunner() {
        this(Duration.ofMinutes(5), Map.of());
    }

    /**
     * Создаёт исполнитель с заданным таймаутом и переменными окружения.
     *
     * @param defaultTimeout таймаут по умолчанию для всех процессов
     * @param extraEnv       дополнительные переменные окружения
     */
    public ProcessRunner(Duration defaultTimeout, Map<String, String> extraEnv) {
        this.defaultTimeout = defaultTimeout;
        this.extraEnv = Map.copyOf(extraEnv);
    }

    /**
     * Создаёт daemon-поток, читающий из потока в StringBuilder с синхронизацией.
     *
     * @param stream входной поток для чтения
     * @param sink   StringBuilder для накопления данных
     * @return запущенный daemon поток
     */
    private static Thread drain(InputStream stream, StringBuilder sink) {
        Thread t = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    synchronized (sink) {
                        sink.append(line).append('\n');
                    }
                }
            } catch (IOException ignored) {
            }
        }, "process-pump");
        t.setDaemon(true);
        t.start();
        return t;
    }

    /**
     * Объединяет две карты переменных окружения.
     *
     * @param base  базовые переменные
     * @param extra дополнительные переменные (перекрывают базовые)
     * @return объединённая карта
     */
    public static Map<String, String> mergeEnv(Map<String, String> base,
        Map<String, String> extra) {
        Map<String, String> merged = new HashMap<>(base);
        merged.putAll(extra);
        return merged;
    }

    /**
     * Запускает процесс с таймаутом по умолчанию.
     *
     * @param workingDir рабочий каталог процесса (null = текущий каталог JVM)
     * @param command    список аргументов команды
     * @return результат выполнения процесса
     * @throws IOException          если не удалось запустить процесс
     * @throws InterruptedException если текущий поток был прерван
     */
    public ProcessResult run(Path workingDir, List<String> command)
        throws IOException, InterruptedException {
        return run(workingDir, command, defaultTimeout);
    }

    /**
     * Запускает процесс с заданным таймаутом.
     *
     * @param workingDir рабочий каталог процесса (null = текущий каталог JVM)
     * @param command    список аргументов команды
     * @param timeout    максимальное время выполнения
     * @return результат выполнения процесса (с флагом timedOut если достигнут таймаут)
     * @throws IOException          если не удалось запустить процесс
     * @throws InterruptedException если текущий поток был прерван
     */
    public ProcessResult run(Path workingDir, List<String> command, Duration timeout)
        throws IOException, InterruptedException {

        ProcessBuilder pb = new ProcessBuilder(command);
        if (workingDir != null) {
            pb.directory(workingDir.toFile());
        }
        Map<String, String> env = pb.environment();
        env.putAll(extraEnv);
        pb.redirectErrorStream(false);

        long started = System.nanoTime();
        Process process = pb.start();

        StringBuilder stdout = new StringBuilder();
        StringBuilder stderr = new StringBuilder();
        Thread outPump = drain(process.getInputStream(), stdout);
        Thread errPump = drain(process.getErrorStream(), stderr);

        boolean finished = process.waitFor(timeout.toMillis(), TimeUnit.MILLISECONDS);
        if (!finished) {
            process.destroyForcibly();
            process.waitFor(5, TimeUnit.SECONDS);
            outPump.join(1000);
            errPump.join(1000);
            return new ProcessResult(
                -1, stdout.toString(), stderr.toString(),
                true, Duration.ofNanos(System.nanoTime() - started));
        }
        outPump.join();
        errPump.join();
        return new ProcessResult(
            process.exitValue(), stdout.toString(), stderr.toString(),
            false, Duration.ofNanos(System.nanoTime() - started));
    }

    /**
     * Запускает процесс, преобразуя IOException и InterruptedException в
     * ProcessExecutionException.
     *
     * @param workingDir рабочий каталог процесса
     * @param command    список аргументов команды
     * @param timeout    максимальное время выполнения
     * @return результат выполнения процесса
     * @throws ProcessExecutionException при ошибке запуска или прерывании
     */
    public ProcessResult runUnchecked(Path workingDir, List<String> command, Duration timeout) {
        try {
            return run(workingDir, command, timeout);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ProcessExecutionException("interrupted while running " + command, e);
        } catch (IOException e) {
            throw new ProcessExecutionException("failed to start " + command, e);
        }
    }
}
