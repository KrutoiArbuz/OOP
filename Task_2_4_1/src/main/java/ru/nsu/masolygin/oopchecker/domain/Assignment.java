package ru.nsu.masolygin.oopchecker.domain;

/**
 * Задание на проверку, связующее задачу и студента.
 *
 * @param taskId        идентификатор задачи
 * @param studentGithub GitHub ник студента
 */
public record Assignment(String taskId, String studentGithub) {

}
