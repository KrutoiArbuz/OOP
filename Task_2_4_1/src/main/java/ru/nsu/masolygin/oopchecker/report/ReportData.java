package ru.nsu.masolygin.oopchecker.report;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import ru.nsu.masolygin.oopchecker.runner.TaskExecutionResult;

/**
 * Обёртка над результатами проверки задач, предоставляющая удобные методы поиска.
 */
public class ReportData {

    private final List<TaskExecutionResult> all;

    /**
     * Создаёт обёртку над списком результатов.
     *
     * @param results результаты проверки
     */
    public ReportData(List<TaskExecutionResult> results) {
        Objects.requireNonNull(results, "results must not be null");
        this.all = List.copyOf(results);
    }

    /**
     * Ищет результат по ID задачи и GitHub нику студента.
     *
     * @param taskId ID задачи
     * @param github GitHub ник студента
     * @return результат, обёрнутый в Optional
     */
    public Optional<TaskExecutionResult> find(String taskId, String github) {
        return all.stream()
            .filter(r -> r.taskId().equals(taskId) && r.studentGithub().equals(github))
            .findFirst();
    }

    /**
     * Возвращает все результаты конкретного студента.
     *
     * @param github GitHub ник студента
     * @return результаты студента
     */
    public List<TaskExecutionResult> of(String github) {
        return all.stream()
            .filter(r -> r.studentGithub().equals(github))
            .toList();
    }
}
