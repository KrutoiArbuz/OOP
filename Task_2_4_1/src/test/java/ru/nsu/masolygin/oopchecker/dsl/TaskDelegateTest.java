package ru.nsu.masolygin.oopchecker.dsl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Method;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.oopchecker.domain.Task;

class TaskDelegateTest {

    private TaskDelegate delegate;

    @BeforeEach
    void setUp() {
        delegate = new TaskDelegate();
    }

    private Task build() throws Exception {
        Method m = TaskDelegate.class.getDeclaredMethod("build");
        m.setAccessible(true);
        return (Task) m.invoke(delegate);
    }

    @Test
    void buildAssemblesTaskWithAllFields() throws Exception {
        delegate.id("t1");
        delegate.name("Простые числа");
        delegate.maxPoints(10);
        delegate.softDeadline("2024-10-01");
        delegate.hardDeadline("2024-10-15");

        Task t = build();
        assertEquals("t1", t.id());
        assertEquals("Простые числа", t.name());
        assertEquals(10, t.maxPoints());
        assertEquals(LocalDate.of(2024, 10, 1), t.softDeadline());
        assertEquals(LocalDate.of(2024, 10, 15), t.hardDeadline());
    }

    @Test
    void labPathDefaultsToIdWhenNotSet() throws Exception {
        delegate.id("t1");
        delegate.name("n");
        delegate.maxPoints(1);
        Task t = build();
        assertEquals("t1", t.labPath());
    }

    @Test
    void customLabPathIsKept() throws Exception {
        delegate.id("t1");
        delegate.name("n");
        delegate.maxPoints(1);
        delegate.labPath("labs/Task_t1");
        Task t = build();
        assertEquals("labs/Task_t1", t.labPath());
    }

    @Test
    void missingIdFailsBuild() {
        delegate.name("n");
        delegate.maxPoints(1);
        assertThrows(Exception.class, this::build);
    }

    @Test
    void missingNameFailsBuild() {
        delegate.id("t1");
        delegate.maxPoints(1);
        assertThrows(Exception.class, this::build);
    }

    @Test
    void invalidIsoDateThrows() {
        assertThrows(java.time.format.DateTimeParseException.class,
            () -> delegate.softDeadline("not-a-date"));
    }
}
