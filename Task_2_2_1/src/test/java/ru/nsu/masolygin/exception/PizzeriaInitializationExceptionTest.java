package ru.nsu.masolygin.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Тест для PizzeriaInitializationException.
 */
class PizzeriaInitializationExceptionTest {

    @Test
    void testExceptionWithMessage() {
        PizzeriaInitializationException exception =
            new PizzeriaInitializationException("Invalid configuration");

        assertNotNull(exception);
        assertEquals("Invalid configuration", exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testExceptionWithMessageAndCause() {
        RuntimeException cause = new RuntimeException("Worker creation failed");
        PizzeriaInitializationException exception =
            new PizzeriaInitializationException("Failed to build", cause);

        assertNotNull(exception);
        assertEquals("Failed to build", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testExceptionMessage() {
        PizzeriaInitializationException exception =
            new PizzeriaInitializationException("Warehouse capacity must be positive");

        assertTrue(exception.getMessage().contains("Warehouse capacity"));
    }

    @Test
    void testExceptionCause() {
        RuntimeException cause = new RuntimeException("Validation error");
        PizzeriaInitializationException exception =
            new PizzeriaInitializationException("Build failed", cause);

        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof RuntimeException);
    }

    @Test
    void testExceptionInheritance() {
        PizzeriaInitializationException exception =
            new PizzeriaInitializationException("Test");

        assertTrue(exception instanceof PizzeriaException);
        assertTrue(exception instanceof Exception);
    }

    @Test
    void testExceptionWithNullCause() {
        PizzeriaInitializationException exception =
            new PizzeriaInitializationException("Error", null);

        assertNotNull(exception);
        assertEquals("Error", exception.getMessage());
        assertNull(exception.getCause());
    }
}

