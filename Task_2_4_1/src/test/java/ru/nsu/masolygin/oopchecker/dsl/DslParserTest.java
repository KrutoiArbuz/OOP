package ru.nsu.masolygin.oopchecker.dsl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.oopchecker.domain.Assignment;
import ru.nsu.masolygin.oopchecker.domain.Group;
import ru.nsu.masolygin.oopchecker.domain.Student;
import ru.nsu.masolygin.oopchecker.domain.Task;
import ru.nsu.masolygin.oopchecker.domain.courseconfig.CourseConfig;

class DslParserTest {

    private static final Path FIXTURES = Paths.get("src/test/resources/fixtures");

    private CourseConfig parse(String src) {
        return new DslParser().parseSource(src, "test.groovy", FIXTURES);
    }

    @Test
    void parsesFullExampleFixture() throws Exception {
        CourseConfig config = new DslParser().parseFile(FIXTURES.resolve("oopchecker.groovy"));

        assertEquals(2, config.tasks().size());
        Task t = config.findTask("2_1_1").orElseThrow();
        assertEquals("Простые числа", t.name());
        assertEquals(10, t.maxPoints());
        assertEquals(LocalDate.of(2023, 10, 1), t.softDeadline());
        assertEquals(LocalDate.of(2023, 10, 15), t.hardDeadline());
    }

    @Test
    void parsesGroupsAndStudents() throws Exception {
        CourseConfig config = new DslParser().parseFile(FIXTURES.resolve("oopchecker.groovy"));
        Group g = config.groups().get(0);
        assertEquals("12345", g.name());
        assertEquals(2, g.students().size());

        Student ivanov = config.findStudent("ivanov").orElseThrow();
        assertEquals("Иванов И.И.", ivanov.fullName());
        assertTrue(ivanov.repoUrl().endsWith("/ivanov/oop.git"));
    }

    @Test
    void parsesAssignmentsAndCheckpoints() throws Exception {
        CourseConfig config = new DslParser().parseFile(FIXTURES.resolve("oopchecker.groovy"));
        assertEquals(3, config.assignments().size());
        assertTrue(config.assignments().contains(new Assignment("2_1_1", "ivanov")));
        assertEquals(2, config.checkpoints().size());
        assertEquals("КТ1", config.checkpoints().get(0).name());
    }

    @Test
    void parsesSettings() throws Exception {
        CourseConfig config = new DslParser().parseFile(FIXTURES.resolve("oopchecker.groovy"));
        assertEquals(0.5, config.settings().latePenalty(), 1e-9);
        assertEquals(60L, config.settings().testTimeoutSeconds());
        assertEquals(0.2, config.settings().activityWeight(), 1e-9);
        assertEquals(2, config.settings().getExtraPoints("2_3_1", "ivanov"));
        assertEquals(0, config.settings().getExtraPoints("2_1_1", "ivanov"));
    }

    @Test
    void gradeScaleAppliesCorrectThresholds() throws Exception {
        CourseConfig config = new DslParser().parseFile(FIXTURES.resolve("oopchecker.groovy"));
        assertEquals("отлично", config.settings().gradeFor(90));
        assertEquals("хорошо", config.settings().gradeFor(75));
        assertEquals("неудовлетворительно", config.settings().gradeFor(30));
    }

    @Test
    void importConfigMergesAcrossFiles() throws Exception {
        CourseConfig config = new DslParser().parseFile(FIXTURES.resolve("semester.groovy"));
        assertNotNull(config.findTask("shared_task").orElse(null));
        assertNotNull(config.findStudent("sidorov").orElse(null));
    }

    @Test
    void emptyCourseBlockYieldsEmptyConfig() {
        CourseConfig config = parse("course { }");
        assertEquals(0, config.tasks().size());
        assertEquals(0, config.groups().size());
    }

    @Test
    void namedArgumentsForCheckByStudent() {
        CourseConfig config = parse("""
            course {
                assignments { check task: 'x', student: 'y' }
            }
            """);
        assertEquals(new Assignment("x", "y"), config.assignments().get(0));
    }

    @Test
    void checkByGroupExpandsToAllStudents() {
        CourseConfig config = parse("""
            course {
                groups {
                    group('g') {
                        student github: 's1', name: 'S 1', repo: 'https://x/1.git'
                        student github: 's2', name: 'S 2', repo: 'https://x/2.git'
                    }
                }
                assignments { check task: 't', group: 'g' }
            }
            """);
        assertEquals(2, config.assignments().size());
        assertTrue(config.assignments().contains(new Assignment("t", "s1")));
        assertTrue(config.assignments().contains(new Assignment("t", "s2")));
    }

    @Test
    void semesterBlockIsParsed() {
        CourseConfig config = parse("""
            course {
                settings {
                    semester(1) {
                        startDate '2025-09-01'
                        weeks 17
                    }
                }
            }
            """);
        assertEquals(LocalDate.of(2025, 9, 1),
            config.settings().semesters().get(1).startDate());
        assertEquals(17, config.settings().semesters().get(1).weeks());
    }

    @Test
    void checkpointWithStartDateIsParsed() {
        CourseConfig config = parse("""
            course {
                checkpoints {
                    checkpoint name: 'КТ1', startDate: '2025-09-01', date: '2025-12-27'
                }
            }
            """);
        assertEquals(LocalDate.of(2025, 9, 1),
            config.checkpoints().get(0).startDate().orElseThrow());
        assertEquals(LocalDate.of(2025, 12, 27),
            config.checkpoints().get(0).date());
    }

    @Test
    void taskWithCustomLabPathIsParsed() {
        CourseConfig config = parse("""
            course {
                tasks {
                    task {
                        id 't1'
                        name 'Name'
                        maxPoints 5
                        labPath 'labs/Task_t1'
                        softDeadline '2025-10-01'
                        hardDeadline '2025-10-15'
                    }
                }
            }
            """);
        assertEquals("labs/Task_t1", config.findTask("t1").orElseThrow().labPath());
    }
}
