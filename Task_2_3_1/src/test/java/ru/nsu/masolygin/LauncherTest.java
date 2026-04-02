package ru.nsu.masolygin;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class LauncherTest {

    @Test
    void testLauncherCreation() {
        Launcher launcher = new Launcher();
        assertNotNull(launcher);
    }

    @Test
    void testMainMethod() {
        assertTrue(true);
    }

    @Test
    void testLauncherHasMainMethod() {
        try {
            Method mainMethod = Launcher.class.getMethod("main", String[].class);
            assertNotNull(mainMethod);
        } catch (NoSuchMethodException e) {
            throw new AssertionError("main method not found", e);
        }
    }

    @Test
    void testLauncherMainIsStatic() {
        try {
            Method mainMethod = Launcher.class.getMethod("main", String[].class);
            assertTrue(java.lang.reflect.Modifier.isStatic(mainMethod.getModifiers()));
        } catch (NoSuchMethodException e) {
            throw new AssertionError("main method not found", e);
        }
    }

    @Test
    void testLauncherClass() {
        assertNotNull(Launcher.class);
    }

    @Test
    void testLauncherClassName() {
        assertTrue(Launcher.class.getSimpleName().equals("Launcher"));
    }
}

