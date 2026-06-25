package ru.nsu.masolygin.oopchecker.dsl;

import java.time.LocalDate;
import java.util.Objects;
import ru.nsu.masolygin.oopchecker.domain.SemesterInfo;

/**
 * Делегат блока {@code semester(id) { ... }}: задаёт начало и длительность семестра.
 */
public class SemesterDelegate {

    private LocalDate startDate;
    private int weeks;

    /**
     * Устанавливает дату начала семестра.
     *
     * @param isoDate дата в формате ISO-8601 (yyyy-MM-dd)
     */
    public void startDate(String isoDate) {
        this.startDate = LocalDate.parse(isoDate);
    }

    /**
     * Устанавливает длительность семестра в неделях.
     *
     * @param weeks количество учебных недель
     */
    public void weeks(int weeks) {
        this.weeks = weeks;
    }

    SemesterInfo build() {
        Objects.requireNonNull(startDate, "startDate необходим");
        if (weeks <= 0) {
            throw new IllegalArgumentException("weeks необходим и должен быть > 0");
        }
        return new SemesterInfo(startDate, weeks);
    }
}
