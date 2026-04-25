package ru.nsu.masolygin.oopchecker.grader;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class StudentActivityTest {

    @Test
    void ratioIsOneWhenAllWeeksActive() {
        StudentActivity a = new StudentActivity("alice", 17, 17);
        assertEquals(1.0, a.ratio(), 1e-9);
    }

    @Test
    void ratioIsZeroWhenNoActiveWeeks() {
        StudentActivity a = new StudentActivity("alice", 0, 17);
        assertEquals(0.0, a.ratio(), 1e-9);
    }

    @Test
    void ratioIsZeroWhenTotalWeeksIsZero() {
        StudentActivity a = new StudentActivity("alice", 0, 0);
        assertEquals(0.0, a.ratio(), 1e-9);
    }

    @Test
    void ratioIsHalfWhenHalfActive() {
        StudentActivity a = new StudentActivity("alice", 8, 16);
        assertEquals(0.5, a.ratio(), 1e-9);
    }

    @Test
    void fieldValuesAreAccessible() {
        StudentActivity a = new StudentActivity("bob", 10, 17);
        assertEquals("bob", a.studentGithub());
        assertEquals(10, a.activeWeeks());
        assertEquals(17, a.totalWeeks());
    }
}
