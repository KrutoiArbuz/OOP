package ru.nsu.masolygin.oopchecker.vcs;


import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import ru.nsu.masolygin.oopchecker.runner.ProcessExecutionException;
import ru.nsu.masolygin.oopchecker.runner.ProcessResult;
import ru.nsu.masolygin.oopchecker.runner.ProcessRunner;

/**
 * Запускает git-команды через ProcessRunner; ошибки оборачивает в GitException.
 */
public class GitCommandExecutor {

    private final ProcessRunner runner;

    public GitCommandExecutor(ProcessRunner runner) {
        this.runner = runner;
    }

    /**
     * Выполняет команду; бросает GitException при ненулевом exit-коде.
     *
     * @param dir рабочий каталог
     * @param cmd аргументы команды
     * @return результат выполнения
     */
    public ProcessResult run(Path dir, List<String> cmd) {
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
     * Выполняет команду без проверки кода возврата.
     *
     * @param dir рабочий каталог
     * @param cmd аргументы команды
     * @return результат выполнения (может быть неудачным)
     */
    public ProcessResult safeRun(Path dir, List<String> cmd) {
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
