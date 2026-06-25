package ru.nsu.masolygin.oopchecker.dsl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Method;
import java.util.Map;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.oopchecker.domain.Group;

class GroupDelegateTest {

    private Group build(GroupDelegate delegate) throws Exception {
        Method m = GroupDelegate.class.getDeclaredMethod("build");
        m.setAccessible(true);
        return (Group) m.invoke(delegate);
    }

    @Test
    void emptyDelegateBuildsGroupWithNoStudents() throws Exception {
        GroupDelegate d = new GroupDelegate("24216");
        Group g = build(d);
        assertEquals("24216", g.name());
        assertEquals(0, g.students().size());
    }

    @Test
    void studentNamedArgsArePassedToStudent() throws Exception {
        GroupDelegate d = new GroupDelegate("g");
        d.student(Map.of(
            "github", "ivanov",
            "name", "Иванов И.И.",
            "repo", "https://github.com/ivanov/oop.git"
        ));
        Group g = build(d);
        assertEquals(1, g.students().size());
        assertEquals("ivanov", g.students().get(0).github());
        assertEquals("Иванов И.И.", g.students().get(0).fullName());
    }

    @Test
    void multipleStudentsPreserveInsertionOrder() throws Exception {
        GroupDelegate d = new GroupDelegate("g");
        d.student(Map.of("github", "a", "name", "A", "repo", "https://x/a"));
        d.student(Map.of("github", "b", "name", "B", "repo", "https://x/b"));
        d.student(Map.of("github", "c", "name", "C", "repo", "https://x/c"));
        Group g = build(d);
        assertEquals("a", g.students().get(0).github());
        assertEquals("b", g.students().get(1).github());
        assertEquals("c", g.students().get(2).github());
    }

    @Test
    void missingRequiredFieldsFailStudentCreation() {
        GroupDelegate d = new GroupDelegate("g");
        assertThrows(IllegalArgumentException.class,
            () -> d.student(Map.of("github", "x", "name", "X"))); // нет repo
    }
}
