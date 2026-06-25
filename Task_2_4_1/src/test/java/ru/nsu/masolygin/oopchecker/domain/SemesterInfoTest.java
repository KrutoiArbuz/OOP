package ru.nsu.masolygin.oopchecker.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SemesterInfoTest {

    @Test
    void fieldsAreAccessible() {
        LocalDate start = LocalDate.of(2025, 9, 1);
        SemesterInfo s = new SemesterInfo(start, 17);
        assertEquals(start, s.startDate());
        assertEquals(17, s.weeks());
    }

    @Test
    void nullStartDateIsRejected() {
        assertThrows(NullPointerException.class,
            () -> new SemesterInfo(null, 17));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -10})
    void nonPositiveWeeksIsRejected(int bad) {
        assertThrows(IllegalArgumentException.class,
            () -> new SemesterInfo(LocalDate.now(), bad));
    }

    @Test
    void singleWeekSemesterIsAllowed() {
        SemesterInfo s = new SemesterInfo(LocalDate.now(), 1);
        assertEquals(1, s.weeks());
    }
}
