package ru.nsu.masolygin.oopchecker.domain;

/**
 * Задание на проверку, связующее задачу и студента.
 *
 * @param taskId        идентификатор задачи
 * @param studentGithub GitHub ник студента
 */
public record Assignment(String taskId, String studentGithub) {

    /**
     * Создает задание с валидацией идентификаторов.
     */
    public Assignment {
        if (taskId == null || taskId.isBlank()) {
            throw new IllegalArgumentException("taskId не может быть пустым");
        }
        if (studentGithub == null || studentGithub.isBlank()) {
            throw new IllegalArgumentException("studentGithub не может быть пустым");
        }
    }
}
