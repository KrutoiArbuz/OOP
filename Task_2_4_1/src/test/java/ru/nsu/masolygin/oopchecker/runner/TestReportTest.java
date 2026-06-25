package ru.nsu.masolygin.oopchecker.runner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class TestReportTest {

    @Test
    void emptyConstantHasZeroCounts() {
        assertEquals(0, TestReport.EMPTY.passed());
        assertEquals(0, TestReport.EMPTY.failed());
        assertEquals(0, TestReport.EMPTY.skipped());
    }

    @Test
    void emptyConstantIsNotNull() {
        assertNotNull(TestReport.EMPTY);
    }

    @ParameterizedTest
    @CsvSource({
        "10, 0, 0, 1.0",
        "0, 10, 0, 0.0",
        "5, 5, 0, 0.5",
        "4, 1, 5, 0.8",
        "0, 0, 0, 0.0"
    })
    void passRatioComputedCorrectly(int passed, int failed, int skipped, double expected) {
        TestReport r = new TestReport(passed, failed, skipped);
        assertEquals(expected, r.passRatio(), 1e-9);
    }

    @Test
    void passRatioIgnoresSkipped() {
        TestReport withSkipped = new TestReport(4, 1, 100);
        TestReport withoutSkipped = new TestReport(4, 1, 0);
        assertEquals(withoutSkipped.passRatio(), withSkipped.passRatio(), 1e-9);
    }

    @Test
    void allFieldsAreAccessible() {
        TestReport r = new TestReport(7, 2, 1);
        assertEquals(7, r.passed());
        assertEquals(2, r.failed());
        assertEquals(1, r.skipped());
    }

    @Test
    void recordEqualityByValue() {
        assertEquals(new TestReport(5, 0, 0), new TestReport(5, 0, 0));
    }
}
