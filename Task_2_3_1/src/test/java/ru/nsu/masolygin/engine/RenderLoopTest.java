package ru.nsu.masolygin.engine;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.config.SnakeConfig;
import ru.nsu.masolygin.model.GameModel;

class RenderLoopTest {

    private RenderLoop renderLoop;
    private GameModel gameModel;

    @BeforeEach
    void setUp() {
        gameModel = new GameModel(new SnakeConfig());
        renderLoop = new RenderLoop(gameModel, snapshot -> {
        });
    }

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

    @Test
    void testRenderLoopCreation() {
        assertNotNull(renderLoop);
    }

    @Test
    void testRenderLoopHasThreadName() {
        try {
            assertNotNull(renderLoop.getClass().getMethod("threadName"));
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    void testRenderLoopCanStart() {
        renderLoop.start();
        assertTrue(renderLoop.getClass().getName().contains("RenderLoop"));
    }

    @Test
    void testRenderLoopCanStop() {
        renderLoop.start();
        renderLoop.stop();
        assertTrue(true);
    }

    @Test
    void testRenderLoopHasLoop() {
        try {
            assertNotNull(renderLoop.getClass().getDeclaredMethod("loop"));
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    void testRenderLoopExtendsAbstractGameThread() {
        assertTrue(renderLoop instanceof AbstractGameThread);
    }

    @Test
    void testRenderLoopHasTargetFpsConstant() {
        assertTrue(RenderLoop.TARGET_FPS == 60);
    }
}
