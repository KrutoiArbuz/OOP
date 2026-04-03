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
}

