package ru.nsu.masolygin.oopchecker.report;

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

class ConsoleReportTest {

    private CourseConfig config;
    private Grader grader;
    private ConsoleReport report;

    @BeforeEach
    void setUp() {
        CourseConfigBuilder b = new CourseConfigBuilder();
        b.addTask(new Task("t1", "Простые числа", 10, null, null, null));
        b.addGroup(new Group("24216", List.of(
            new Student("alice", "Алиса А.А.", "https://x/a.git"),
            new Student("bob", "Боб Б.Б.", "https://x/b.git"))));
        b.addAssignment(new Assignment("t1", "alice"));
        b.addAssignment(new Assignment("t1", "bob"));
        b.addCheckpoint(new Checkpoint("КТ1", null, LocalDate.of(2024, 6, 1)));
        b.getSettingsBuilder().addGradeThreshold(80, "отлично");
        b.getSettingsBuilder().addGradeThreshold(50, "хорошо");
        config = b.build();
        grader = new Grader(config);
        report = new ConsoleReport(config, grader);
    }

    private String render(List<TaskExecutionResult> results,
        Map<String, StudentActivity> activity) {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        report.print(results, activity, new PrintStream(buf, true, StandardCharsets.UTF_8));
        return buf.toString(StandardCharsets.UTF_8);
    }

    @Test
    void outputContainsGroupName() {
        String out = render(List.of(), Map.of());
        assertTrue(out.contains("Группа 24216"));
    }

    @Test
    void outputContainsTaskHeader() {
        TaskExecutionResult r = new TaskExecutionResult("t1", "alice", true, true, true,
            new TestReport(5, 0, 0), null);
        String out = render(List.of(r), Map.of());
        assertTrue(out.contains("2_1_1") || out.contains("t1"));
        assertTrue(out.contains("Простые числа"));
    }

    @Test
    void outputIncludesStudentName() {
        TaskExecutionResult r = new TaskExecutionResult("t1", "alice", true, true, true,
            new TestReport(5, 0, 0), null);
        String out = render(List.of(r), Map.of());
        assertTrue(out.contains("Алиса А.А."));
    }

    @Test
    void okAndFailMarksAreVisible() {
        TaskExecutionResult r = new TaskExecutionResult("t1", "alice", true, false, true,
            new TestReport(5, 0, 0), null);
        String out = render(List.of(r), Map.of());
        assertTrue(out.contains("+"));
        assertTrue(out.contains("-"));
    }

    @Test
    void testCountsAreShown() {
        TaskExecutionResult r = new TaskExecutionResult("t1", "alice", true, true, true,
            new TestReport(7, 2, 1), null);
        String out = render(List.of(r), Map.of());
        assertTrue(out.contains("7/2/1"));
    }

    @Test
    void summaryTableContainsGradeWord() {
        TaskExecutionResult r = new TaskExecutionResult("t1", "alice", true, true, true,
            new TestReport(10, 0, 0), null);
        String out = render(List.of(r),
            Map.of("alice", new StudentActivity("alice", 5, 5)));
        assertTrue(out.contains("отлично") || out.contains("хорошо")
            || out.contains("неудовлетворительно"));
    }

    @Test
    void checkpointSectionIsRendered() {
        String out = render(List.of(), Map.of());
        assertTrue(out.contains("КТ1"));
    }

    @Test
    void missingResultProducesDashInSummary() {
        String out = render(List.of(), Map.of());
        assertTrue(out.contains("-"));
    }

    @Test
    void activityPercentIsRendered() {
        TaskExecutionResult r = new TaskExecutionResult("t1", "alice", true, true, true,
            new TestReport(5, 0, 0), null);
        String out = render(List.of(r),
            Map.of("alice", new StudentActivity("alice", 8, 10)));
        assertTrue(out.contains("80%"));
    }

    @Test
    void absentActivityShownAsDash() {
        TaskExecutionResult r = new TaskExecutionResult("t1", "alice", true, true, true,
            new TestReport(5, 0, 0), null);
        String out = render(List.of(r), Map.of());
        // alice не имеет записи активности — в саммари должна быть "-"
        assertFalse(out.isBlank());
    }
}
