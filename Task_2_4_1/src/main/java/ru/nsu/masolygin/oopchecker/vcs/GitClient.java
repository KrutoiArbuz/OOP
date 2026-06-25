package ru.nsu.masolygin.oopchecker.vcs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import ru.nsu.masolygin.oopchecker.runner.ProcessResult;
import ru.nsu.masolygin.oopchecker.runner.ProcessRunner;

/**
 * Обёртка над консольным git'ом через ProcessBuilder; все команды non-interactive.
 */
public class GitClient {

    private static final Map<String, String> NON_INTERACTIVE_ENV = Map.of(
        "GIT_TERMINAL_PROMPT", "0",
        "GIT_ASKPASS", "echo",
        "SSH_ASKPASS", "echo",
        "GIT_LFS_SKIP_SMUDGE", "1"
    );

    private final GitCommandExecutor executor;
    private final GitLogParser logParser;

    /**
     * Конструктор с таймаутом 5 минут и non-interactive переменными окружения.
     */
    public GitClient() {
        ProcessRunner defaultRunner = new ProcessRunner(Duration.ofMinutes(5), NON_INTERACTIVE_ENV);
        this.executor = new GitCommandExecutor(defaultRunner);
        this.logParser = new GitLogParser();
    }

    /**
     * Проверяет, что git установлен и настроен без интерактивных запросов пароля.
     */
    public void ensureNonInteractive() {
        ProcessResult version = executor.safeRun(null, List.of("git", "--version"));
        if (!version.success()) {
            throw new GitException("git executable not found or not working: " + version.stderr());
        }

        ProcessResult helper = executor.safeRun(null,
            List.of("git", "config", "--global", "credential.helper"));
        String h = helper.stdout().trim();
        if (!h.isEmpty() && isInteractiveHelper(h)) {
            System.err.println("[oop-checker] WARNING: git credential.helper='" + h
                + "' может запросить интерактив; GIT_TERMINAL_PROMPT=0 должен это блокировать.");
        }
    }

    /**
     * Клонирует репозиторий или обновляет существующий через fetch.
     *
     * @param repoUrl адрес репозитория
     * @param target  целевой каталог
     */
    public void cloneOrUpdate(String repoUrl, Path target) {
        if (Files.exists(target.resolve(".git"))) {
            executor.run(target, List.of("git", "fetch", "--all", "--prune"));
        } else {
            ensureParent(target);
            executor.run(null, List.of("git", "clone", repoUrl, target.toString()));
        }
    }

    /**
     * Переключается на дефолтную ветку origin и жёстко выравнивает HEAD.
     *
     * @param repo путь к репозиторию
     */
    public void checkoutDefaultBranch(Path repo) {
        String branch = resolveDefaultBranch(repo);
        executor.run(repo, List.of("git", "checkout", branch));
        executor.run(repo, List.of("git", "reset", "--hard", "origin/" + branch));
    }

    /**
     * Коммиты в полуоткрытом интервале [sinceInclusive, untilExclusive).
     *
     * @param repo           путь к репозиторию
     * @param sinceInclusive начало интервала включительно
     * @param untilExclusive конец интервала исключительно
     * @return список коммитов
     */
    public List<Commit> log(Path repo, LocalDate sinceInclusive, LocalDate untilExclusive) {
        List<String> cmd = logParser.buildLogCommand(sinceInclusive, untilExclusive);
        ProcessResult r = executor.run(repo, cmd);
        return logParser.parseMany(r.stdout());
    }

    /**
     * Проверяет наличие коммита в неделе начиная с weekStart.
     *
     * @param repo      путь к репозиторию
     * @param weekStart первый день недели
     * @return true если есть хотя бы один коммит
     */
    public boolean hasCommitInWeek(Path repo, LocalDate weekStart) {
        return !log(repo, weekStart, weekStart.plusDays(7)).isEmpty();
    }

    /**
     * Дата последнего коммита по пути relPath.
     *
     * @param repo    путь к репозиторию
     * @param relPath путь внутри репозитория
     * @return дата или пусто если коммитов нет
     */
    public Optional<LocalDate> lastCommitDateForPath(Path repo, String relPath) {
        List<String> cmd = List.of("git", "log", "-1", "--pretty=format:%aI", "--", relPath);
        ProcessResult r = executor.safeRun(repo, cmd);
        if (!r.success() || r.stdout().isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(OffsetDateTime.parse(r.stdout().trim()).toLocalDate());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private String resolveDefaultBranch(Path repo) {
        ProcessResult head = executor.safeRun(repo,
            List.of("git", "symbolic-ref", "refs/remotes/origin/HEAD"));
        if (head.success()) {
            String ref = head.stdout().trim();
            int slash = ref.lastIndexOf('/');
            if (slash > 0) {
                return ref.substring(slash + 1);
            }
        }
        for (String candidate : List.of("main", "master")) {
            if (executor.safeRun(repo,
                    List.of("git", "rev-parse", "--verify", "origin/" + candidate))
                .success()) {
                return candidate;
            }
        }
        throw new GitException("Не удалось определить дефолтную ветку в " + repo
            + " (нет ни origin/main, ни origin/master)");
    }

    private boolean isInteractiveHelper(String helper) {
        String h = helper.toLowerCase();
        return h.contains("manager") || h.contains("wincred")
            || h.contains("osxkeychain") || h.contains("cache");
    }

    private void ensureParent(Path target) {
        Path parent = target.toAbsolutePath().getParent();
        if (parent == null) {
            return;
        }
        try {
            Files.createDirectories(parent);
        } catch (IOException e) {
            throw new GitException("cannot create parent dir " + parent, e);
        }
    }
}
