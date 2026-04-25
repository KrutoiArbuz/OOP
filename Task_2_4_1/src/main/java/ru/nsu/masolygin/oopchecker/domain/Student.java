package ru.nsu.masolygin.oopchecker.domain;

/**
 * Информация о студенте в группе.
 *
 * @param github   GitHub ник студента
 * @param fullName полное имя студента
 * @param repoUrl  URL репозитория студента
 */
public record Student(
    String github,
    String fullName,
    String repoUrl
) {

}
