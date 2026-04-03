package ru.nsu.masolygin.engine;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.config.SnakeConfig;
import ru.nsu.masolygin.model.GameModel;
import ru.nsu.masolygin.model.GameSnapshot;

class RenderLoopTest {

    private RenderLoop renderLoop;
    private GameModel gameModel;
    private Consumer<GameSnapshot> mockRenderer;

    @BeforeEach
    void setUp() {
        gameModel = new GameModel(new SnakeConfig());
        mockRenderer = snapshot -> {
        };
        renderLoop = new RenderLoop(gameModel, mockRenderer);
    }

    @Test
    void testRenderLoopCreation() {
        assertNotNull(renderLoop);
    }

    @Test
    void testRenderLoopStart() {
        renderLoop.start();
        renderLoop.stop();
    }

    @Test
    void testRenderLoopStop() {
        renderLoop.start();
        renderLoop.stop();
    }

    @Test
    void testTargetFps() {
        assertTrue(RenderLoop.TARGET_FPS > 0);
    }

    @Test
    void testRenderLoopWithMultipleStartStop() {
        for (int i = 0; i < 3; i++) {
            renderLoop.start();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            renderLoop.stop();
        }
    }

    @Test
    void testRenderLoopMultipleFrames() {
        renderLoop.start();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        renderLoop.stop();
    }

    @Test
    void testRenderLoopWithGameModelUpdates() {
        renderLoop.start();
        gameModel.setDirection(ru.nsu.masolygin.model.Direction.UP);
        gameModel.executePlayerStep();
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        renderLoop.stop();
    }

    @Test
    void testRenderLoopFrameRate() {
        assertTrue(RenderLoop.TARGET_FPS == 60);
    }
}

