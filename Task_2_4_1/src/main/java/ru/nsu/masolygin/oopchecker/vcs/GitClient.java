package ru.nsu.masolygin.oopchecker.vcs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import ru.nsu.masolygin.oopchecker.runner.ProcessResult;
import ru.nsu.masolygin.oopchecker.runner.ProcessRunner;

/**
 * Обёртка над консольным git'ом через ProcessBuilder; все команды non-interactive.
 */
public class GitClient {

    private final GitCommandExecutor executor;

    /**
     * Конструктор с таймаутом 5 минут и non-interactive переменными окружения.
     */
    public GitClient() {
        this(new ProcessRunner(Duration.ofMinutes(5), nonInteractiveEnv()));
    }

    /**
     * Конструктор с кастомным ProcessRunner.
     *
     * @param runner раннер процессов
     */
    public GitClient(ProcessRunner runner) {
        this(new GitCommandExecutor(runner));
    }

    /**
     * Конструктор с кастомным исполнителем команд.
     *
     * @param executor исполнитель
     */
    GitClient(GitCommandExecutor executor) {
        this.executor = executor;
    }

    /**
     * Возвращает переменные окружения для non-interactive режима.
     *
     * @return переменные
     */
    public static Map<String, String> nonInteractiveEnv() {
        return GitCommandExecutor.nonInteractiveEnv();
    }

    private static boolean isInteractiveHelper(String helper) {
        String h = helper.toLowerCase();
        return h.contains("manager") || h.contains("wincred")
            || h.contains("osxkeychain") || h.contains("cache");
    }

    private static void ensureParent(Path target) {
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
     * @return путь к репозиторию
     */
    public Path cloneOrUpdate(String repoUrl, Path target) {
        if (Files.exists(target.resolve(".git"))) {
            executor.run(target, List.of("git", "fetch", "--all", "--prune"));
        } else {
            ensureParent(target);
            executor.run(null, List.of("git", "clone", repoUrl, target.toString()));
        }
        return target;
    }

    /**
     * Переключается на дефолтную ветку origin и жёстко выравнивает HEAD.
     *
     * @param repo путь к репозиторию
     * @return имя ветки
     */
    public String checkoutDefaultBranch(Path repo) {
        String branch = resolveDefaultBranch(repo);
        executor.run(repo, List.of("git", "checkout", branch));
        executor.run(repo, List.of("git", "reset", "--hard", "origin/" + branch));
        return branch;
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

    /**
     * Возвращает коммиты в полуоткрытом интервале [since, until).
     *
     * @param repo           путь к репозиторию
     * @param sinceInclusive начало интервала (включительно)
     * @param untilExclusive конец интервала (исключительно)
     * @return список коммитов
     */
    public List<Commit> log(Path repo, LocalDate sinceInclusive, LocalDate untilExclusive) {
        List<String> cmd = new ArrayList<>(List.of(
            "git", "log", GitLogParser.prettyFormatArg(),
            "--since=" + sinceInclusive,
            "--until=" + untilExclusive
        ));
        ProcessResult r = executor.run(repo, cmd);
        return GitLogParser.parseMany(r.stdout());
    }

    /**
     * Проверяет, есть ли коммиты в указанной неделе.
     *
     * @param repo      путь к репозиторию
     * @param weekStart первый день недели
     * @return true если есть коммиты
     */
    public boolean hasCommitInWeek(Path repo, LocalDate weekStart) {
        return !log(repo, weekStart, weekStart.plusDays(7)).isEmpty();
    }

    /**
     * Возвращает последний коммит до указанной даты.
     *
     * @param repo путь к репозиторию
     * @param date дата
     * @return последний коммит или пусто
     */
    public Optional<Commit> lastCommitBefore(Path repo, LocalDate date) {
        List<String> cmd = List.of("git", "log", "-1", GitLogParser.prettyFormatArg(),
            "--until=" + date);
        ProcessResult r = executor.safeRun(repo, cmd);
        if (!r.success()) {
            return Optional.empty();
        }
        return GitLogParser.parseSingle(r.stdout());
    }

    /**
     * Возвращает дату последнего коммита, затронувшего указанный путь.
     *
     * @param repo    путь к репозиторию
     * @param relPath путь внутри репозитория
     * @return дата сдачи или пусто
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
}
