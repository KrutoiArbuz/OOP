package ru.nsu.masolygin.engine;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.config.SnakeConfig;
import ru.nsu.masolygin.model.Direction;
import ru.nsu.masolygin.model.GameEngine;
import ru.nsu.masolygin.model.GameModel;
import ru.nsu.masolygin.model.GameSnapshot;

class RenderLoopTest {

    private RenderLoop renderLoop;
    private GameModel model;
    private GameEngine engine;

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @BeforeEach
    void setUp() {
        SnakeConfig config = SnakeConfig.defaults();
        model = new GameModel(config);
        engine = new GameEngine(model, config);
        engine.reset();
        Consumer<GameSnapshot> mockRenderer = snapshot -> {
        };
        renderLoop = new RenderLoop(model, mockRenderer);
    }

    @Test
    void testCreation() {
        assertNotNull(renderLoop);
    }

    @Test
    void testTargetFps() {
        assertTrue(RenderLoop.TARGET_FPS > 0);
        assertTrue(RenderLoop.TARGET_FPS == 60);
    }

    @Test
    void testStartAndStop() {
        renderLoop.start();
        renderLoop.stop();
    }

    @Test
    void testMultipleStartStop() {
        for (int i = 0; i < 3; i++) {
            renderLoop.start();
            sleep(50);
            renderLoop.stop();
        }
    }

    @Test
    void testRunsFramesDuringInterval() {
        renderLoop.start();
        sleep(200);
        renderLoop.stop();
    }

    @Test
    void testWithGameModelUpdates() {
        renderLoop.start();
        model.setDirection(Direction.UP);
        engine.executePlayerStep();
        sleep(150);
        renderLoop.stop();
    }
}
