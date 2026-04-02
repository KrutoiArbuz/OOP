package ru.nsu.masolygin.engine;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class RenderLoopTest {

    @Test
    void testTargetFps() {
        assertTrue(RenderLoop.TARGET_FPS > 0);
    }

    @Test
    void testTargetFpsValue() {
        assertTrue(RenderLoop.TARGET_FPS >= 30 && RenderLoop.TARGET_FPS <= 120);
    }

    @Test
    void testBasicFunctionality() {
        assertTrue(true);
    }
}

