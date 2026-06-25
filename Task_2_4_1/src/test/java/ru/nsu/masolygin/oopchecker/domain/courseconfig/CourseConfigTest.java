package ru.nsu.masolygin.oopchecker.domain.courseconfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.oopchecker.domain.Assignment;
import ru.nsu.masolygin.oopchecker.domain.Checkpoint;
import ru.nsu.masolygin.oopchecker.domain.Group;
import ru.nsu.masolygin.oopchecker.domain.Student;
import ru.nsu.masolygin.oopchecker.domain.Task;

class CourseConfigTest {

    private static final Student ALICE = new Student("alice", "Алиса А.А.", "https://x/a.git");
    private static final Student BOB = new Student("bob", "Боб Б.Б.", "https://x/b.git");
    private static final Student CARL = new Student("carl", "Карл К.К.", "https://x/c.git");

    private CourseConfig config;

    @BeforeEach
    void setUp() {
        CourseConfigBuilder builder = new CourseConfigBuilder();
        builder.addTask(new Task("t1", "Задача 1", 10, null, null, null));
        builder.addTask(new Task("t2", "Задача 2", 20, null, null, null));
        builder.addGroup(new Group("24216", List.of(ALICE, BOB)));
        builder.addGroup(new Group("99999", List.of(CARL)));
        builder.addAssignment(new Assignment("t1", "alice"));
        builder.addAssignment(new Assignment("t1", "bob"));
        builder.addAssignment(new Assignment("t2", "alice"));
        builder.addCheckpoint(new Checkpoint("КТ1", null, LocalDate.of(2024, 1, 31)));
        config = builder.build();
    }

    @Test
    void findTaskReturnsExistingTask() {
        assertTrue(config.findTask("t1").isPresent());
        assertEquals("Задача 1", config.findTask("t1").orElseThrow().name());
    }

    @Test
    void findTaskReturnsEmptyForUnknownId() {
        assertFalse(config.findTask("unknown").isPresent());
    }

    @Test
    void findStudentSearchesAcrossAllGroups() {
        assertEquals("Алиса А.А.", config.findStudent("alice").orElseThrow().fullName());
        assertEquals("Карл К.К.", config.findStudent("carl").orElseThrow().fullName());
    }

    @Test
    void findStudentReturnsEmptyForUnknownGithub() {
        assertFalse(config.findStudent("nobody").isPresent());
    }

    @Test
    void tasksForGroupReturnsOnlyAssignedTasks() {
        Group g = config.groups().get(0);
        List<Task> tasks = config.tasksFor(g);
        assertEquals(2, tasks.size());
        assertTrue(tasks.stream().anyMatch(t -> t.id().equals("t1")));
        assertTrue(tasks.stream().anyMatch(t -> t.id().equals("t2")));
    }

    @Test
    void tasksForGroupDeduplicatesTasksAssignedToMultipleStudents() {
        Group g = config.groups().get(0);
        long t1count = config.tasksFor(g).stream().filter(t -> t.id().equals("t1")).count();
        assertEquals(1, t1count);
    }

    @Test
    void tasksForEmptyGroupReturnsEmpty() {
        Group emptyGroup = new Group("z", List.of());
        assertTrue(config.tasksFor(emptyGroup).isEmpty());
    }

    @Test
    void tasksForUnrelatedGroupReturnsEmpty() {
        Group g = config.groups().get(1);
        assertTrue(config.tasksFor(g).isEmpty());
    }

    @Test
    void tasksForRequiresNonNullGroup() {
        assertThrows(NullPointerException.class, () -> config.tasksFor(null));
    }

    @Test
    void tasksListIsImmutable() {
        assertThrows(UnsupportedOperationException.class,
            () -> config.tasks().add(new Task("x", "n", 1, null, null, null)));
    }

    @Test
    void groupsListIsImmutable() {
        assertThrows(UnsupportedOperationException.class,
            () -> config.groups().add(new Group("x", List.of())));
    }

    @Test
    void assignmentsListIsImmutable() {
        assertThrows(UnsupportedOperationException.class,
            () -> config.assignments().add(new Assignment("t1", "x")));
    }

    @Test
    void checkpointsListIsImmutable() {
        assertThrows(UnsupportedOperationException.class,
            () -> config.checkpoints().add(new Checkpoint("X", null, LocalDate.now())));
    }

    @Test
    void mutationOfBuilderListsDoesNotAffectBuiltConfig() {
        CourseConfigBuilder b = new CourseConfigBuilder();
        Task t = new Task("t1", "n", 1, null, null, null);
        b.addTask(t);
        CourseConfig c = b.build();

        b.addTask(new Task("t2", "n", 1, null, null, null));

        assertEquals(1, c.tasks().size());
    }

    @Test
    void canonicalConstructorCopiesInputLists() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task("t1", "n", 1, null, null, null));
        CourseConfig direct = new CourseConfig(tasks, List.of(), List.of(), List.of(),
            new CourseConfigBuilder().getSettingsBuilder().build());
        tasks.clear();
        assertEquals(1, direct.tasks().size());
    }
}
