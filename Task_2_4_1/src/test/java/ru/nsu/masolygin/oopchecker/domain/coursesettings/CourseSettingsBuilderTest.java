package ru.nsu.masolygin.oopchecker.domain.coursesettings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.nsu.masolygin.oopchecker.domain.SemesterInfo;

class CourseSettingsBuilderTest {

    private CourseSettingsBuilder builder;

    @BeforeEach
    void setUp() {
        builder = new CourseSettingsBuilder();
    }

    @Test
    void defaultValuesArePresentAfterBuild() {
        CourseSettings s = builder.build();
        assertEquals(0.0, s.latePenalty(), 1e-9);
        assertEquals(120L, s.testTimeoutSeconds());
        assertEquals(300L, s.buildTimeoutSeconds());
        assertEquals(0.0, s.activityWeight(), 1e-9);
        assertEquals(0.2, s.docsPenalty(), 1e-9);
        assertEquals(0.2, s.stylePenalty(), 1e-9);
    }

    @Test
    void latePenaltyIsStored() {
        builder.setLatePenalty(0.5);
        assertEquals(0.5, builder.build().latePenalty(), 1e-9);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.1, 1.1, -1.0, 2.0})
    void latePenaltyOutsideRangeIsRejected(double bad) {
        assertThrows(IllegalArgumentException.class, () -> builder.setLatePenalty(bad));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, 0.5, 1.0})
    void latePenaltyOnRangeBoundariesIsAccepted(double ok) {
        builder.setLatePenalty(ok);
        assertEquals(ok, builder.build().latePenalty(), 1e-9);
    }

    @ParameterizedTest
    @ValueSource(longs = {0, -1, -100})
    void nonPositiveTestTimeoutIsRejected(long bad) {
        assertThrows(IllegalArgumentException.class, () -> builder.setTestTimeoutSeconds(bad));
    }

    @ParameterizedTest
    @ValueSource(longs = {0, -1})
    void nonPositiveBuildTimeoutIsRejected(long bad) {
        assertThrows(IllegalArgumentException.class, () -> builder.setBuildTimeoutSeconds(bad));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.5, 1.5})
    void activityWeightOutsideRangeIsRejected(double bad) {
        assertThrows(IllegalArgumentException.class, () -> builder.setActivityWeight(bad));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.1, 1.5})
    void docsPenaltyOutsideRangeIsRejected(double bad) {
        assertThrows(IllegalArgumentException.class, () -> builder.setDocsPenalty(bad));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.1, 1.5})
    void stylePenaltyOutsideRangeIsRejected(double bad) {
        assertThrows(IllegalArgumentException.class, () -> builder.setStylePenalty(bad));
    }

    @Test
    void extraPointsAreStored() {
        builder.addExtraPoints("t1", "alice", 3);
        assertEquals(3, builder.build().getExtraPoints("t1", "alice"));
    }

    @Test
    void extraPointsAccumulateForSameStudent() {
        builder.addExtraPoints("t1", "alice", 1);
        builder.addExtraPoints("t1", "alice", 2);
        assertEquals(3, builder.build().getExtraPoints("t1", "alice"));
    }

    @Test
    void extraPointsAreIsolatedByTask() {
        builder.addExtraPoints("t1", "alice", 5);
        assertEquals(0, builder.build().getExtraPoints("t2", "alice"));
    }

    @Test
    void extraPointsAreIsolatedByStudent() {
        builder.addExtraPoints("t1", "alice", 5);
        assertEquals(0, builder.build().getExtraPoints("t1", "bob"));
    }

    @Test
    void gradeThresholdsAreStored() {
        builder.addGradeThreshold(85, "отлично");
        assertEquals("отлично", builder.build().gradeFor(90));
    }

    @Test
    void semesterIsStored() {
        SemesterInfo info = new SemesterInfo(LocalDate.of(2025, 9, 1), 17);
        builder.addSemester(1, info);
        assertEquals(info, builder.build().semesters().get(1));
    }

    @Test
    void multipleSemestersAreStored() {
        builder.addSemester(1, new SemesterInfo(LocalDate.of(2025, 9, 1), 17));
        builder.addSemester(2, new SemesterInfo(LocalDate.of(2026, 2, 1), 17));
        assertEquals(2, builder.build().semesters().size());
    }

    @Test
    void nullSemesterIsRejected() {
        assertThrows(NullPointerException.class, () -> builder.addSemester(1, null));
    }
}
