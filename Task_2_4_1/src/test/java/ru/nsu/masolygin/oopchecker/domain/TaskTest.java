package ru.nsu.masolygin.oopchecker.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class TaskTest {

    private static final LocalDate SOFT = LocalDate.of(2024, 10, 1);
    private static final LocalDate HARD = LocalDate.of(2024, 10, 15);

    @Test
    void allFieldsAreStored() {
        Task task = new Task("t1", "Простые числа", 10, SOFT, HARD, "Task_2_1_1");
        assertAll(
            () -> assertEquals("t1", task.id()),
            () -> assertEquals("Простые числа", task.name()),
            () -> assertEquals(10, task.maxPoints()),
            () -> assertEquals(SOFT, task.softDeadline()),
            () -> assertEquals(HARD, task.hardDeadline()),
            () -> assertEquals("Task_2_1_1", task.labPath())
        );
    }

    @Test
    void labPathDefaultsToIdWhenNull() {
        Task task = new Task("t1", "Задача", 5, SOFT, HARD, null);
        assertEquals("t1", task.labPath());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t"})
    void labPathDefaultsToIdWhenBlank(String blank) {
        Task task = new Task("t1", "Задача", 5, SOFT, HARD, blank);
        assertEquals("t1", task.labPath());
    }

    @Test
    void labPathKeepsCustomValue() {
        Task task = new Task("t1", "Задача", 5, SOFT, HARD, "labs/Task_1");
        assertEquals("labs/Task_1", task.labPath());
    }

    @Test
    void shortConstructorSetsLabPathToId() {
        Task task = new Task("2_1_1", "Задача", 5, SOFT, HARD, null);
        assertEquals("2_1_1", task.labPath());
    }

    @Test
    void nullDeadlinesAreAllowed() {
        Task task = new Task("t1", "Задача", 5, null, null, null);
        assertAll(
            () -> assertEquals(null, task.softDeadline()),
            () -> assertEquals(null, task.hardDeadline())
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void blankIdIsRejected(String bad) {
        assertThrows(IllegalArgumentException.class,
            () -> new Task(bad, "name", 1, SOFT, HARD, null));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void blankNameIsRejected(String bad) {
        assertThrows(IllegalArgumentException.class,
            () -> new Task("id", bad, 1, SOFT, HARD, null));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -100})
    void nonPositiveMaxPointsIsRejected(int bad) {
        assertThrows(IllegalArgumentException.class,
            () -> new Task("id", "name", bad, SOFT, HARD, null));
    }

    @Test
    void recordEqualityIsByValue() {
        Task a = new Task("t1", "n", 1, SOFT, HARD, "p");
        Task b = new Task("t1", "n", 1, SOFT, HARD, "p");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
