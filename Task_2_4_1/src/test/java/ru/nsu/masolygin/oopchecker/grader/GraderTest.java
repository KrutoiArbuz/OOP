package ru.nsu.masolygin.oopchecker.grader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.oopchecker.domain.Checkpoint;
import ru.nsu.masolygin.oopchecker.domain.Task;
import ru.nsu.masolygin.oopchecker.domain.courseconfig.CourseConfigBuilder;
import ru.nsu.masolygin.oopchecker.runner.TaskExecutionResult;
import ru.nsu.masolygin.oopchecker.runner.TestReport;

class GraderTest {

    private static final LocalDate T1_SOFT = LocalDate.of(2024, 1, 10);
    private static final LocalDate T1_HARD = LocalDate.of(2024, 1, 20);
    private static final LocalDate T2_SOFT = LocalDate.of(2024, 2, 10);
    private static final LocalDate T2_HARD = LocalDate.of(2024, 2, 20);

    private CourseConfigBuilder cfgBuilder;
    private Grader grader;

    @BeforeEach
    void setUp() {
        cfgBuilder = new CourseConfigBuilder();
        cfgBuilder.addTask(new Task("t1", "Задача 1", 10, T1_SOFT, T1_HARD, null));
        cfgBuilder.addTask(new Task("t2", "Задача 2", 20, T2_SOFT, T2_HARD, null));
        cfgBuilder.getSettingsBuilder().setLatePenalty(0.5);
        grader = new Grader(cfgBuilder.build());
    }

    private TaskExecutionResult result(String task, boolean compile, boolean docs, boolean style,
        TestReport tests, LocalDate date) {
        return new TaskExecutionResult(task, "u1", compile, docs, style, tests, date);
    }

    @Test
    void buildFailedGivesZeroScore() {
        TaskExecutionResult r = TaskExecutionResult.notSubmitted("t1", "u1");
        assertEquals(0.0, grader.grade(r).score(), 1e-9);
    }

    @Test
    void buildFailedKeepsExtraPoints() {
        cfgBuilder.getSettingsBuilder().addExtraPoints("t1", "u1", 2);
        grader = new Grader(cfgBuilder.build());
        TaskExecutionResult r = TaskExecutionResult.notSubmitted("t1", "u1");
        assertEquals(2.0, grader.grade(r).score(), 1e-9);
    }

    @Test
    void greenAndOnTimeGivesMaxPoints() {
        TaskExecutionResult r = result("t1", true, true, true,
            new TestReport(10, 0, 0), LocalDate.of(2024, 1, 5));
        assertEquals(10.0, grader.grade(r).score(), 1e-9);
    }

    @Test
    void missingDocsAppliesPenalty() {
        TaskExecutionResult r = result("t1", true, false, true,
            new TestReport(10, 0, 0), LocalDate.of(2024, 1, 5));
        assertEquals(8.0, grader.grade(r).score(), 1e-9);
    }

    @Test
    void missingDocsAndStylePenaltiesStack() {
        TaskExecutionResult r = result("t1", true, false, false,
            new TestReport(10, 0, 0), LocalDate.of(2024, 1, 5));
        assertEquals(6.0, grader.grade(r).score(), 1e-9);
    }

    @Test
    void halfTestsPassingHalvesScore() {
        TaskExecutionResult r = result("t1", true, true, true,
            new TestReport(5, 5, 0), LocalDate.of(2024, 1, 5));
        assertEquals(5.0, grader.grade(r).score(), 1e-9);
    }

    @Test
    void skippedTestsDontCountAgainstPassRatio() {
        TaskExecutionResult r = result("t1", true, true, true,
            new TestReport(5, 0, 5), LocalDate.of(2024, 1, 5));
        assertEquals(10.0, grader.grade(r).score(), 1e-9);
    }

    @Test
    void zeroTestsExecutedGivesZeroScore() {
        TaskExecutionResult r = result("t1", true, true, true,
            new TestReport(0, 0, 0), LocalDate.of(2024, 1, 5));
        assertEquals(0.0, grader.grade(r).score(), 1e-9);
    }

    @Test
    void lateAfterSoftDeadlineAppliesPenalty() {
        TaskExecutionResult r = result("t1", true, true, true,
            new TestReport(10, 0, 0), LocalDate.of(2024, 1, 12));
        assertEquals(5.0, grader.grade(r).score(), 1e-9);
    }

    @Test
    void afterHardDeadlineGivesZeroScore() {
        TaskExecutionResult r = result("t1", true, true, true,
            new TestReport(10, 0, 0), LocalDate.of(2024, 1, 25));
        assertEquals(0.0, grader.grade(r).score(), 1e-9);
    }

    @Test
    void afterHardDeadlineKeepsExtraPoints() {
        cfgBuilder.getSettingsBuilder().addExtraPoints("t1", "u1", 3);
        grader = new Grader(cfgBuilder.build());
        TaskExecutionResult r = result("t1", true, true, true,
            new TestReport(10, 0, 0), LocalDate.of(2024, 1, 25));
        assertEquals(3.0, grader.grade(r).score(), 1e-9);
    }

    @Test
    void extraPointsAddOnTopOfBase() {
        cfgBuilder.getSettingsBuilder().addExtraPoints("t1", "u1", 3);
        grader = new Grader(cfgBuilder.build());
        TaskExecutionResult r = result("t1", true, true, true,
            new TestReport(10, 0, 0), LocalDate.of(2024, 1, 5));
        assertEquals(13.0, grader.grade(r).score(), 1e-9);
    }

    @Test
    void totalScoreSumsAllResults() {
        List<TaskExecutionResult> results = List.of(
            result("t1", true, true, true, new TestReport(10, 0, 0),
                LocalDate.of(2024, 1, 5)),
            result("t2", true, true, true, new TestReport(10, 0, 0),
                LocalDate.of(2024, 2, 5))
        );
        assertEquals(30.0, grader.totalScore(results), 1e-9);
    }

    @Test
    void totalWithActivityWeightZeroEqualsTotalScore() {
        cfgBuilder.getSettingsBuilder().setActivityWeight(0.0);
        grader = new Grader(cfgBuilder.build());
        List<TaskExecutionResult> results = List.of(result("t1", true, true, true,
            new TestReport(10, 0, 0), LocalDate.of(2024, 1, 5)));

        StudentActivity zero = new StudentActivity("u1", 0, 10);
        assertEquals(grader.totalScore(results),
            grader.totalWithActivity(results, zero), 1e-9);
    }

    @Test
    void totalWithFullActivityEqualsTotalScore() {
        cfgBuilder.getSettingsBuilder().setActivityWeight(0.2);
        grader = new Grader(cfgBuilder.build());
        List<TaskExecutionResult> results = List.of(result("t1", true, true, true,
            new TestReport(10, 0, 0), LocalDate.of(2024, 1, 5)));

        StudentActivity full = new StudentActivity("u1", 10, 10);
        assertEquals(10.0, grader.totalWithActivity(results, full), 1e-9);
    }

    @Test
    void totalWithZeroActivityScalesDown() {
        cfgBuilder.getSettingsBuilder().setActivityWeight(0.2);
        grader = new Grader(cfgBuilder.build());
        List<TaskExecutionResult> results = List.of(result("t1", true, true, true,
            new TestReport(10, 0, 0), LocalDate.of(2024, 1, 5)));

        StudentActivity zero = new StudentActivity("u1", 0, 10);
        assertEquals(8.0, grader.totalWithActivity(results, zero), 1e-9);
    }

    @Test
    void checkpointScoreIncludesOnlyTasksBeforeCheckpoint() {
        List<TaskExecutionResult> results = List.of(
            result("t1", true, true, true, new TestReport(10, 0, 0),
                LocalDate.of(2024, 1, 5)),
            result("t2", true, true, true, new TestReport(10, 0, 0),
                LocalDate.of(2024, 2, 5))
        );
        Checkpoint cp = new Checkpoint("КТ1", null, LocalDate.of(2024, 2, 1));
        assertEquals(10.0, grader.checkpointScore(results, cp), 1e-9);
    }

    @Test
    void checkpointWithStartDateExcludesTasksBeforeWindow() {
        List<TaskExecutionResult> results = List.of(
            result("t1", true, true, true, new TestReport(10, 0, 0),
                LocalDate.of(2024, 1, 5)),
            result("t2", true, true, true, new TestReport(10, 0, 0),
                LocalDate.of(2024, 2, 5))
        );
        Checkpoint cp = new Checkpoint("КТ2",
            LocalDate.of(2024, 2, 1), LocalDate.of(2024, 3, 1));
        assertEquals(20.0, grader.checkpointScore(results, cp), 1e-9);
    }

    @Test
    void checkpointWindowSumsOnlyTasksInside() {
        List<TaskExecutionResult> results = List.of(
            result("t1", true, true, true, new TestReport(10, 0, 0),
                LocalDate.of(2024, 1, 5)),
            result("t2", true, true, true, new TestReport(10, 0, 0),
                LocalDate.of(2024, 2, 5))
        );
        Checkpoint full = new Checkpoint("КТ", null, LocalDate.of(2024, 3, 1));
        assertEquals(30.0, grader.checkpointScore(results, full), 1e-9);
    }

    @Test
    void gradeForUsesSettingsScale() {
        cfgBuilder.getSettingsBuilder().addGradeThreshold(85, "отлично");
        cfgBuilder.getSettingsBuilder().addGradeThreshold(70, "хорошо");
        cfgBuilder.getSettingsBuilder().addGradeThreshold(50, "удовл.");
        grader = new Grader(cfgBuilder.build());

        assertEquals("отлично", grader.gradeFor(90.0));
        assertEquals("хорошо", grader.gradeFor(75.0));
        assertEquals("удовл.", grader.gradeFor(60.0));
        assertEquals("неудовлетворительно", grader.gradeFor(30.0));
    }

    @Test
    void latePenaltyAppliedReturnsPositiveValue() {
        TaskExecutionResult r = result("t1", true, true, true,
            new TestReport(10, 0, 0), LocalDate.of(2024, 1, 12));
        GradingResult g = grader.grade(r);
        assertTrue(g.latePenaltyPoints() > 0);
    }

    @Test
    void latePenaltyIsZeroWhenOnTime() {
        TaskExecutionResult r = result("t1", true, true, true,
            new TestReport(10, 0, 0), LocalDate.of(2024, 1, 5));
        assertEquals(0.0, grader.grade(r).latePenaltyPoints(), 1e-9);
    }

    @Test
    void noSubmissionDateMeansNoPenalty() {
        TaskExecutionResult r = new TaskExecutionResult("t1", "u1",
            true, true, true, new TestReport(10, 0, 0), null);
        assertEquals(10.0, grader.grade(r).score(), 1e-9);
    }
}
