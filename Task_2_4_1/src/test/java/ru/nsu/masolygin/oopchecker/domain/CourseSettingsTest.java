package ru.nsu.masolygin.oopchecker.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CourseSettingsTest {

    private CourseSettings settings;

    @BeforeEach
    void setUp() {
        settings = new CourseSettings();
    }

    @Test
    void latePenaltyDefaultIsZero() {
        assertEquals(0.0, settings.getLatePenalty(), 1e-9);
    }

    @Test
    void latePenaltyIsStoredAndRetrieved() {
        settings.setLatePenalty(0.5);
        assertEquals(0.5, settings.getLatePenalty(), 1e-9);
    }

    @Test
    void testTimeoutDefaultIs120() {
        assertEquals(120L, settings.getTestTimeoutSeconds());
    }

    @Test
    void testTimeoutIsStoredAndRetrieved() {
        settings.setTestTimeoutSeconds(300);
        assertEquals(300L, settings.getTestTimeoutSeconds());
    }

    @Test
    void activityWeightDefaultIsZero() {
        assertEquals(0.0, settings.getActivityWeight(), 1e-9);
    }

    @Test
    void activityWeightIsStoredAndRetrieved() {
        settings.setActivityWeight(0.3);
        assertEquals(0.3, settings.getActivityWeight(), 1e-9);
    }

    @Test
    void extraPointsDefaultIsZero() {
        assertEquals(0, settings.getExtraPoints("t1", "alice"));
    }

    @Test
    void extraPointsAreAddedAndRetrieved() {
        settings.addExtraPoints("t1", "alice", 2);
        assertEquals(2, settings.getExtraPoints("t1", "alice"));
    }

    @Test
    void extraPointsAccumulateForSameStudentAndTask() {
        settings.addExtraPoints("t1", "alice", 1);
        settings.addExtraPoints("t1", "alice", 2);
        assertEquals(3, settings.getExtraPoints("t1", "alice"));
    }

    @Test
    void extraPointsAreIsolatedByStudent() {
        settings.addExtraPoints("t1", "alice", 3);
        assertEquals(0, settings.getExtraPoints("t1", "bob"));
    }

    @Test
    void gradeForReturnsBestMatchingThreshold() {
        settings.addGradeThreshold(81, "отлично");
        settings.addGradeThreshold(63, "хорошо");
        settings.addGradeThreshold(44, "удовлетворительно");

        assertEquals("отлично", settings.gradeFor(100));
        assertEquals("отлично", settings.gradeFor(81));
        assertEquals("хорошо", settings.gradeFor(80));
        assertEquals("хорошо", settings.gradeFor(63));
        assertEquals("удовлетворительно", settings.gradeFor(62));
        assertEquals("удовлетворительно", settings.gradeFor(44));
    }

    @Test
    void gradeForReturnsFallbackBelowAllThresholds() {
        settings.addGradeThreshold(44, "удовлетворительно");
        assertEquals("неудовлетворительно", settings.gradeFor(43));
        assertEquals("неудовлетворительно", settings.gradeFor(0));
    }

    @Test
    void gradeForWithNoThresholdsReturnsDefault() {
        assertEquals("неудовлетворительно", settings.gradeFor(100));
    }
}
