package ru.nsu.masolygin.oopchecker.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class GroupTest {

    private static final Student ALICE = new Student("alice", "Алиса А.А.", "https://x/a.git");
    private static final Student BOB = new Student("bob", "Боб Б.Б.", "https://x/b.git");

    @Test
    void groupKeepsAllStudents() {
        Group g = new Group("24216", List.of(ALICE, BOB));
        assertEquals(2, g.students().size());
        assertEquals(ALICE, g.students().get(0));
        assertEquals(BOB, g.students().get(1));
    }

    @Test
    void emptyStudentListIsAllowed() {
        Group g = new Group("24216", List.of());
        assertEquals(0, g.students().size());
    }

    @Test
    void studentsListIsImmutable() {
        Group g = new Group("24216", List.of(ALICE));
        assertThrows(UnsupportedOperationException.class,
            () -> g.students().add(BOB));
    }

    @Test
    void mutationOfSourceListDoesNotAffectGroup() {
        List<Student> source = new ArrayList<>();
        source.add(ALICE);
        Group g = new Group("24216", source);

        source.add(BOB);

        assertEquals(1, g.students().size());
    }

    @Test
    void nullNameIsRejected() {
        assertThrows(NullPointerException.class,
            () -> new Group(null, List.of(ALICE)));
    }

    @Test
    void nullStudentsListIsRejected() {
        assertThrows(NullPointerException.class,
            () -> new Group("24216", null));
    }
}
