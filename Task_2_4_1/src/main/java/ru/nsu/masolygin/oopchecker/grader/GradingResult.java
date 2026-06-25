package ru.nsu.masolygin.oopchecker.grader;

/**
 * Развёрнутая оценка задачи студента с базовым баллом, штрафами и финальным результатом.
 *
 * @param taskId            идентификатор задачи
 * @param studentGithub     GitHub ник студента
 * @param base              базовый балл без корректировок
 * @param latePenaltyPoints снятые баллы за просрочку мягкого дедлайна
 * @param extraPoints       дополнительные баллы
 * @param score             финальный балл с учётом штрафов и доп. баллов
 * @param comment           объяснение результата (если нужно)
 */
public record GradingResult(
    String taskId,
    String studentGithub,
    double base,
    double latePenaltyPoints,
    int extraPoints,
    double score,
    String comment
) {

}
