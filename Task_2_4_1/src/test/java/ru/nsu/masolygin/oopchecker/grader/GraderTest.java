package ru.nsu.masolygin.oopchecker.grader;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.oopchecker.domain.Checkpoint;
import ru.nsu.masolygin.oopchecker.domain.CourseConfig;
import ru.nsu.masolygin.oopchecker.domain.Task;
import ru.nsu.masolygin.oopchecker.runner.TaskExecutionResult;
import ru.nsu.masolygin.oopchecker.runner.TestReport;

class GraderTest {

    private CourseConfig config;
    private Grader grader;

    @BeforeEach
    void setUp() {
        config = new CourseConfig();
        config.addTask(new Task("t1", "Task 1", 10,
            LocalDate.of(2024, 1, 10), LocalDate.of(2024, 1, 20)));
        config.addTask(new Task("t2", "Task 2", 20,
            LocalDate.of(2024, 2, 10), LocalDate.of(2024, 2, 20)));
        config.settings().setLatePenalty(0.5);
        grader = new Grader(config);
    }

    @Test
    void buildFailedGivesZero() {
        TaskExecutionResult r = TaskExecutionResult.notSubmitted("t1", "u1");
        assertEquals(0.0, grader.grade(r).score(), 1e-9);
    }

    @Test
    void fullGreenOnTimeGivesMaxPoints() {
        TaskExecutionResult r = new TaskExecutionResult(
            "t1", "u1", true, true, true,
            new TestReport(10, 0, 0),
            Optional.of(LocalDate.of(2024, 1, 5)));
        assertEquals(10.0, grader.grade(r).score(), 1e-9);
    }

    @Test
    void missingDocsAndStyleApplyQualityMultiplier() {
        TaskExecutionResult r = new TaskExecutionResult(
            "t1", "u1", true, false, false,
            new TestReport(10, 0, 0),
            Optional.of(LocalDate.of(2024, 1, 5)));
        assertEquals(6.0, grader.grade(r).score(), 1e-9);
    }

    @Test
    void halfTestsPassingHalvesScore() {
        TaskExecutionResult r = new TaskExecutionResult(
            "t1", "u1", true, true, true,
            new TestReport(5, 5, 0),
            Optional.of(LocalDate.of(2024, 1, 5)));
        assertEquals(5.0, grader.grade(r).score(), 1e-9);
    }

    @Test
    void skippedTestsDontCountAgainstPassRatio() {
        TaskExecutionResult r = new TaskExecutionResult(
            "t1", "u1", true, true, true,
            new TestReport(5, 0, 5),
            Optional.of(LocalDate.of(2024, 1, 5)));
        assertEquals(10.0, grader.grade(r).score(), 1e-9);
    }

    @Test
    void lateSubmissionAppliesPenalty() {
        TaskExecutionResult r = new TaskExecutionResult(
            "t1", "u1", true, true, true,
            new TestReport(10, 0, 0),
            Optional.of(LocalDate.of(2024, 1, 12)));
        assertEquals(5.0, grader.grade(r).score(), 1e-9);
    }

    @Test
    void afterHardDeadlineGivesZero() {
        TaskExecutionResult r = new TaskExecutionResult(
            "t1", "u1", true, true, true,
            new TestReport(10, 0, 0),
            Optional.of(LocalDate.of(2024, 1, 25)));
        assertEquals(0.0, grader.grade(r).score(), 1e-9);
    }

    @Test
    void extraPointsAddOnTop() {
        config.settings().addExtraPoints("t1", "u1", 3);
        TaskExecutionResult r = new TaskExecutionResult(
            "t1", "u1", true, true, true,
            new TestReport(10, 0, 0),
            Optional.of(LocalDate.of(2024, 1, 5)));
        assertEquals(13.0, grader.grade(r).score(), 1e-9);
    }

    @Test
    void extraPointsKeptEvenIfBuildFailed() {
        config.settings().addExtraPoints("t1", "u1", 2);
        TaskExecutionResult r = TaskExecutionResult.notSubmitted("t1", "u1");
        assertEquals(2.0, grader.grade(r).score(), 1e-9);
    }

    @Test
    void activityWeightScalesTotal() {
        List<TaskExecutionResult> results = List.of(
            new TaskExecutionResult("t1", "u1", true, true, true,
                new TestReport(1, 0, 0), Optional.of(LocalDate.of(2024, 1, 5))),
            new TaskExecutionResult("t2", "u1", true, true, true,
                new TestReport(1, 0, 0), Optional.of(LocalDate.of(2024, 2, 5)))
        );
        config.settings().setActivityWeight(0.2);
        StudentActivity half = new StudentActivity("u1", 5, 10);
        assertEquals(27.0, grader.totalWithActivity(results, half), 1e-9);

        StudentActivity full = new StudentActivity("u1", 10, 10);
        assertEquals(30.0, grader.totalWithActivity(results, full), 1e-9);
    }

    @Test
    void checkpointIgnoresTasksAfterCheckpoint() {
        List<TaskExecutionResult> results = List.of(
            new TaskExecutionResult("t1", "u1", true, true, true,
                new TestReport(1, 0, 0), Optional.of(LocalDate.of(2024, 1, 5))),
            new TaskExecutionResult("t2", "u1", true, true, true,
                new TestReport(1, 0, 0), Optional.of(LocalDate.of(2024, 2, 5)))
        );
        Checkpoint cp1 = new Checkpoint("КТ1", LocalDate.of(2024, 2, 1));
        assertEquals(10.0, grader.checkpointScore(results, cp1), 1e-9);

        Checkpoint cp2 = new Checkpoint("КТ2", LocalDate.of(2024, 3, 1));
        assertEquals(30.0, grader.checkpointScore(results, cp2), 1e-9);
    }

    @Test
    void gradeForUsesSettingsScale() {
        config.settings().addGradeThreshold(85, "отлично");
        config.settings().addGradeThreshold(70, "хорошо");
        config.settings().addGradeThreshold(50, "удовл.");

        assertEquals("отлично", grader.gradeFor(90, 100));
        assertEquals("хорошо", grader.gradeFor(75, 100));
        assertEquals("удовл.", grader.gradeFor(60, 100));
        assertEquals("неудовлетворительно", grader.gradeFor(30, 100));
    }
}
