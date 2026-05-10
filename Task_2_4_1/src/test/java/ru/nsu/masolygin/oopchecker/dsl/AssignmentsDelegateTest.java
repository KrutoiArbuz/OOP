package ru.nsu.masolygin.oopchecker.dsl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.oopchecker.domain.Assignment;
import ru.nsu.masolygin.oopchecker.domain.Group;
import ru.nsu.masolygin.oopchecker.domain.Student;
import ru.nsu.masolygin.oopchecker.domain.courseconfig.CourseConfigBuilder;

class AssignmentsDelegateTest {

    private CourseConfigBuilder builder;
    private AssignmentsDelegate delegate;

    @BeforeEach
    void setUp() {
        builder = new CourseConfigBuilder();
        delegate = new AssignmentsDelegate(builder);
    }

    @Test
    void checkByStudentAddsOneAssignment() {
        delegate.check(Map.of("task", "t1", "student", "alice"));
        assertEquals(List.of(new Assignment("t1", "alice")), builder.build().assignments());
    }

    @Test
    void checkByGroupAddsAssignmentForEachStudent() {
        builder.addGroup(new Group("g", List.of(
            new Student("a", "A", "https://x/a.git"),
            new Student("b", "B", "https://x/b.git"))));

        delegate.check(Map.of("task", "t1", "group", "g"));

        assertEquals(2, builder.build().assignments().size());
    }

    @Test
    void unknownGroupThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> delegate.check(Map.of("task", "t", "group", "ghost")));
    }

    @Test
    void blankTaskIsRejected() {
        assertThrows(IllegalArgumentException.class,
            () -> delegate.check(Map.of("task", "", "student", "a")));
    }

    @Test
    void missingTaskIsRejected() {
        assertThrows(IllegalArgumentException.class,
            () -> delegate.check(Map.of("student", "a")));
    }

    @Test
    void neitherStudentNorGroupIsRejected() {
        assertThrows(IllegalArgumentException.class,
            () -> delegate.check(Map.of("task", "t")));
    }

    @Test
    void bothStudentAndGroupIsRejected() {
        builder.addGroup(new Group("g", List.of()));
        assertThrows(IllegalArgumentException.class,
            () -> delegate.check(Map.of("task", "t", "student", "a", "group", "g")));
    }

    @Test
    void emptyGroupExpandsToZeroAssignments() {
        builder.addGroup(new Group("g", List.of()));
        delegate.check(Map.of("task", "t", "group", "g"));
        assertEquals(0, builder.build().assignments().size());
    }
}
