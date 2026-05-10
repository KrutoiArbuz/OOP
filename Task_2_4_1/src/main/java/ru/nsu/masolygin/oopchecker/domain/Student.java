package ru.nsu.masolygin.oopchecker.domain;

/**
 * Информация о студенте в группе.
 *
 * @param github   GitHub ник студента
 * @param fullName полное имя студента
 * @param repoUrl  URL репозитория студента
 */
public record Student(String github, String fullName, String repoUrl) {

    public Student {
        if (github == null || github.isBlank()) {
            throw new IllegalArgumentException("github не может быть пустым");
        }
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("fullName не может быть пустым");
        }
        if (repoUrl == null || repoUrl.isBlank()) {
            throw new IllegalArgumentException("repoUrl не может быть пустым");
        }
    }
}
