package ru.nsu.masolygin;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class LauncherTest {

    @Test
    void testLauncherCreation() {
        Launcher launcher = new Launcher();
        assertNotNull(launcher);
    }
}

