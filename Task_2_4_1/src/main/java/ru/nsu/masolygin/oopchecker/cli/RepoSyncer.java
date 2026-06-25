package ru.nsu.masolygin.oopchecker.cli;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import ru.nsu.masolygin.oopchecker.domain.Assignment;
import ru.nsu.masolygin.oopchecker.domain.Student;
import ru.nsu.masolygin.oopchecker.domain.courseconfig.CourseConfig;
import ru.nsu.masolygin.oopchecker.vcs.GitClient;
import ru.nsu.masolygin.oopchecker.vcs.GitException;

/**
 * Клонирует или обновляет репозитории студентов и переключается на дефолтную ветку.
 */
public class RepoSyncer {

    private final Path workDir;
    private final GitClient git;

    public RepoSyncer(Path workDir, GitClient git) {
        this.workDir = workDir;
        this.git = git;
    }

    /**
     * Синхронизирует репозитории студентов из списка заданий.
     *
     * @param config      конфигурация курса для поиска студентов
     * @param assignments список заданий к проверке
     * @return GitHub ники студентов с успешно синхронизированными репозиториями
     */
    public Set<String> sync(CourseConfig config, List<Assignment> assignments) {
        Set<String> synced = new HashSet<>();
        Set<String> processed = new HashSet<>();

        for (Assignment assignment : assignments) {
            String github = assignment.studentGithub();
            if (!processed.add(github)) {
                continue;
            }

            Student student = config.findStudent(github).orElse(null);
            if (student == null) {
                System.err.println("[oop-checker] WARNING: student '" + github
                    + "' not found in any group — skipping");
                continue;
            }

            Path repo = workDir.resolve(github);
            System.err.println("[oop-checker] clone/update " + github + " <- " + student.repoUrl());

            if (!cloneOrUpdate(student.repoUrl(), repo, github)) {
                continue;
            }
            checkoutDefault(repo, github);
            synced.add(github);
        }
        return synced;
    }

    private boolean cloneOrUpdate(String repoUrl, Path repo, String github) {
        try {
            git.cloneOrUpdate(repoUrl, repo);
            return true;
        } catch (GitException e) {
            String detail = firstLine(e.getMessage());
            System.err.println("[oop-checker] WARNING: cannot clone/update "
                + github + ": " + detail);
            if (!Files.exists(repo.resolve(".git"))) {
                return false;
            }
            System.err.println("[oop-checker] using cache from previous run: " + repo);
            return true;
        }
    }

    private void checkoutDefault(Path repo, String github) {
        try {
            git.checkoutDefaultBranch(repo);
        } catch (GitException e) {
            System.err.println("[oop-checker] WARNING: cannot checkout default branch for "
                + github + ": " + firstLine(e.getMessage()));
        }
    }

    private String firstLine(String message) {
        if (message == null) {
            return "(null)";
        }
        return message.split("\n", 2)[0];
    }
}
