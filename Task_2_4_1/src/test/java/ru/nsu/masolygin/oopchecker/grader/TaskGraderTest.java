package ru.nsu.masolygin.oopchecker.grader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.oopchecker.domain.Task;
import ru.nsu.masolygin.oopchecker.domain.courseconfig.CourseConfigBuilder;
import ru.nsu.masolygin.oopchecker.runner.TaskExecutionResult;
import ru.nsu.masolygin.oopchecker.runner.TestReport;

class TaskGraderTest {

    private CourseConfigBuilder cfgBuilder;
    private TaskGrader grader;

    @BeforeEach
    void setUp() {
        cfgBuilder = new CourseConfigBuilder();
        cfgBuilder.addTask(new Task("t1", "Task", 10,
            LocalDate.of(2024, 1, 10), LocalDate.of(2024, 1, 20), null));
        cfgBuilder.getSettingsBuilder().setLatePenalty(0.5);
        grader = new TaskGrader(cfgBuilder.build());
    }

    private TaskExecutionResult ok(LocalDate date) {
        return new TaskExecutionResult("t1", "u1", true, true, true,
            new TestReport(10, 0, 0), date);
    }

    @Test
    void unknownTaskIdThrows() {
        TaskExecutionResult r = new TaskExecutionResult("unknown", "u1", true, true, true,
            new TestReport(10, 0, 0), LocalDate.of(2024, 1, 5));
        assertThrows(IllegalArgumentException.class, () -> grader.grade(r));
    }

    @Test
    void buildFailedHasZeroBase() {
        TaskExecutionResult r = TaskExecutionResult.notSubmitted("t1", "u1");
        GradingResult g = grader.grade(r);
        assertEquals(0.0, g.base(), 1e-9);
    }

    @Test
    void buildFailedCommentSet() {
        GradingResult g = grader.grade(TaskExecutionResult.notSubmitted("t1", "u1"));
        assertEquals("build failed", g.comment());
    }

    @Test
    void afterHardDeadlineCommentSet() {
        TaskExecutionResult r = ok(LocalDate.of(2024, 1, 25));
        GradingResult g = grader.grade(r);
        assertEquals("after hard deadline", g.comment());
    }

    @Test
    void afterHardDeadlineKeepsBase() {
        TaskExecutionResult r = ok(LocalDate.of(2024, 1, 25));
        GradingResult g = grader.grade(r);
        assertEquals(10.0, g.base(), 1e-9);
    }

    @Test
    void afterHardDeadlineForcesScoreToZero() {
        TaskExecutionResult r = ok(LocalDate.of(2024, 1, 25));
        GradingResult g = grader.grade(r);
        assertEquals(0.0, g.score(), 1e-9);
    }

    @Test
    void okSubmissionHasEmptyComment() {
        GradingResult g = grader.grade(ok(LocalDate.of(2024, 1, 5)));
        assertEquals("", g.comment());
    }

    @Test
    void docsPenaltyIsAppliedFromSettings() {
        cfgBuilder.getSettingsBuilder().setDocsPenalty(0.4);
        grader = new TaskGrader(cfgBuilder.build());
        TaskExecutionResult r = new TaskExecutionResult("t1", "u1", true, false, true,
            new TestReport(10, 0, 0), LocalDate.of(2024, 1, 5));
        assertEquals(6.0, grader.grade(r).score(), 1e-9);
    }

    @Test
    void stylePenaltyIsAppliedFromSettings() {
        cfgBuilder.getSettingsBuilder().setStylePenalty(0.3);
        grader = new TaskGrader(cfgBuilder.build());
        TaskExecutionResult r = new TaskExecutionResult("t1", "u1", true, true, false,
            new TestReport(10, 0, 0), LocalDate.of(2024, 1, 5));
        assertEquals(7.0, grader.grade(r).score(), 1e-9);
    }

    @Test
    void qualityCannotGoNegative() {
        cfgBuilder.getSettingsBuilder().setDocsPenalty(0.7);
        cfgBuilder.getSettingsBuilder().setStylePenalty(0.7);
        grader = new TaskGrader(cfgBuilder.build());
        TaskExecutionResult r = new TaskExecutionResult("t1", "u1", true, false, false,
            new TestReport(10, 0, 0), LocalDate.of(2024, 1, 5));
        assertEquals(0.0, grader.grade(r).score(), 1e-9);
    }

    @Test
    void latePenaltyPointsArePositiveWhenLate() {
        TaskExecutionResult r = ok(LocalDate.of(2024, 1, 12));
        assertTrue(grader.grade(r).latePenaltyPoints() > 0);
    }
}
