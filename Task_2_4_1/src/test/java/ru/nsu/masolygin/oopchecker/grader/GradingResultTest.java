package ru.nsu.masolygin.oopchecker.grader;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class GradingResultTest {

    @Test
    void allFieldsAreAccessible() {
        GradingResult r = new GradingResult("t1", "alice", 8.0, 2.0, 1, 7.0, "comment");
        assertAll(
            () -> assertEquals("t1", r.taskId()),
            () -> assertEquals("alice", r.studentGithub()),
            () -> assertEquals(8.0, r.base(), 1e-9),
            () -> assertEquals(2.0, r.latePenaltyPoints(), 1e-9),
            () -> assertEquals(1, r.extraPoints()),
            () -> assertEquals(7.0, r.score(), 1e-9),
            () -> assertEquals("comment", r.comment())
        );
    }

    @Test
    void zeroScoreCanRepresentBuildFailure() {
        GradingResult r = new GradingResult("t1", "alice", 0.0, 0.0, 0, 0.0, "build failed");
        assertEquals(0.0, r.score(), 1e-9);
        assertEquals("build failed", r.comment());
    }

    @Test
    void recordEqualityByValue() {
        GradingResult a = new GradingResult("t1", "alice", 1, 0, 0, 1, "");
        GradingResult b = new GradingResult("t1", "alice", 1, 0, 0, 1, "");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
