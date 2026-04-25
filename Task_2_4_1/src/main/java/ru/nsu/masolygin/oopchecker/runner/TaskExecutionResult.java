package ru.nsu.masolygin.oopchecker.runner;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Результат проверки одной задачи одного студента с флагами компиляции, документации, стиля и
 * тестами.
 *
 * @param taskId         идентификатор задачи
 * @param studentGithub  GitHub ник студента
 * @param compileOk      true если компиляция прошла успешно
 * @param docsOk         true если документация сгенерирована успешно
 * @param styleOk        true если стиль кода соответствует требованиям
 * @param tests          результаты тестов (passed/failed/skipped)
 * @param submissionDate дата последнего коммита, затронувшего задачу (или empty если коммитов нет)
 */
public record TaskExecutionResult(
    String taskId,
    String studentGithub,
    boolean compileOk,
    boolean docsOk,
    boolean styleOk,
    TestReport tests,
    Optional<LocalDate> submissionDate
) {

    /**
     * Создаёт результат для незасчитанной задачи.
     *
     * @param taskId        идентификатор задачи
     * @param studentGithub GitHub ник студента
     * @return результат со всеми флагами false и пустым отчётом
     */
    public static TaskExecutionResult notSubmitted(String taskId, String studentGithub) {
        return new TaskExecutionResult(
            taskId, studentGithub,
            false, false, false,
            TestReport.EMPTY,
            Optional.empty()
        );
    }
}
