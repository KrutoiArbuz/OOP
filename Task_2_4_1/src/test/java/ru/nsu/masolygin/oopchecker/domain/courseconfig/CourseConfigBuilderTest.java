package ru.nsu.masolygin.oopchecker.domain.courseconfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.oopchecker.domain.Assignment;
import ru.nsu.masolygin.oopchecker.domain.Checkpoint;
import ru.nsu.masolygin.oopchecker.domain.Group;
import ru.nsu.masolygin.oopchecker.domain.Student;
import ru.nsu.masolygin.oopchecker.domain.Task;

class CourseConfigBuilderTest {

    private CourseConfigBuilder builder;

    @BeforeEach
    void setUp() {
        builder = new CourseConfigBuilder();
    }

    @Test
    void emptyBuilderProducesEmptyConfig() {
        CourseConfig config = builder.build();
        assertEquals(0, config.tasks().size());
        assertEquals(0, config.groups().size());
        assertEquals(0, config.assignments().size());
        assertEquals(0, config.checkpoints().size());
    }

    @Test
    void addedTaskAppearsInConfig() {
        Task task = new Task("t1", "Задача", 5, null, null, null);
        builder.addTask(task);
        CourseConfig config = builder.build();
        assertEquals(1, config.tasks().size());
        assertEquals(task, config.tasks().get(0));
    }

    @Test
    void groupsArePreservedInInsertionOrder() {
        builder.addGroup(new Group("a", List.of()));
        builder.addGroup(new Group("b", List.of()));
        builder.addGroup(new Group("c", List.of()));
        List<Group> groups = builder.build().groups();
        assertEquals(List.of("a", "b", "c"),
            groups.stream().map(Group::name).toList());
    }

    @Test
    void findGroupReturnsAddedGroup() {
        Group g = new Group("24216", List.of());
        builder.addGroup(g);
        assertTrue(builder.findGroup("24216").isPresent());
        assertEquals(g, builder.findGroup("24216").orElseThrow());
    }

    @Test
    void findGroupReturnsEmptyForUnknownName() {
        assertFalse(builder.findGroup("nope").isPresent());
    }

    @Test
    void addingSameGroupNameOverwrites() {
        builder.addGroup(new Group("g", List.of()));
        builder.addGroup(new Group("g", List.of(
            new Student("a", "A", "https://x/a.git"))));
        assertEquals(1, builder.build().groups().size());
        assertEquals(1, builder.build().groups().get(0).students().size());
    }

    @Test
    void multipleAssignmentsAreCollected() {
        builder.addAssignment(new Assignment("t1", "u1"));
        builder.addAssignment(new Assignment("t2", "u1"));
        assertEquals(2, builder.build().assignments().size());
    }

    @Test
    void multipleCheckpointsAreCollected() {
        builder.addCheckpoint(new Checkpoint("КТ1", null, LocalDate.of(2024, 1, 1)));
        builder.addCheckpoint(new Checkpoint("КТ2", null, LocalDate.of(2024, 6, 1)));
        assertEquals(2, builder.build().checkpoints().size());
    }

    @Test
    void nullTaskIsRejected() {
        assertThrows(NullPointerException.class, () -> builder.addTask(null));
    }

    @Test
    void nullGroupIsRejected() {
        assertThrows(NullPointerException.class, () -> builder.addGroup(null));
    }

    @Test
    void nullAssignmentIsRejected() {
        assertThrows(NullPointerException.class, () -> builder.addAssignment(null));
    }

    @Test
    void nullCheckpointIsRejected() {
        assertThrows(NullPointerException.class, () -> builder.addCheckpoint(null));
    }

    @Test
    void getSettingsBuilderIsNotNull() {
        assertTrue(builder.getSettingsBuilder() != null);
    }

    @Test
    void settingsBuilderIsSameInstanceBetweenCalls() {
        assertEquals(builder.getSettingsBuilder(), builder.getSettingsBuilder());
    }
}
