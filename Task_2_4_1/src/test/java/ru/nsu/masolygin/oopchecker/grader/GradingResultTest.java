package ru.nsu.masolygin.oopchecker.grader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class GradingResultTest {

    @Test
    void fieldsAreAccessible() {
        GradingResult r = new GradingResult("t1", "alice", 8.0, 2.0, 1, 7.0, "");
        assertEquals("t1", r.taskId());
        assertEquals("alice", r.studentGithub());
        assertEquals(8.0, r.base(), 1e-9);
        assertEquals(2.0, r.latePenaltyApplied(), 1e-9);
        assertEquals(1, r.extraPoints());
        assertEquals(7.0, r.score(), 1e-9);
        assertEquals("", r.comment());
    }

    @Test
    void zeroScoreIsRepresentable() {
        GradingResult r = new GradingResult("t1", "alice", 0.0, 0.0, 0, 0.0, "build failed");
        assertEquals(0.0, r.score(), 1e-9);
        assertEquals("build failed", r.comment());
    }

    @Test
    void scoreCanExceedBaseWhenExtraPointsAdded() {
        GradingResult r = new GradingResult("t1", "alice", 5.0, 0.0, 2, 7.0, "");
        assertTrue(r.score() > r.base());
    }

    @Test
    void penaltyIsPositiveWhenApplied() {
        GradingResult r = new GradingResult("t1", "alice", 10.0, 5.0, 0, 5.0, "");
        assertTrue(r.latePenaltyApplied() > 0);
    }
}
