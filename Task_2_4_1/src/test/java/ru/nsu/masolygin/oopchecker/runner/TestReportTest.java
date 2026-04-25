package ru.nsu.masolygin.oopchecker.runner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class TestReportTest {

    @Test
    void emptyConstantHasZeroCounts() {
        assertEquals(0, TestReport.EMPTY.passed());
        assertEquals(0, TestReport.EMPTY.failed());
        assertEquals(0, TestReport.EMPTY.skipped());
    }

    @Test
    void totalSumsAllThreeCounters() {
        TestReport r = new TestReport(5, 2, 1);
        assertEquals(8, r.total());
    }

    @Test
    void passRatioIsOneWhenAllPassed() {
        TestReport r = new TestReport(10, 0, 0);
        assertEquals(1.0, r.passRatio(), 1e-9);
    }

    @Test
    void passRatioIsZeroWhenNoTests() {
        assertEquals(0.0, TestReport.EMPTY.passRatio(), 1e-9);
    }

    @Test
    void passRatioIgnoresSkipped() {
        TestReport r = new TestReport(4, 1, 5);
        assertEquals(4.0 / 5.0, r.passRatio(), 1e-9);
    }

    @Test
    void passRatioIsZeroWhenAllFailed() {
        TestReport r = new TestReport(0, 3, 0);
        assertEquals(0.0, r.passRatio(), 1e-9);
    }

    @Test
    void passRatioIsHalfWhenHalfPassed() {
        TestReport r = new TestReport(5, 5, 0);
        assertEquals(0.5, r.passRatio(), 1e-9);
    }

    @Test
    void reportFieldsAreAccessible() {
        TestReport r = new TestReport(7, 2, 1);
        assertEquals(7, r.passed());
        assertEquals(2, r.failed());
        assertEquals(1, r.skipped());
    }

    @Test
    void emptyReportIsNotNull() {
        assertNotNull(TestReport.EMPTY);
    }
}
