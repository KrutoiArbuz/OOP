package ru.nsu.masolygin.oopchecker.domain;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

/**
 * Контрольная точка семестра для промежуточной оценки.
 *
 * @param name              название контрольной точки
 * @param internalStartDate начало окна (включительно), null — без ограничения снизу
 * @param date              конец окна (включительно, дата контрольной точки)
 */
public record Checkpoint(String name, LocalDate internalStartDate, LocalDate date) {

    public Checkpoint {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(date, "date");
    }

    /**
     * Возвращает начало окна контрольной точки, если задано.
     *
     * @return начало окна или пусто
     */
    public Optional<LocalDate> startDate() {
        return Optional.ofNullable(internalStartDate);
    }
}
