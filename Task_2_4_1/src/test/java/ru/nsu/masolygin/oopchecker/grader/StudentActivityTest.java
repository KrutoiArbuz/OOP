package ru.nsu.masolygin.oopchecker.grader;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class StudentActivityTest {

    @ParameterizedTest
    @CsvSource({
        "0, 17, 0.0",
        "17, 17, 1.0",
        "8, 16, 0.5",
        "1, 4, 0.25",
        "3, 4, 0.75"
    })
    void ratioReflectsActivePart(int active, int total, double expected) {
        StudentActivity a = new StudentActivity("u1", active, total);
        assertEquals(expected, a.ratio(), 1e-9);
    }

    @Test
    void ratioIsZeroWhenTotalIsZero() {
        StudentActivity a = new StudentActivity("u1", 0, 0);
        assertEquals(0.0, a.ratio(), 1e-9);
    }

    @Test
    void absentReturnsZeroCounters() {
        StudentActivity a = StudentActivity.absent("u1");
        assertEquals("u1", a.studentGithub());
        assertEquals(0, a.activeWeeks());
        assertEquals(0, a.totalWeeks());
        assertEquals(0.0, a.ratio(), 1e-9);
    }

    @Test
    void fieldsAreAccessible() {
        StudentActivity a = new StudentActivity("u1", 10, 17);
        assertEquals("u1", a.studentGithub());
        assertEquals(10, a.activeWeeks());
        assertEquals(17, a.totalWeeks());
    }
}
