package ru.nsu.masolygin.oopchecker.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CourseConfigTest {

    private CourseConfig config;

    @BeforeEach
    void setUp() {
        config = new CourseConfig();
        config.addTask(new Task("t1", "Задача 1", 10,
            LocalDate.of(2024, 1, 10), LocalDate.of(2024, 1, 20)));
        config.addTask(new Task("t2", "Задача 2", 5,
            LocalDate.of(2024, 2, 10), LocalDate.of(2024, 2, 20)));
        config.addGroup(new Group("24216", List.of(
            new Student("alice", "Алиса А.А.", "https://example.com/alice.git"),
            new Student("bob", "Боб Б.Б.", "https://example.com/bob.git")
        )));
        config.addAssignment(new Assignment("t1", "alice"));
        config.addAssignment(new Assignment("t2", "bob"));
        config.addCheckpoint(new Checkpoint("КТ1", LocalDate.of(2024, 1, 31)));
    }

    @Test
    void findTaskByIdReturnsCorrectTask() {
        assertTrue(config.findTask("t1").isPresent());
        assertEquals("Задача 1", config.findTask("t1").get().name());
    }

    @Test
    void findTaskWithUnknownIdReturnsEmpty() {
        assertFalse(config.findTask("unknown").isPresent());
    }

    @Test
    void findStudentByGithubReturnsCorrectStudent() {
        assertTrue(config.findStudent("alice").isPresent());
        assertEquals("Алиса А.А.", config.findStudent("alice").get().fullName());
    }

    @Test
    void findStudentWithUnknownGithubReturnsEmpty() {
        assertFalse(config.findStudent("nobody").isPresent());
    }

    @Test
    void findGroupOfReturnsGroupContainingStudent() {
        assertTrue(config.findGroupOf("alice").isPresent());
        assertEquals("24216", config.findGroupOf("alice").get().name());
    }

    @Test
    void findGroupOfReturnsEmptyForUnknownStudent() {
        assertFalse(config.findGroupOf("nobody").isPresent());
    }

    @Test
    void tasksListIsUnmodifiable() {
        List<Task> tasks = config.tasks();
        assertEquals(2, tasks.size());
        assertThrows(UnsupportedOperationException.class,
            () -> tasks.add(new Task("t3", "X", 1, null, null)));
    }


    @Test
    void assignmentsContainExpectedEntries() {
        assertEquals(2, config.assignments().size());
        assertTrue(config.assignments().contains(new Assignment("t1", "alice")));
        assertTrue(config.assignments().contains(new Assignment("t2", "bob")));
    }

    @Test
    void checkpointsContainAddedCheckpoint() {
        assertEquals(1, config.checkpoints().size());
        assertEquals("КТ1", config.checkpoints().get(0).name());
    }

    @Test
    void settingsInstanceIsNotNull() {
        assertNotNull(config.settings());
    }
}
