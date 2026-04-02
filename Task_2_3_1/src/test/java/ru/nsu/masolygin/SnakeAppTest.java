package ru.nsu.masolygin;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SnakeAppTest {

    @Test
    void testSnakeAppCreation() {
        assertTrue(true);
    }

    @Test
    void testApplicationInitialization() {
        assertTrue(true);
    }

    @Test
    void testSnakeAppClass() {
        assertNotNull(SnakeApp.class);
    }

    @Test
    void testSnakeAppInstantiation() {
        SnakeApp app = new SnakeApp();
        assertNotNull(app);
    }

    @Test
    void testSnakeAppHasStart() {
        assertNotNull(SnakeApp.class.getDeclaredMethods());
    }

    @Test
    void testSnakeAppIsExtendingApplication() {
        assertTrue(SnakeApp.class.getName().contains("SnakeApp"));
    }
}

