package ru.nsu.masolygin.oopchecker.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class TaskTest {

    @Test
    void effectiveLabPathReturnsIdWhenLabPathIsNull() {
        Task task = new Task("2_1_1", "Простые числа", 1,
            LocalDate.of(2026, 2, 14), LocalDate.of(2026, 3, 2));
        assertEquals("2_1_1", task.effectiveLabPath());
    }

    @Test
    void effectiveLabPathReturnsCustomPathWhenSet() {
        Task task = new Task("2_1_1", "Простые числа", 1,
            LocalDate.of(2026, 2, 14), LocalDate.of(2026, 3, 2), "Task_2_1_1");
        assertEquals("Task_2_1_1", task.effectiveLabPath());
    }

    @Test
    void effectiveLabPathReturnsIdWhenLabPathIsBlank() {
        Task task = new Task("2_1_1", "Простые числа", 1,
            LocalDate.of(2026, 2, 14), LocalDate.of(2026, 3, 2), "   ");
        assertEquals("2_1_1", task.effectiveLabPath());
    }

    @Test
    void taskFieldsAreAccessible() {
        LocalDate soft = LocalDate.of(2026, 2, 14);
        LocalDate hard = LocalDate.of(2026, 3, 2);
        Task task = new Task("t1", "Задача", 5, soft, hard);
        assertEquals("t1", task.id());
        assertEquals("Задача", task.name());
        assertEquals(5, task.maxPoints());
        assertEquals(soft, task.softDeadline());
        assertEquals(hard, task.hardDeadline());
    }

    @Test
    void taskWithNullDeadlinesIsCreated() {
        Task task = new Task("t1", "Задача", 1, null, null);
        assertNotNull(task);
        assertEquals("t1", task.effectiveLabPath());
    }
}
