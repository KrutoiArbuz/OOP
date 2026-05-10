package ru.nsu.masolygin.oopchecker.grader;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import ru.nsu.masolygin.oopchecker.domain.Checkpoint;
import ru.nsu.masolygin.oopchecker.domain.Task;
import ru.nsu.masolygin.oopchecker.domain.courseconfig.CourseConfig;
import ru.nsu.masolygin.oopchecker.runner.TaskExecutionResult;

/**
 * Считает итоговые баллы: по задаче, сумму, КТ, оценку по шкале.
 */
public class Grader {

    private final CourseConfig config;
    private final TaskGrader taskGrader;

    public Grader(CourseConfig config) {
        this.config = Objects.requireNonNull(config);
        this.taskGrader = new TaskGrader(config);
    }

    /**
     * Вычисляет развёрнутую оценку задачи.
     *
     * @param result результат выполнения задачи
     * @return развёрнутая оценка
     */
    public GradingResult grade(TaskExecutionResult result) {
        return taskGrader.grade(result);
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
        double weight = config.settings().activityWeight();
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
     * Преобразует балл в оценку согласно шкале курса.
     *
     * @param score текущий балл
     * @return буквенная оценка
     */
    public String gradeFor(double score) {
        return config.settings().gradeFor((int) Math.floor(score));
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
            return false;
        }
        LocalDate dl = task.softDeadline() != null ? task.softDeadline() : task.hardDeadline();
        if (dl == null) {
            return true;
        }
        if (dl.isAfter(checkpoint.date())) {
            return false;
        }
        return checkpoint.startDate()
            .map(start -> !dl.isBefore(start))
            .orElse(true);
    }
}
