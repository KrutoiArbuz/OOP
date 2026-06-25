package ru.nsu.masolygin.oopchecker.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.oopchecker.domain.courseconfig.CourseConfig;
import ru.nsu.masolygin.oopchecker.dsl.DslParser;
import ru.nsu.masolygin.oopchecker.grader.Grader;
import ru.nsu.masolygin.oopchecker.grader.GradingResult;
import ru.nsu.masolygin.oopchecker.grader.StudentActivity;
import ru.nsu.masolygin.oopchecker.report.ConsoleReport;
import ru.nsu.masolygin.oopchecker.report.HtmlReport;
import ru.nsu.masolygin.oopchecker.runner.TaskExecutionResult;
import ru.nsu.masolygin.oopchecker.runner.TestReport;

class PipelineIntegrationTest {

    private static final Path FIXTURES = Paths.get("src/test/resources/fixtures");

    private CourseConfig config;
    private Grader grader;

    @BeforeEach
    void setUp() throws Exception {
        config = new DslParser().parseFile(FIXTURES.resolve("oopchecker.groovy"));
        grader = new Grader(config);
    }

    private TaskExecutionResult ok(String task, String github, int passed, LocalDate when) {
        return new TaskExecutionResult(task, github, true, true, true,
            new TestReport(passed, 0, 0), when);
    }

    @Test
    void dslProducesCompleteCourseConfig() {
        assertEquals(2, config.tasks().size());
        assertEquals(1, config.groups().size());
        assertEquals(2, config.groups().get(0).students().size());
        assertEquals(3, config.assignments().size());
        assertEquals(2, config.checkpoints().size());
        assertEquals(0.5, config.settings().latePenalty(), 1e-9);
    }

    @Test
    void onTimeFullScoreEqualsMaxPoints() {
        TaskExecutionResult r = ok("2_1_1", "ivanov", 5, LocalDate.of(2023, 9, 25));
        assertEquals(10.0, grader.grade(r).score(), 1e-9);
    }

    @Test
    void notSubmittedTaskGivesZero() {
        TaskExecutionResult r = TaskExecutionResult.notSubmitted("2_1_1", "petrov");
        assertEquals(0.0, grader.grade(r).score(), 1e-9);
    }

    @Test
    void lateSubmissionWithExtraPointsAppliesPenaltyAndExtra() {
        TaskExecutionResult r = ok("2_3_1", "ivanov", 3, LocalDate.of(2023, 11, 7));
        GradingResult g = grader.grade(r);
        assertEquals(2, g.extraPoints());
        assertTrue(g.latePenaltyPoints() > 0);
        assertEquals(12.0, g.score(), 1e-9);
    }

    @Test
    void firstCheckpointIncludesOnlyFirstTask() {
        List<TaskExecutionResult> results = List.of(
            ok("2_1_1", "ivanov", 5, LocalDate.of(2023, 9, 20)),
            ok("2_3_1", "ivanov", 5, LocalDate.of(2023, 10, 20))
        );
        assertEquals(10.0, grader.checkpointScore(results, config.checkpoints().get(0)), 1e-9);
    }

    @Test
    void finalCheckpointIncludesAllTasksWithExtraPoints() {
        List<TaskExecutionResult> results = List.of(
            ok("2_1_1", "ivanov", 5, LocalDate.of(2023, 9, 20)),
            ok("2_3_1", "ivanov", 5, LocalDate.of(2023, 10, 20))
        );
        assertEquals(32.0, grader.checkpointScore(results, config.checkpoints().get(1)), 1e-9);
    }

    @Test
    void consoleReportRendersAllStudentsAndCheckpoints() {
        List<TaskExecutionResult> results = List.of(
            ok("2_1_1", "ivanov", 5, LocalDate.of(2023, 9, 20)),
            TaskExecutionResult.notSubmitted("2_1_1", "petrov"),
            TaskExecutionResult.notSubmitted("2_3_1", "ivanov")
        );
        Map<String, StudentActivity> activity = Map.of(
            "ivanov", new StudentActivity("ivanov", 8, 17),
            "petrov", new StudentActivity("petrov", 3, 17)
        );
        String out = renderConsole(results, activity);

        assertTrue(out.contains("Иванов И.И."));
        assertTrue(out.contains("Петров П.П."));
        assertTrue(out.contains("2_1_1"));
        assertTrue(out.contains("КТ1"));
        assertTrue(out.contains("КТ2"));
    }

    @Test
    void htmlReportContainsExpectedStructure() {
        List<TaskExecutionResult> results = List.of(
            new TaskExecutionResult("2_1_1", "ivanov", true, false, true,
                new TestReport(4, 1, 0), LocalDate.of(2023, 10, 5)),
            ok("2_1_1", "petrov", 5, LocalDate.of(2023, 9, 28))
        );
        Map<String, StudentActivity> activity = Map.of(
            "ivanov", new StudentActivity("ivanov", 10, 17),
            "petrov", new StudentActivity("petrov", 17, 17)
        );
        String html = renderHtml(results, activity);

        assertTrue(html.startsWith("<!doctype html>"));
        assertTrue(html.contains("<meta charset=\"utf-8\">"));
        assertTrue(html.contains("</html>"));
        assertTrue(html.contains("<table>"));
        assertTrue(html.contains("Иванов И.И."));
        assertTrue(html.contains("КТ1"));
    }

    @Test
    void activityWeightAffectsTotalScore() {
        List<TaskExecutionResult> results = List.of(
            ok("2_1_1", "ivanov", 5, LocalDate.of(2023, 9, 20))
        );
        assertEquals(10.0, grader.totalScore(results), 1e-9);

        StudentActivity zero = new StudentActivity("ivanov", 0, 17);
        assertEquals(8.0, grader.totalWithActivity(results, zero), 1e-9);

        StudentActivity full = new StudentActivity("ivanov", 17, 17);
        assertEquals(10.0, grader.totalWithActivity(results, full), 1e-9);
    }

    @Test
    void importedConfigCombinesWithMainConfig() throws Exception {
        CourseConfig imported = new DslParser().parseFile(FIXTURES.resolve("semester.groovy"));
        assertTrue(imported.findTask("shared_task").isPresent());
        assertTrue(imported.findStudent("sidorov").isPresent());
    }

    @Test
    void htmlOutputHasMatchingTdTags() {
        List<TaskExecutionResult> results = List.of(
            ok("2_1_1", "ivanov", 5, LocalDate.of(2023, 9, 20)));
        String html = renderHtml(results, Map.of());
        long open = countOf(html, "<td>");
        long close = countOf(html, "</td>");
        assertEquals(open, close);
        assertFalse(html.contains("<td>") && !html.contains("</td>"));
    }

    private String renderConsole(List<TaskExecutionResult> results,
        Map<String, StudentActivity> activity) {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        new ConsoleReport(config, grader)
            .print(results, activity, new PrintStream(buf, true, StandardCharsets.UTF_8));
        return buf.toString(StandardCharsets.UTF_8);
    }

    private String renderHtml(List<TaskExecutionResult> results,
        Map<String, StudentActivity> activity) {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        new HtmlReport(config, grader)
            .print(results, activity, new PrintStream(buf, true, StandardCharsets.UTF_8));
        return buf.toString(StandardCharsets.UTF_8);
    }

    private long countOf(String haystack, String needle) {
        long count = 0;
        int idx = 0;
        while ((idx = haystack.indexOf(needle, idx)) != -1) {
            count++;
            idx += needle.length();
        }
        return count;
    }
}
