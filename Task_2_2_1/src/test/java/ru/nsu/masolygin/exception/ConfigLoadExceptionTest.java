package ru.nsu.masolygin.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import org.junit.jupiter.api.Test;

/**
 * Тест для ConfigLoadException.
 */
class ConfigLoadExceptionTest {

    @Test
    void testExceptionCreation() {
        IOException cause = new IOException("File not found");
        ConfigLoadException exception = new ConfigLoadException("Failed to load", cause);

        assertNotNull(exception);
        assertEquals("Failed to load", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testExceptionMessage() {
        IOException cause = new IOException("Invalid JSON");
        ConfigLoadException exception = new ConfigLoadException("Config error", cause);

        assertTrue(exception.getMessage().contains("Config error"));
    }

    @Test
    void testExceptionCause() {
        IOException cause = new IOException("Read error");
        ConfigLoadException exception = new ConfigLoadException("Failed", cause);

        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof IOException);
    }

    @Test
    void testExceptionInheritance() {
        IOException cause = new IOException("Test");
        ConfigLoadException exception = new ConfigLoadException("Test message", cause);

        assertTrue(exception instanceof PizzeriaException);
        assertTrue(exception instanceof Exception);
    }
}

