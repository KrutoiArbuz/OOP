package ru.nsu.masolygin.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConfigLoaderTest {

    private ConfigLoader configLoader;

    @BeforeEach
    void setUp() {
        configLoader = new ConfigLoader();
    }

    @Test
    void testConfigLoaderCreation() {
        assertNotNull(configLoader);
    }

    @Test
    void testLoadExistingConfig() {
        SnakeConfig config = configLoader.load("/config.json");
        assertNotNull(config);
        assertEquals(25, config.getFieldWidth());
        assertEquals(25, config.getFieldHeight());
    }

    @Test
    void testLoadNonexistentConfigThrowsException() {
        assertThrows(ConfigLoadException.class, () -> {
            configLoader.load("/nonexistent.json");
        });
    }

    @Test
    void testLoadConfigReturnsValidSnakeConfig() {
        SnakeConfig config = configLoader.load("/config.json");
        assertNotNull(config.getFieldWidth());
        assertNotNull(config.getFieldHeight());
    }

    @Test
    void testLoadMultipleConfigs() {
        SnakeConfig config1 = configLoader.load("/config.json");
        SnakeConfig config2 = configLoader.load("/config.json");
        assertNotNull(config1);
        assertNotNull(config2);
    }
}
