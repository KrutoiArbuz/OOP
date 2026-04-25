package ru.nsu.masolygin.oopchecker.domain;

import java.time.LocalDate;

/**
 * Контрольная точка семестра для промежуточной оценки.
 *
 * @param name      название контрольной точки
 * @param startDate начало окна (включительно), null — без ограничения снизу
 * @param date      конец окна (включительно, дата контрольной точки)
 */
public record Checkpoint(String name, LocalDate startDate, LocalDate date) {

    /**
     * Создаёт контрольную точку без ограничения начала окна.
     *
     * @param name название контрольной точки
     * @param date дата контрольной точки
     */
    public Checkpoint(String name, LocalDate date) {
        this(name, null, date);
    }
}
