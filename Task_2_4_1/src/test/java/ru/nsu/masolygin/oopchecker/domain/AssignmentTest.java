package ru.nsu.masolygin.oopchecker.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class AssignmentTest {

    @Test
    void fieldsAreAccessible() {
        Assignment a = new Assignment("2_1_1", "ivanov");
        assertEquals("2_1_1", a.taskId());
        assertEquals("ivanov", a.studentGithub());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t"})
    void blankTaskIdIsRejected(String bad) {
        assertThrows(IllegalArgumentException.class,
            () -> new Assignment(bad, "ivanov"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t"})
    void blankStudentGithubIsRejected(String bad) {
        assertThrows(IllegalArgumentException.class,
            () -> new Assignment("2_1_1", bad));
    }

    @Test
    void recordEqualityByValue() {
        Assignment a = new Assignment("t1", "u1");
        Assignment b = new Assignment("t1", "u1");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
