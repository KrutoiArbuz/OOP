package ru.nsu.masolygin.oopchecker.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.nsu.masolygin.oopchecker.domain.Assignment;
import ru.nsu.masolygin.oopchecker.domain.CourseConfig;
import ru.nsu.masolygin.oopchecker.dsl.DslParser;
import ru.nsu.masolygin.oopchecker.grader.Grader;
import ru.nsu.masolygin.oopchecker.grader.GradingResult;
import ru.nsu.masolygin.oopchecker.grader.StudentActivity;
import ru.nsu.masolygin.oopchecker.report.ConsoleReport;
import ru.nsu.masolygin.oopchecker.report.HtmlReport;
import ru.nsu.masolygin.oopchecker.runner.BuildTestRunner;
import ru.nsu.masolygin.oopchecker.runner.ProcessRunner;
import ru.nsu.masolygin.oopchecker.runner.TaskExecutionResult;
import ru.nsu.masolygin.oopchecker.runner.TestReport;
import ru.nsu.masolygin.oopchecker.vcs.GitClient;

class PipelineIntegrationTest {

    private static final java.nio.file.Path FIXTURES =
        Paths.get("src/test/resources/fixtures");

    @Test
    void dslParserProducesCompleteConfig() throws Exception {
        CourseConfig config = new DslParser().parseFile(FIXTURES.resolve("oopchecker.groovy"));

        assertEquals(2, config.tasks().size());
        assertEquals(1, config.groups().size());
        assertEquals(2, config.groups().get(0).students().size());
        assertEquals(3, config.assignments().size());
        assertEquals(2, config.checkpoints().size());
        assertEquals(0.5, config.settings().getLatePenalty(), 1e-9);
        assertEquals(60L, config.settings().getTestTimeoutSeconds());
    }

    @Test
    void graderScoringEndToEnd() throws Exception {
        CourseConfig config = new DslParser().parseFile(FIXTURES.resolve("oopchecker.groovy"));
        Grader grader = new Grader(config);

        TaskExecutionResult ivanovOk = new TaskExecutionResult(
            "2_1_1", "ivanov",
            true, true, true,
            new TestReport(5, 0, 0),
            Optional.of(LocalDate.of(2023, 9, 25))
        );
        GradingResult gr = grader.grade(ivanovOk);
        assertEquals(10.0, gr.score(), 1e-9);

        TaskExecutionResult petrovFail = TaskExecutionResult.notSubmitted("2_1_1", "petrov");
        assertEquals(0.0, grader.grade(petrovFail).score(), 1e-9);

        TaskExecutionResult ivanovLate = new TaskExecutionResult(
            "2_3_1", "ivanov",
            true, true, true,
            new TestReport(3, 0, 0),
            Optional.of(LocalDate.of(2023, 11, 7))
        );
        GradingResult lateGr = grader.grade(ivanovLate);
        assertEquals(12.0, lateGr.score(), 1e-9);
        assertEquals(2, lateGr.extraPoints());
        assertTrue(lateGr.latePenaltyApplied() > 0);
    }

    @Test
    void checkpointScoreFiltersCorrectly() throws Exception {
        CourseConfig config = new DslParser().parseFile(FIXTURES.resolve("oopchecker.groovy"));
        Grader grader = new Grader(config);

        TaskExecutionResult r1 = new TaskExecutionResult(
            "2_1_1", "ivanov", true, true, true,
            new TestReport(5, 0, 0), Optional.of(LocalDate.of(2023, 9, 20))
        );
        TaskExecutionResult r2 = new TaskExecutionResult(
            "2_3_1", "ivanov", true, true, true,
            new TestReport(5, 0, 0), Optional.of(LocalDate.of(2023, 10, 20))
        );

        double cpScore = grader.checkpointScore(List.of(r1, r2), config.checkpoints().get(0));
        assertEquals(10.0, cpScore, 1e-9);

        double finalScore = grader.checkpointScore(List.of(r1, r2), config.checkpoints().get(1));
        assertEquals(32.0, finalScore, 1e-9);
    }

    @Test
    void consoleReportContainsKeyElements() throws Exception {
        CourseConfig config = new DslParser().parseFile(FIXTURES.resolve("oopchecker.groovy"));
        Grader grader = new Grader(config);

        List<TaskExecutionResult> results = List.of(
            new TaskExecutionResult("2_1_1", "ivanov", true, true, true,
                new TestReport(5, 0, 0), Optional.of(LocalDate.of(2023, 9, 20))),
            TaskExecutionResult.notSubmitted("2_1_1", "petrov"),
            TaskExecutionResult.notSubmitted("2_3_1", "ivanov")
        );
        Map<String, StudentActivity> activity = Map.of(
            "ivanov", new StudentActivity("ivanov", 8, 17),
            "petrov", new StudentActivity("petrov", 3, 17)
        );

        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        new ConsoleReport(config, grader).print(results, activity,
            new PrintStream(buf, true, StandardCharsets.UTF_8));
        String out = buf.toString(StandardCharsets.UTF_8);

        assertTrue(out.contains("Иванов И.И."));
        assertTrue(out.contains("Петров П.П."));
        assertTrue(out.contains("2_1_1"));
        assertTrue(out.contains("КТ1"));
        assertTrue(out.contains("КТ2"));
        assertTrue(out.contains("отлично") || out.contains("хорошо")
            || out.contains("удовлетворительно") || out.contains("неудовлетворительно"));
    }

    @Test
    void htmlReportProducesWellFormedMarkup() throws Exception {
        CourseConfig config = new DslParser().parseFile(FIXTURES.resolve("oopchecker.groovy"));
        Grader grader = new Grader(config);

        List<TaskExecutionResult> results = List.of(
            new TaskExecutionResult("2_1_1", "ivanov", true, false, true,
                new TestReport(4, 1, 0), Optional.of(LocalDate.of(2023, 10, 5))),
            new TaskExecutionResult("2_1_1", "petrov", true, true, true,
                new TestReport(5, 0, 0), Optional.of(LocalDate.of(2023, 9, 28)))
        );
        Map<String, StudentActivity> activity = Map.of(
            "ivanov", new StudentActivity("ivanov", 10, 17),
            "petrov", new StudentActivity("petrov", 17, 17)
        );

        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        new HtmlReport(config, grader).print(results, activity,
            new PrintStream(buf, true, StandardCharsets.UTF_8));
        String html = buf.toString(StandardCharsets.UTF_8);

        assertTrue(html.startsWith("<!doctype html>"));
        assertTrue(html.contains("<meta charset=\"utf-8\">"));
        assertTrue(html.contains("</html>"));
        assertTrue(html.contains("<table>"));
        assertTrue(html.contains("</table>"));
        assertTrue(html.contains("Иванов И.И."));
        assertTrue(html.contains("Петров П.П."));
        assertTrue(html.contains("КТ1"));
        assertTrue(html.contains("КТ2"));
        assertFalse(html.contains("<td>") && !html.contains("</td>"));
    }

    @Test
    void gradeScaleAppliesCorrectly() throws Exception {
        CourseConfig config = new DslParser().parseFile(FIXTURES.resolve("oopchecker.groovy"));
        Grader grader = new Grader(config);

        assertEquals("отлично", grader.gradeFor(90, 100));
        assertEquals("хорошо", grader.gradeFor(72, 100));
        assertEquals("удовлетворительно", grader.gradeFor(55, 100));
        assertEquals("неудовлетворительно", grader.gradeFor(40, 100));
    }

    @Test
    void buildRunnerReturnsNotSubmittedForMissingDirectory(@TempDir java.nio.file.Path tmp)
        throws Exception {
        CourseConfig config = new DslParser().parseFile(FIXTURES.resolve("oopchecker.groovy"));
        BuildTestRunner runner = new BuildTestRunner(new ProcessRunner(), new GitClient());

        Assignment a = new Assignment("2_1_1", "ivanov");
        TaskExecutionResult result = runner.run(tmp, a, config);

        assertFalse(result.compileOk());
        assertFalse(result.docsOk());
        assertFalse(result.styleOk());
        assertEquals(0, result.tests().passed());
        assertTrue(result.submissionDate().isEmpty());
    }

    @Test
    void activityWeightAffectsFinalScore() throws Exception {
        CourseConfig config = new DslParser().parseFile(FIXTURES.resolve("oopchecker.groovy"));
        Grader grader = new Grader(config);

        TaskExecutionResult r = new TaskExecutionResult(
            "2_1_1", "ivanov", true, true, true,
            new TestReport(5, 0, 0), Optional.of(LocalDate.of(2023, 9, 20))
        );
        double total = grader.totalScore(List.of(r));
        assertEquals(10.0, total, 1e-9);

        StudentActivity zero = new StudentActivity("ivanov", 0, 17);
        assertEquals(8.0, grader.totalWithActivity(List.of(r), zero), 1e-9);

        StudentActivity full = new StudentActivity("ivanov", 17, 17);
        assertEquals(10.0, grader.totalWithActivity(List.of(r), full), 1e-9);
    }
}
