package ru.nsu.masolygin.oopchecker.grader;

import java.time.LocalDate;
import java.util.Objects;
import ru.nsu.masolygin.oopchecker.domain.Task;
import ru.nsu.masolygin.oopchecker.domain.courseconfig.CourseConfig;
import ru.nsu.masolygin.oopchecker.domain.coursesettings.CourseSettings;
import ru.nsu.masolygin.oopchecker.runner.TaskExecutionResult;

/**
 * Вычисляет оценку одной задачи: качество, штраф за просрочку, дедлайны.
 */
public class TaskGrader {

    private final CourseConfig config;

    public TaskGrader(CourseConfig config) {
        this.config = Objects.requireNonNull(config);
    }

    /**
     * Считает GradingResult с учётом дедлайнов, качества и доп. баллов.
     *
     * @param result результат выполнения задачи
     * @return развёрнутая оценка
     */
    public GradingResult grade(TaskExecutionResult result) {
        Task task = config.findTask(result.taskId())
            .orElseThrow(() -> new IllegalArgumentException(
                "Unknown task: " + result.taskId()));
        CourseSettings settings = config.settings();
        int extra = settings.getExtraPoints(result.taskId(), result.studentGithub());

        if (!result.compileOk()) {
            return new GradingResult(result.taskId(), result.studentGithub(),
                0.0, 0.0, extra, Math.max(0, extra), "build failed");
        }

        double base =
            task.maxPoints() * result.tests().passRatio() * computeQuality(result, settings);
        LocalDate submitted = result.submissionDate().orElse(null);

        if (isAfterHardDeadline(submitted, task)) {
            return new GradingResult(result.taskId(), result.studentGithub(),
                base, base, extra, Math.max(0, extra), "after hard deadline");
        }

        double penalty = computePenalty(base, submitted, task, settings);
        double score = Math.max(0, base - penalty + extra);
        return new GradingResult(result.taskId(), result.studentGithub(),
            base, penalty, extra, score, "");
    }

    /**
     * Коэффициент качества с учётом штрафов за docs и style.
     *
     * @param result   результат выполнения
     * @param settings настройки курса
     * @return коэффициент в диапазоне [0.0, 1.0]
     */
    private double computeQuality(TaskExecutionResult result, CourseSettings settings) {
        double quality = 1.0;
        if (!result.docsOk()) {
            quality -= settings.docsPenalty();
        }
        if (!result.styleOk()) {
            quality -= settings.stylePenalty();
        }
        return Math.max(0.0, quality);
    }

    /**
     * Проверяет, сдана ли задача после жёсткого дедлайна.
     *
     * @param submitted дата последнего коммита
     * @param task      задача
     * @return true если просрочено
     */
    private boolean isAfterHardDeadline(LocalDate submitted, Task task) {
        return submitted != null
            && task.hardDeadline() != null
            && submitted.isAfter(task.hardDeadline());
    }

    /**
     * Вычисляет штраф за просрочку мягкого дедлайна.
     *
     * @param base      базовый балл
     * @param submitted дата последнего коммита
     * @param task      задача
     * @param settings  настройки курса
     * @return величина штрафа в баллах
     */
    private double computePenalty(double base, LocalDate submitted,
        Task task, CourseSettings settings) {
        if (submitted == null || task.softDeadline() == null) {
            return 0.0;
        }
        return submitted.isAfter(task.softDeadline()) ? base * settings.latePenalty() : 0.0;
    }
}
