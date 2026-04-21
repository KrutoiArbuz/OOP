package ru.nsu.masolygin.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConfigLoaderTest {

    private ConfigLoader configLoader;

    private static void assertTrue(boolean condition) {
        org.junit.jupiter.api.Assertions.assertTrue(condition);
    }

    @BeforeEach
    void setUp() {
        configLoader = new ConfigLoader();
    }

    @Test
    void testCreation() {
        assertNotNull(configLoader);
    }

    @Test
    void testLoadExistingConfig() {
        SnakeConfig config = configLoader.load("/config.json");
        assertNotNull(config);
        assertEquals(25, config.fieldWidth());
        assertEquals(25, config.fieldHeight());
    }

    @Test
    void testLoadNonexistentConfigThrowsException() {
        assertThrows(ConfigLoadException.class, () ->
            configLoader.load("/nonexistent.json"));
    }

    @Test
    void testLoadedConfigIsValid() {
        SnakeConfig config = configLoader.load("/config.json");
        assertTrue(config.fieldWidth() > 0);
        assertTrue(config.fieldHeight() > 0);
    }

    @Test
    void testLoadMultipleTimes() {
        assertNotNull(configLoader.load("/config.json"));
        assertNotNull(configLoader.load("/config.json"));
    }
}
