package ru.nsu.masolygin.oopchecker.report;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.oopchecker.domain.Assignment;
import ru.nsu.masolygin.oopchecker.domain.Checkpoint;
import ru.nsu.masolygin.oopchecker.domain.Group;
import ru.nsu.masolygin.oopchecker.domain.Student;
import ru.nsu.masolygin.oopchecker.domain.Task;
import ru.nsu.masolygin.oopchecker.domain.courseconfig.CourseConfig;
import ru.nsu.masolygin.oopchecker.domain.courseconfig.CourseConfigBuilder;
import ru.nsu.masolygin.oopchecker.grader.Grader;
import ru.nsu.masolygin.oopchecker.grader.StudentActivity;
import ru.nsu.masolygin.oopchecker.runner.TaskExecutionResult;
import ru.nsu.masolygin.oopchecker.runner.TestReport;

class HtmlReportTest {

    private CourseConfig config;
    private HtmlReport report;

    @BeforeEach
    void setUp() {
        CourseConfigBuilder b = new CourseConfigBuilder();
        b.addTask(new Task("t1", "Простые<числа>", 10, null, null, null));
        b.addGroup(new Group("24216 & студенты", List.of(
            new Student("alice", "Алиса \"А.А.\"", "https://x/a.git"))));
        b.addAssignment(new Assignment("t1", "alice"));
        b.addCheckpoint(new Checkpoint("КТ1",
            LocalDate.of(2024, 1, 1), LocalDate.of(2024, 6, 1)));
        b.getSettingsBuilder().addGradeThreshold(80, "отлично");
        config = b.build();
        report = new HtmlReport(config, new Grader(config));
    }

    private String render(List<TaskExecutionResult> results,
        Map<String, StudentActivity> activity) {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        report.print(results, activity, new PrintStream(buf, true, StandardCharsets.UTF_8));
        return buf.toString(StandardCharsets.UTF_8);
    }

    @Test
    void documentStartsWithDoctype() {
        assertTrue(render(List.of(), Map.of()).startsWith("<!doctype html>"));
    }

    @Test
    void documentContainsRequiredTags() {
        String html = render(List.of(), Map.of());
        assertTrue(html.contains("<meta charset=\"utf-8\">"));
        assertTrue(html.contains("<style>"));
        assertTrue(html.contains("</body></html>"));
    }

    @Test
    void specialCharactersInTaskNameAreEscaped() {
        String html = render(List.of(), Map.of());
        assertTrue(html.contains("&lt;числа&gt;"));
        assertFalse(html.contains("Простые<числа>"));
    }

    @Test
    void ampersandInGroupNameIsEscaped() {
        String html = render(List.of(), Map.of());
        assertTrue(html.contains("24216 &amp; студенты"));
    }

    @Test
    void quoteInStudentNameIsEscaped() {
        TaskExecutionResult r = new TaskExecutionResult("t1", "alice", true, true, true,
            new TestReport(5, 0, 0), null);
        String html = render(List.of(r), Map.of());
        assertTrue(html.contains("&quot;А.А.&quot;"));
    }

    @Test
    void everyOpenTdHasMatchingClose() {
        TaskExecutionResult r = new TaskExecutionResult("t1", "alice", true, true, true,
            new TestReport(5, 0, 0), null);
        String html = render(List.of(r), Map.of());
        long opens = html.chars().filter(ch -> ch == '<').count();
        // примитивная проверка — в выводе должны быть как открывающие, так и закрывающие <td> тэги
        assertTrue(html.contains("<td>"));
        assertTrue(html.contains("</td>"));
        long openTd = countOccurrences(html, "<td>");
        long closeTd = countOccurrences(html, "</td>");
        assertEquals(openTd, closeTd);
    }

    @Test
    void okMarkIsRenderedAsGreenSpan() {
        TaskExecutionResult r = new TaskExecutionResult("t1", "alice", true, true, true,
            new TestReport(5, 0, 0), null);
        String html = render(List.of(r), Map.of());
        assertTrue(html.contains("<span class=\"ok\">+</span>"));
    }

    @Test
    void failMarkIsRenderedAsRedSpan() {
        TaskExecutionResult r = new TaskExecutionResult("t1", "alice", true, false, true,
            new TestReport(5, 0, 0), null);
        String html = render(List.of(r), Map.of());
        assertTrue(html.contains("<span class=\"bad\">-</span>"));
    }

    @Test
    void checkpointSectionRendersDateRange() {
        String html = render(List.of(), Map.of());
        assertTrue(html.contains("2024-01-01"));
        assertTrue(html.contains("2024-06-01"));
    }

    @Test
    void taskTableHasExpectedColumnHeaders() {
        TaskExecutionResult r = new TaskExecutionResult("t1", "alice", true, true, true,
            new TestReport(5, 0, 0), null);
        String html = render(List.of(r), Map.of());
        assertTrue(html.contains("<th>Студент</th>"));
        assertTrue(html.contains("<th>Сборка</th>"));
        assertTrue(html.contains("<th>Тесты</th>"));
    }

    private long countOccurrences(String haystack, String needle) {
        long count = 0;
        int idx = 0;
        while ((idx = haystack.indexOf(needle, idx)) != -1) {
            count++;
            idx += needle.length();
        }
        return count;
    }
}
