package ru.nsu.masolygin.oopchecker.dsl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.oopchecker.domain.coursesettings.CourseSettings;
import ru.nsu.masolygin.oopchecker.domain.coursesettings.CourseSettingsBuilder;

class SettingsDelegateTest {

    private CourseSettingsBuilder builder;
    private SettingsDelegate delegate;

    @BeforeEach
    void setUp() {
        builder = new CourseSettingsBuilder();
        delegate = new SettingsDelegate(builder);
    }

    private CourseSettings build() {
        return builder.build();
    }

    @Test
    void latePenaltyDelegates() {
        delegate.latePenalty(0.3);
        assertEquals(0.3, build().latePenalty(), 1e-9);
    }

    @Test
    void testTimeoutDelegates() {
        delegate.testTimeoutSeconds(180);
        assertEquals(180L, build().testTimeoutSeconds());
    }

    @Test
    void buildTimeoutDelegates() {
        delegate.buildTimeoutSeconds(600);
        assertEquals(600L, build().buildTimeoutSeconds());
    }

    @Test
    void activityWeightDelegates() {
        delegate.activityWeight(0.4);
        assertEquals(0.4, build().activityWeight(), 1e-9);
    }

    @Test
    void docsPenaltyDelegates() {
        delegate.docsPenalty(0.15);
        assertEquals(0.15, build().docsPenalty(), 1e-9);
    }

    @Test
    void stylePenaltyDelegates() {
        delegate.stylePenalty(0.25);
        assertEquals(0.25, build().stylePenalty(), 1e-9);
    }

    @Test
    void gradeThresholdDelegatesViaMap() {
        delegate.gradeThreshold(Map.of("min", 80, "grade", "отлично"));
        assertEquals("отлично", build().gradeFor(85));
    }

    @Test
    void extraPointsDelegatesViaMap() {
        delegate.extraPoints(Map.of("task", "t1", "student", "alice", "points", 3));
        assertEquals(3, build().getExtraPoints("t1", "alice"));
    }

    @Test
    void invalidPenaltyDelegatesAndThrows() {
        assertThrows(IllegalArgumentException.class, () -> delegate.latePenalty(1.5));
    }
}
