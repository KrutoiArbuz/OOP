package ru.nsu.masolygin.oopchecker.grader;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import ru.nsu.masolygin.oopchecker.domain.Checkpoint;
import ru.nsu.masolygin.oopchecker.domain.CourseConfig;
import ru.nsu.masolygin.oopchecker.domain.CourseSettings;
import ru.nsu.masolygin.oopchecker.domain.Task;
import ru.nsu.masolygin.oopchecker.runner.TaskExecutionResult;

/**
 * Пересчитывает результаты проверок в баллы и оценки согласно критериям курса с учётом дедлайнов,
 * штрафов и активности.
 */
public class Grader {

    private static final double DOCS_PENALTY = 0.2;
    private static final double STYLE_PENALTY = 0.2;

    private final CourseConfig config;

    /**
     * Создаёт оценщик с конфигурацией курса.
     *
     * @param config конфигурация курса с задачами и настройками
     */
    public Grader(CourseConfig config) {
        this.config = Objects.requireNonNull(config);
    }

    /**
     * Вычисляет развёрнутую оценку задачи с учётом дедлайнов и штрафов.
     *
     * @param result результат выполнения задачи
     * @return развёрнутая оценка с базовым баллом и финальным результатом
     */
    public GradingResult grade(TaskExecutionResult result) {
        Task task = config.findTask(result.taskId())
            .orElseThrow(() -> new IllegalArgumentException(
                "Unknown task: " + result.taskId()));
        CourseSettings settings = config.settings();
        int extra = settings.getExtraPoints(result.taskId(), result.studentGithub());

        if (!result.compileOk()) {
            return new GradingResult(result.taskId(), result.studentGithub(),
                0.0, 0.0, extra, Math.max(0, extra),
                "build failed");
        }

        double quality = 1.0;
        if (!result.docsOk()) {
            quality -= DOCS_PENALTY;
        }
        if (!result.styleOk()) {
            quality -= STYLE_PENALTY;
        }
        quality = Math.max(0.0, quality);

        double base = task.maxPoints() * result.tests().passRatio() * quality;

        LocalDate submitted = result.submissionDate().orElse(null);
        if (submitted != null && task.hardDeadline() != null
            && submitted.isAfter(task.hardDeadline())) {
            return new GradingResult(result.taskId(), result.studentGithub(),
                base, base, extra, Math.max(0, extra),
                "after hard deadline");
        }

        double penalty = 0.0;
        if (submitted != null && task.softDeadline() != null
            && submitted.isAfter(task.softDeadline())) {
            penalty = base * settings.getLatePenalty();
            base -= penalty;
        }

        double score = Math.max(0, base + extra);
        return new GradingResult(result.taskId(), result.studentGithub(),
            base + penalty, penalty, extra, score, "");
    }

    /**
     * Вычисляет сумму баллов студента по всем задачам без учёта активности.
     *
     * @param results результаты выполнения задач
     * @return сумма баллов
     */
    public double totalScore(List<TaskExecutionResult> results) {
        return results.stream().mapToDouble(r -> grade(r).score()).sum();
    }

    /**
     * Вычисляет итоговый балл с модификатором активности.
     *
     * @param results  результаты выполнения задач
     * @param activity активность студента за семестр
     * @return балл с учётом веса активности
     */
    public double totalWithActivity(List<TaskExecutionResult> results, StudentActivity activity) {
        double total = totalScore(results);
        double weight = config.settings().getActivityWeight();
        if (weight <= 0) {
            return total;
        }
        return total * ((1.0 - weight) + weight * activity.ratio());
    }

    /**
     * Вычисляет балл на контрольную точку (только задачи с дедлайном ≤ даты точки).
     *
     * @param results    результаты выполнения задач
     * @param checkpoint контрольная точка
     * @return балл на контрольную точку
     */
    public double checkpointScore(List<TaskExecutionResult> results, Checkpoint checkpoint) {
        return results.stream()
            .filter(r -> isInCheckpointScope(r, checkpoint))
            .mapToDouble(r -> grade(r).score())
            .sum();
    }

    /**
     * Проверяет, входит ли задача в окно контрольной точки [startDate, date].
     *
     * @param result     результат выполнения
     * @param checkpoint контрольная точка
     * @return true если задача входит в окно точки
     */
    private boolean isInCheckpointScope(TaskExecutionResult result, Checkpoint checkpoint) {
        Task task = config.findTask(result.taskId()).orElse(null);
        if (task == null) {
            return true;
        }
        LocalDate dl = task.softDeadline() != null ? task.softDeadline() : task.hardDeadline();
        if (dl == null) {
            return true;
        }
        if (dl.isAfter(checkpoint.date())) {
            return false;
        }
        return checkpoint.startDate() == null || !dl.isBefore(checkpoint.startDate());
    }

    /**
     * Возвращает максимально возможный балл за набор задач.
     *
     * @param taskIds список идентификаторов задач
     * @return сумма максимальных баллов
     */
    public double maxScoreFor(List<String> taskIds) {
        return taskIds.stream()
            .map(config::findTask)
            .filter(java.util.Optional::isPresent)
            .mapToInt(t -> t.get().maxPoints())
            .sum();
    }

    /**
     * Преобразует балл в оценку согласно шкале курса.
     *
     * @param score    текущий балл
     * @param maxScore максимальный балл
     * @return буквенная оценка
     */
    public String gradeFor(double score, double maxScore) {
        int percent = maxScore <= 0 ? 0 : (int) Math.round(100.0 * score / maxScore);
        return config.settings().gradeFor(percent);
    }
}
