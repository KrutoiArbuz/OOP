package ru.nsu.masolygin.oopchecker.domain;

import java.time.LocalDate;

/**
 * Лабораторная работа с мягким и жёстким дедлайном.
 *
 * @param id           идентификатор задачи
 * @param name         название задачи
 * @param maxPoints    максимум баллов
 * @param softDeadline мягкий дедлайн (после него штраф)
 * @param hardDeadline жёсткий дедлайн (после него 0 баллов)
 * @param labPath      путь к каталогу задачи в репозитории (null означает id)
 */
public record Task(String id, String name, int maxPoints, LocalDate softDeadline,
                   LocalDate hardDeadline, String labPath) {

    /**
     * Компактный конструктор: нормализует путь к директории.
     */
    public Task {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id не может быть пустым");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name не может быть пустым");
        }
        if (maxPoints <= 0) {
            throw new IllegalArgumentException("maxPoints должен быть > 0, передано: " + maxPoints);
        }
        if (labPath == null || labPath.isBlank()) {
            labPath = id;
        }
    }


}
