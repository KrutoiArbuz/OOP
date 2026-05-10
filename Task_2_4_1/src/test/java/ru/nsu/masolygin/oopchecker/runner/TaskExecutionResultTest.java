package ru.nsu.masolygin.oopchecker.runner;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class TaskExecutionResultTest {

    @Test
    void notSubmittedHasFalseFlagsAndEmptyDate() {
        TaskExecutionResult r = TaskExecutionResult.notSubmitted("t1", "u1");
        assertAll(
            () -> assertEquals("t1", r.taskId()),
            () -> assertEquals("u1", r.studentGithub()),
            () -> assertFalse(r.compileOk()),
            () -> assertFalse(r.docsOk()),
            () -> assertFalse(r.styleOk()),
            () -> assertEquals(TestReport.EMPTY, r.tests()),
            () -> assertFalse(r.submissionDate().isPresent())
        );
    }

    @Test
    void submissionDateWrapsRawValue() {
        LocalDate date = LocalDate.of(2024, 5, 1);
        TaskExecutionResult r = new TaskExecutionResult("t1", "u1",
            true, true, true, TestReport.EMPTY, date);
        assertEquals(date, r.submissionDate().orElseThrow());
        assertEquals(date, r.submissionDateRaw());
    }

    @Test
    void submissionDateIsEmptyForNullRaw() {
        TaskExecutionResult r = new TaskExecutionResult("t1", "u1",
            true, true, true, TestReport.EMPTY, null);
        assertFalse(r.submissionDate().isPresent());
    }

    @Test
    void allFlagsAreStored() {
        TaskExecutionResult r = new TaskExecutionResult("t1", "u1",
            true, true, true, new TestReport(5, 0, 0), null);
        assertTrue(r.compileOk());
        assertTrue(r.docsOk());
        assertTrue(r.styleOk());
    }

    @Test
    void recordEqualityByValue() {
        TaskExecutionResult a = TaskExecutionResult.notSubmitted("t1", "u1");
        TaskExecutionResult b = TaskExecutionResult.notSubmitted("t1", "u1");
        assertEquals(a, b);
    }
}
