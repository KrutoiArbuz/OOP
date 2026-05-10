package ru.nsu.masolygin.oopchecker.domain;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Информация о семестре.
 *
 * @param startDate начало семестра
 * @param weeks     длительность семестра в неделях
 */
public record SemesterInfo(LocalDate startDate, int weeks) {

    public SemesterInfo {
        Objects.requireNonNull(startDate, "startDate");
        if (weeks <= 0) {
            throw new IllegalArgumentException("weeks должен быть > 0, передано: " + weeks);
        }
    }
}
