package ru.nsu.masolygin.oopchecker.runner;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Результат проверки одной задачи одного студента с флагами компиляции, документации, стиля и
 * тестами.
 *
 * @param taskId        идентификатор задачи
 * @param studentGithub GitHub ник студента
 * @param compileOk     true если компиляция прошла успешно
 * @param docsOk        true если документация сгенерирована успешно
 * @param styleOk       true если стиль кода соответствует требованиям
 * @param tests         результаты тестов (passed/failed/skipped)
 */
public record TaskExecutionResult(
    String taskId,
    String studentGithub,
    boolean compileOk,
    boolean docsOk,
    boolean styleOk,
    TestReport tests,
    LocalDate submissionDateRaw
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
            null
        );
    }

    /**
     * Возвращает дату последнего коммита по задаче, если есть.
     *
     * @return дата сдачи или пусто
     */
    public Optional<LocalDate> submissionDate() {
        return Optional.ofNullable(submissionDateRaw);
    }
}
