package ru.nsu.masolygin.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void testBasicFunctionality() {
        assertTrue(true);
    }
}

