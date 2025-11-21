package ru.nsu.masolygin.markdown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class TaskTest {

    @Test
    void testTaskListWithChecked() {
        Task task = new Task.Builder()
            .addTask("Completed task", true)
            .addTask("Incomplete task", false)
            .build();
        assertEquals("- [x] Completed task\n- [ ] Incomplete task", task.toMarkdown());
    }

    @Test
    void testTaskListDefaultUnchecked() {
        Task task = new Task.Builder()
            .addTask("Task 1")
            .addTask("Task 2")
            .build();
        assertEquals("- [ ] Task 1\n- [ ] Task 2", task.toMarkdown());
    }

    @Test
    void testSingleTask() {
        Task task = new Task.Builder()
            .addTask("Single task", true)
            .build();
        assertEquals("- [x] Single task", task.toMarkdown());
    }

    @Test
    void testEmptyTaskList() {
        Task task = new Task.Builder().build();
        assertEquals("", task.toMarkdown());
    }

    @Test
    void testMixedTasks() {
        Task task = new Task.Builder()
            .addTask("Task 1", true)
            .addTask("Task 2")
            .addTask("Task 3", false)
            .addTask("Task 4", true)
            .build();
        assertEquals("- [x] Task 1\n- [ ] Task 2\n- [ ] Task 3\n- [x] Task 4",
                     task.toMarkdown());
    }

    @Test
    void testEquals() {
        Task task1 = new Task.Builder()
            .addTask("Task", true)
            .build();
        Task task2 = new Task.Builder()
            .addTask("Task", true)
            .build();
        assertEquals(task1, task2);
    }

    @Test
    void testEqualsSameObject() {
        Task task = new Task.Builder()
            .addTask("Task", true)
            .build();
        assertEquals(task, task);
    }

    @Test
    void testEqualsDifferentTasks() {
        Task task1 = new Task.Builder()
            .addTask("Task 1", true)
            .build();
        Task task2 = new Task.Builder()
            .addTask("Task 2", true)
            .build();
        assertNotEquals(task1, task2);
    }

    @Test
    void testEqualsDifferentCheckedState() {
        Task task1 = new Task.Builder()
            .addTask("Task", true)
            .build();
        Task task2 = new Task.Builder()
            .addTask("Task", false)
            .build();
        assertNotEquals(task1, task2);
    }

    @Test
    void testEqualsNull() {
        Task task = new Task.Builder()
            .addTask("Task", true)
            .build();
        assertNotEquals(task, null);
    }

    @Test
    void testEqualsDifferentClass() {
        Task task = new Task.Builder()
            .addTask("Task", true)
            .build();
        assertNotEquals(task, "Task");
    }

    @Test
    void testHashCode() {
        Task task1 = new Task.Builder()
            .addTask("Task", true)
            .build();
        Task task2 = new Task.Builder()
            .addTask("Task", true)
            .build();
        assertEquals(task1.hashCode(), task2.hashCode());
    }

    @Test
    void testToString() {
        Task task = new Task.Builder()
            .addTask("Task", true)
            .build();
        assertEquals("- [x] Task", task.toString());
    }
}

