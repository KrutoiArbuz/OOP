package ru.nsu.masolygin.oopchecker.cli;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import ru.nsu.masolygin.oopchecker.domain.SemesterInfo;
import ru.nsu.masolygin.oopchecker.domain.courseconfig.CourseConfig;
import ru.nsu.masolygin.oopchecker.grader.StudentActivity;
import ru.nsu.masolygin.oopchecker.vcs.GitClient;

/**
 * Вычисляет активность студентов по количеству недель с коммитами за семестр.
 */
public class ActivityCollector {

    private final Path workDir;
    private final GitClient git;

    /**
     * Создаёт сборщик активности.
     *
     * @param workDir рабочая директория с репозиториями студентов
     * @param git     git клиент
     */
    public ActivityCollector(Path workDir, GitClient git) {
        this.workDir = workDir;
        this.git = git;
    }

    /**
     * Вычисляет активность для каждого студента из {@code githubs}.
     *
     * <p>Берёт первый настроенный семестр. Для каждой учебной недели проверяет,
     * есть ли хотя бы один коммит.
     *
     * @param config  конфигурация курса с настройками семестра
     * @param githubs GitHub ники студентов
     * @return карта github → активность
     * @throws IllegalStateException если ни один семестр не настроен
     */
    public Map<String, StudentActivity> collect(CourseConfig config, Set<String> githubs) {
        SemesterInfo semester = config.settings().semesters().values().stream()
            .findFirst()
            .orElseThrow(() -> new IllegalStateException(
                "No semester configured — add a semester block to the course DSL"));

        LocalDate semStart = semester.startDate();
        int weeks = semester.weeks();

        Map<String, StudentActivity> result = new HashMap<>();
        for (String github : githubs) {
            Path repo = workDir.resolve(github);
            int active = countActiveWeeks(repo, semStart, weeks);
            System.err.println(
                "[oop-checker]   " + github + " activity: " + active + "/" + weeks);
            result.put(github, new StudentActivity(github, active, weeks));
        }
        return result;
    }

    private int countActiveWeeks(Path repo, LocalDate semStart, int weeks) {
        int active = 0;
        for (int i = 0; i < weeks; i++) {
            if (git.hasCommitInWeek(repo, semStart.plusWeeks(i))) {
                active++;
            }
        }
        return active;
    }
}
