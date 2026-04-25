package ru.nsu.masolygin.oopchecker.domain;

import java.time.LocalDate;

/**
 * Лабораторная работа с мягким и жёстким дедлайном.
 */
public record Task(
    String id,
    String name,
    int maxPoints,
    LocalDate softDeadline,
    LocalDate hardDeadline,
    String labPath
) {

    /**
     * Конструктор.
     *
     * @param id           идентификатор задачи
     * @param name         название
     * @param maxPoints    максимум баллов
     * @param softDeadline мягкий дедлайн
     * @param hardDeadline жёсткий дедлайн
     */
    public Task(String id, String name, int maxPoints,
        LocalDate softDeadline, LocalDate hardDeadline) {
        this(id, name, maxPoints, softDeadline, hardDeadline, null);
    }

    /**
     * Реальный путь к каталогу задачи; если не задан, возвращает id.
     *
     * @return путь
     */
    public String effectiveLabPath() {
        return (labPath == null || labPath.isBlank()) ? id : labPath;
    }
}
