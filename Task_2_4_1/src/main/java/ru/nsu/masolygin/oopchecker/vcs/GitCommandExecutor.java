package ru.nsu.masolygin.oopchecker.vcs;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import ru.nsu.masolygin.oopchecker.runner.ProcessExecutionException;
import ru.nsu.masolygin.oopchecker.runner.ProcessResult;
import ru.nsu.masolygin.oopchecker.runner.ProcessRunner;

/**
 * Исполнитель git команд через ProcessRunner с преобразованием ошибок в GitException.
 */
final class GitCommandExecutor {

    private final ProcessRunner runner;

    /**
     * Создаёт исполнитель с заданным процесс-раннером.
     *
     * @param runner раннер для запуска команд
     */
    GitCommandExecutor(ProcessRunner runner) {
        this.runner = runner;
    }

    /**
     * Возвращает переменные окружения для неинтерактивного режима git.
     *
     * @return отображение переменных окружения
     */
    static Map<String, String> nonInteractiveEnv() {
        return Map.of(
            "GIT_TERMINAL_PROMPT", "0",
            "GIT_ASKPASS", "echo",
            "SSH_ASKPASS", "echo",
            "GIT_LFS_SKIP_SMUDGE", "1"
        );
    }

    /**
     * Запускает git команду и выбрасывает исключение при ошибке.
     *
     * @param dir рабочий каталог
     * @param cmd аргументы команды
     * @return результат выполнения
     * @throws GitException если команда завершилась с ошибкой
     */
    ProcessResult run(Path dir, List<String> cmd) {
        ProcessResult r = safeRun(dir, cmd);
        if (!r.success()) {
            throw new GitException("git "
                + String.join(" ", cmd.subList(1, cmd.size()))
                + " failed: exit=" + r.exitCode()
                + (r.timedOut() ? " (timed out)" : "")
                + "\nstderr: " + r.stderr().trim());
        }
        return r;
    }

    /**
     * Запускает git команду без проверки кода возврата.
     *
     * @param dir рабочий каталог
     * @param cmd аргументы команды
     * @return результат выполнения (может быть неудачным)
     * @throws GitException если не удалось запустить процесс
     */
    ProcessResult safeRun(Path dir, List<String> cmd) {
        try {
            return runner.run(dir, cmd);
        } catch (ProcessExecutionException e) {
            throw new GitException("failed to exec " + cmd, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new GitException("interrupted while running " + cmd, e);
        } catch (IOException e) {
            throw new GitException("failed to start " + cmd, e);
        }
    }
}