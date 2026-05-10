package ru.nsu.masolygin.oopchecker.domain.coursesettings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import java.util.TreeMap;
import org.junit.jupiter.api.Test;

class CourseSettingsTest {

    private CourseSettings build(int min, String grade) {
        TreeMap<Integer, String> scale = new TreeMap<>();
        scale.put(min, grade);
        return new CourseSettings(Map.of(), scale, Map.of(),
            0.0, 60, 300, 0.0, 0.2, 0.2);
    }

    @Test
    void getExtraPointsReturnsZeroWhenNotSet() {
        CourseSettings s = new CourseSettings(Map.of(), new TreeMap<>(), Map.of(),
            0.0, 60, 300, 0.0, 0.2, 0.2);
        assertEquals(0, s.getExtraPoints("t1", "alice"));
    }

    @Test
    void getExtraPointsReturnsStoredValue() {
        Map<String, Map<String, Integer>> extra = Map.of("t1", Map.of("alice", 5));
        CourseSettings s = new CourseSettings(extra, new TreeMap<>(), Map.of(),
            0.0, 60, 300, 0.0, 0.2, 0.2);
        assertEquals(5, s.getExtraPoints("t1", "alice"));
    }

    @Test
    void gradeForReturnsBestMatchingThreshold() {
        TreeMap<Integer, String> scale = new TreeMap<>();
        scale.put(85, "отлично");
        scale.put(70, "хорошо");
        scale.put(50, "удовлетворительно");
        CourseSettings s = new CourseSettings(Map.of(), scale, Map.of(),
            0.0, 60, 300, 0.0, 0.2, 0.2);

        assertEquals("отлично", s.gradeFor(100));
        assertEquals("отлично", s.gradeFor(85));
        assertEquals("хорошо", s.gradeFor(84));
        assertEquals("хорошо", s.gradeFor(70));
        assertEquals("удовлетворительно", s.gradeFor(69));
    }

    @Test
    void gradeForFallsBackToFailWhenBelowAllThresholds() {
        CourseSettings s = build(50, "удовлетворительно");
        assertEquals("неудовлетворительно", s.gradeFor(49));
    }

    @Test
    void gradeForReturnsFailWhenNoThresholds() {
        CourseSettings s = new CourseSettings(Map.of(), new TreeMap<>(), Map.of(),
            0.0, 60, 300, 0.0, 0.2, 0.2);
        assertEquals("неудовлетворительно", s.gradeFor(100));
    }

    @Test
    void extraPointsMapIsImmutable() {
        Map<String, Map<String, Integer>> extra = new java.util.HashMap<>();
        extra.put("t1", new java.util.HashMap<>(Map.of("alice", 5)));
        CourseSettings s = new CourseSettings(extra, new TreeMap<>(), Map.of(),
            0.0, 60, 300, 0.0, 0.2, 0.2);
        assertThrows(UnsupportedOperationException.class,
            () -> s.extraPoints().put("t2", Map.of()));
    }

    @Test
    void gradeScaleIsImmutable() {
        CourseSettings s = build(50, "x");
        assertThrows(UnsupportedOperationException.class,
            () -> s.gradeScale().put(60, "y"));
    }
}
