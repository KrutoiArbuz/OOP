package ru.nsu.masolygin.oopchecker.dsl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Method;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.oopchecker.domain.SemesterInfo;

class SemesterDelegateTest {

    private SemesterDelegate delegate;

    @BeforeEach
    void setUp() {
        delegate = new SemesterDelegate();
    }

    private SemesterInfo build() throws Exception {
        Method m = SemesterDelegate.class.getDeclaredMethod("build");
        m.setAccessible(true);
        return (SemesterInfo) m.invoke(delegate);
    }

    @Test
    void buildSetsStartDateAndWeeks() throws Exception {
        delegate.startDate("2025-09-01");
        delegate.weeks(17);
        SemesterInfo info = build();
        assertEquals(LocalDate.of(2025, 9, 1), info.startDate());
        assertEquals(17, info.weeks());
    }

    @Test
    void missingStartDateFailsBuild() {
        delegate.weeks(17);
        assertThrows(Exception.class, this::build);
    }

    @Test
    void zeroWeeksFailsBuild() {
        delegate.startDate("2025-09-01");
        delegate.weeks(0);
        assertThrows(Exception.class, this::build);
    }

    @Test
    void negativeWeeksFailBuild() {
        delegate.startDate("2025-09-01");
        delegate.weeks(-5);
        assertThrows(Exception.class, this::build);
    }

    @Test
    void invalidIsoDateFormatThrows() {
        assertThrows(java.time.format.DateTimeParseException.class,
            () -> delegate.startDate("01.09.2025"));
    }
}
