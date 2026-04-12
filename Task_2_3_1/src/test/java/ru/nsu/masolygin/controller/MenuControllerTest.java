package ru.nsu.masolygin.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class MenuControllerTest {

    @Test
    void testControllerInstantiation() {
        MenuController controller = new MenuController();
        assertNotNull(controller);
    }

    // Since TestFX isn't currently listed in the dependencies in build.gradle,
    // we test that instantation works without an FX toolkit error (to the extent possible here).
    // Avoiding UI interactions to prevent HeadlessException or toolkit not initialized issues.
}
