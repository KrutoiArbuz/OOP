package ru.nsu.masolygin.oopchecker.runner;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import org.junit.jupiter.api.Test;

class ProcessResultTest {

    @Test
    void successWhenZeroExitAndNotTimedOut() {
        ProcessResult r = new ProcessResult(0, "out", "", false, Duration.ofMillis(10));
        assertTrue(r.success());
    }

    @Test
    void notSuccessWhenNonZeroExit() {
        ProcessResult r = new ProcessResult(1, "", "err", false, Duration.ZERO);
        assertFalse(r.success());
    }

    @Test
    void notSuccessWhenTimedOut() {
        ProcessResult r = new ProcessResult(0, "", "", true, Duration.ofSeconds(30));
        assertFalse(r.success());
    }

    @Test
    void notSuccessWhenTimedOutAndNonZero() {
        ProcessResult r = new ProcessResult(137, "", "", true, Duration.ofSeconds(30));
        assertFalse(r.success());
    }

    @Test
    void notSuccessWhenNegativeExit() {
        ProcessResult r = new ProcessResult(-1, "", "", false, Duration.ZERO);
        assertFalse(r.success());
    }

    @Test
    void allFieldsAreAccessible() {
        Duration d = Duration.ofMillis(500);
        ProcessResult r = new ProcessResult(0, "stdout", "stderr", false, d);
        assertAll(
            () -> assertEquals(0, r.exitCode()),
            () -> assertEquals("stdout", r.stdout()),
            () -> assertEquals("stderr", r.stderr()),
            () -> assertFalse(r.timedOut()),
            () -> assertEquals(d, r.duration())
        );
    }
}
