package ru.nsu.masolygin.oopchecker.runner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Запускает внешние процессы: параллельный drain stdout/stderr, kill по таймауту.
 */
public class ProcessRunner {

    private final Duration defaultTimeout;
    private final Map<String, String> extraEnv;

    public ProcessRunner(Duration defaultTimeout, Map<String, String> extraEnv) {
        this.defaultTimeout = defaultTimeout;
        this.extraEnv = Map.copyOf(extraEnv);
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
     * @return результат выполнения
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
            process.descendants().forEach(ProcessHandle::destroyForcibly);
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
     * Daemon-поток, читающий stream построчно в sink.
     *
     * @param stream входной поток
     * @param sink   приёмник строк
     * @return запущенный поток
     */
    private Thread drain(InputStream stream, StringBuilder sink) {
        Thread t = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    synchronized (sink) {
                        sink.append(line).append('\n');
                    }
                }
            } catch (IOException e) {
                System.err.println("[process-pump] read error: " + e.getMessage());
            }
        }, "process-pump");
        t.setDaemon(true);
        t.start();
        return t;
    }
}
