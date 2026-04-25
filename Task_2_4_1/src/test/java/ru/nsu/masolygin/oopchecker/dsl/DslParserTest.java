package ru.nsu.masolygin.oopchecker.dsl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.oopchecker.domain.Assignment;
import ru.nsu.masolygin.oopchecker.domain.CourseConfig;
import ru.nsu.masolygin.oopchecker.domain.Group;
import ru.nsu.masolygin.oopchecker.domain.Student;
import ru.nsu.masolygin.oopchecker.domain.Task;

class DslParserTest {

    private static final Path FIXTURES = Paths.get("src/test/resources/fixtures");

    @Test
    void parsesFullExample() throws Exception {
        CourseConfig config = new DslParser().parseFile(FIXTURES.resolve("oopchecker.groovy"));

        assertEquals(2, config.tasks().size());
        Task t = config.findTask("2_1_1").orElseThrow();
        assertEquals("Простые числа", t.name());
        assertEquals(10, t.maxPoints());
        assertEquals(LocalDate.of(2023, 10, 1), t.softDeadline());
        assertEquals(LocalDate.of(2023, 10, 15), t.hardDeadline());

        assertEquals(1, config.groups().size());
        Group g = config.groups().get(0);
        assertEquals("12345", g.name());
        assertEquals(2, g.students().size());
        Student ivanov = config.findStudent("ivanov").orElseThrow();
        assertEquals("Иванов И.И.", ivanov.fullName());
        assertTrue(ivanov.repoUrl().endsWith("/ivanov/oop.git"));

        assertEquals(3, config.assignments().size());
        assertTrue(config.assignments().contains(new Assignment("2_1_1", "ivanov")));
        assertTrue(config.assignments().contains(new Assignment("2_3_1", "ivanov")));

        assertEquals(2, config.checkpoints().size());
        assertEquals("КТ1", config.checkpoints().get(0).name());
        assertEquals(LocalDate.of(2023, 10, 31), config.checkpoints().get(0).date());

        assertEquals(0.5, config.settings().getLatePenalty(), 1e-9);
        assertEquals(60L, config.settings().getTestTimeoutSeconds());
        assertEquals(0.2, config.settings().getActivityWeight(), 1e-9);
        assertEquals(2, config.settings().getExtraPoints("2_3_1", "ivanov"));
        assertEquals(0, config.settings().getExtraPoints("2_1_1", "ivanov"));
        assertEquals("отлично", config.settings().gradeFor(90));
        assertEquals("хорошо", config.settings().gradeFor(75));
        assertEquals("неудовлетворительно", config.settings().gradeFor(30));
    }

    @Test
    void importConfigMergesIntoSameCourseConfig() throws Exception {
        CourseConfig config = new DslParser().parseFile(FIXTURES.resolve("semester.groovy"));

        assertNotNull(config.findTask("shared_task").orElse(null));
        assertNotNull(config.findStudent("sidorov").orElse(null));
    }

    @Test
    void emptyCourseBlockIsAccepted() {
        CourseConfig config = new DslParser().parseSource("course { }");
        assertEquals(0, config.tasks().size());
        assertEquals(0, config.groups().size());
    }

    @Test
    void namedArgumentsOnCheckWork() {
        String src = """
            course {
                assignments {
                    check task: 'x', student: 'y'
                }
            }
            """;
        CourseConfig config = new DslParser().parseSource(src);
        assertEquals(new Assignment("x", "y"), config.assignments().get(0));
    }

    @Test
    void checkByGroupExpandsToAllStudentsInGroup() {
        String src = """
            course {
                groups {
                    group('24216') {
                        student github: 's1', name: 'Student 1', repo: 'https://example.com/1.git'
                        student github: 's2', name: 'Student 2', repo: 'https://example.com/2.git'
                    }
                }
                assignments {
                    check task: '2_1_1', group: '24216'
                }
            }
            """;

        CourseConfig config = new DslParser().parseSource(src);

        assertEquals(2, config.assignments().size());
        assertTrue(config.assignments().contains(new Assignment("2_1_1", "s1")));
        assertTrue(config.assignments().contains(new Assignment("2_1_1", "s2")));
    }
}
