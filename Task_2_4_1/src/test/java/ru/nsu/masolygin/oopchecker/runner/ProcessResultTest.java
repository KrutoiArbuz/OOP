package ru.nsu.masolygin.oopchecker.runner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import org.junit.jupiter.api.Test;

class ProcessResultTest {

    @Test
    void successReturnsTrueWhenExitCodeZeroAndNotTimedOut() {
        ProcessResult r = new ProcessResult(0, "output", "", false, Duration.ofSeconds(1));
        assertTrue(r.success());
    }

    @Test
    void successReturnsFalseWhenExitCodeNonZero() {
        ProcessResult r = new ProcessResult(1, "", "error", false, Duration.ofSeconds(1));
        assertFalse(r.success());
    }

    @Test
    void successReturnsFalseWhenTimedOut() {
        ProcessResult r = new ProcessResult(0, "", "", true, Duration.ofSeconds(30));
        assertFalse(r.success());
    }

    @Test
    void successReturnsFalseWhenTimedOutAndNonZeroExit() {
        ProcessResult r = new ProcessResult(137, "", "", true, Duration.ofSeconds(30));
        assertFalse(r.success());
    }

    @Test
    void fieldValuesAreAccessible() {
        Duration dur = Duration.ofMillis(500);
        ProcessResult r = new ProcessResult(0, "out", "err", false, dur);
        assertEquals(0, r.exitCode());
        assertEquals("out", r.stdout());
        assertEquals("err", r.stderr());
        assertFalse(r.timedOut());
        assertEquals(dur, r.duration());
    }

    @Test
    void negativeExitCodeIsNotSuccess() {
        ProcessResult r = new ProcessResult(-1, "", "", false, Duration.ZERO);
        assertFalse(r.success());
    }
}
