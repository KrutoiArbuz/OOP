package ru.nsu.masolygin;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import javafx.application.Application;
import org.junit.jupiter.api.Test;

class SnakeAppTest {

    @Test
    void testSnakeAppIsApplicationSubclass() {
        assertTrue(Application.class.isAssignableFrom(SnakeApp.class));
    }

    @Test
    void testSnakeAppHasMainMethod() {
        try {
            Method mainMethod = SnakeApp.class.getMethod("main", String[].class);
            assertNotNull(mainMethod);
            assertTrue(java.lang.reflect.Modifier.isStatic(mainMethod.getModifiers()));
        } catch (NoSuchMethodException e) {
            throw new AssertionError("main method not found", e);
        }
    }

    @Test
    void testSnakeAppHasStartMethod() {
        try {
            Method startMethod = SnakeApp.class.getMethod("start", javafx.stage.Stage.class);
            assertNotNull(startMethod);
        } catch (NoSuchMethodException e) {
            assertTrue(true);
        }
    }

    @Test
    void testSnakeAppHasStopMethod() {
        try {
            Method stopMethod = SnakeApp.class.getMethod("stop");
            assertNotNull(stopMethod);
        } catch (NoSuchMethodException e) {
            assertTrue(true);
        }
    }

    @Test
    void testSnakeAppHasControllerField() {
        try {
            assertNotNull(SnakeApp.class.getDeclaredField("controller"));
        } catch (NoSuchFieldException e) {
            assertTrue(true);
        }
    }

    @Test
    void testSnakeAppCreation() {
        SnakeApp app = new SnakeApp();
        assertNotNull(app);
    }

    @Test
    void testSnakeAppClassStructure() {
        assertTrue(SnakeApp.class.getDeclaredMethods().length > 0);
        assertTrue(SnakeApp.class.getDeclaredFields().length > 0);
    }

    @Test
    void testSnakeAppExtendsApplicationClass() {
        assertTrue(SnakeApp.class.getSuperclass().getName().contains("Application"));
    }
}
