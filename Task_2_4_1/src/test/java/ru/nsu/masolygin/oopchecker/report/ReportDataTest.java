package ru.nsu.masolygin.oopchecker.report;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.oopchecker.runner.TaskExecutionResult;
import ru.nsu.masolygin.oopchecker.runner.TestReport;

class ReportDataTest {

    private static final TaskExecutionResult R_T1_ALICE =
        new TaskExecutionResult("t1", "alice", true, true, true,
            new TestReport(5, 0, 0), null);
    private static final TaskExecutionResult R_T1_BOB =
        TaskExecutionResult.notSubmitted("t1", "bob");
    private static final TaskExecutionResult R_T2_ALICE =
        new TaskExecutionResult("t2", "alice", true, false, true,
            new TestReport(3, 2, 0), null);

    private ReportData data;

    @BeforeEach
    void setUp() {
        data = new ReportData(List.of(R_T1_ALICE, R_T1_BOB, R_T2_ALICE));
    }

    @Test
    void findReturnsExistingResult() {
        assertEquals(R_T1_ALICE, data.find("t1", "alice").orElseThrow());
    }

    @Test
    void findReturnsEmptyWhenTaskMissing() {
        assertFalse(data.find("unknown", "alice").isPresent());
    }

    @Test
    void findReturnsEmptyWhenStudentMissing() {
        assertFalse(data.find("t1", "nobody").isPresent());
    }

    @Test
    void ofReturnsAllResultsForStudent() {
        List<TaskExecutionResult> alice = data.of("alice");
        assertEquals(2, alice.size());
        assertTrue(alice.contains(R_T1_ALICE));
        assertTrue(alice.contains(R_T2_ALICE));
    }

    @Test
    void ofReturnsEmptyForUnknownStudent() {
        assertTrue(data.of("nobody").isEmpty());
    }

    @Test
    void ofForBobOnlyReturnsBobResults() {
        assertEquals(1, data.of("bob").size());
    }

    @Test
    void emptyDataYieldsEmptyResults() {
        ReportData empty = new ReportData(List.of());
        assertFalse(empty.find("t1", "alice").isPresent());
        assertTrue(empty.of("alice").isEmpty());
    }

    @Test
    void nullResultsAreRejected() {
        assertThrows(NullPointerException.class, () -> new ReportData(null));
    }
}
