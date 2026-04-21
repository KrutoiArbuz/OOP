package ru.nsu.masolygin.engine;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.config.SnakeConfig;
import ru.nsu.masolygin.model.GameEngine;
import ru.nsu.masolygin.model.GameModel;

class PlayerRunnerTest {

    private PlayerRunner playerRunner;
    private GameEngine engine;

    @BeforeEach
    void setUp() {
        SnakeConfig config = SnakeConfig.defaults();
        GameModel model = new GameModel(config);
        engine = new GameEngine(model, config);
        engine.reset();
        playerRunner = new PlayerRunner(engine);
    }

    @Test
    void testCreation() {
        assertNotNull(playerRunner);
    }

    @Test
    void testExtendsAbstractGameThread() {
        assertTrue(playerRunner instanceof AbstractGameThread);
    }

    @Test
    void testThreadName() {
        assertNotNull(playerRunner.threadName());
        assertTrue(playerRunner.threadName().contains("player"));
    }

    @Test
    void testStartAndStop() {
        playerRunner.start();
        assertTrue(playerRunner.running);
        playerRunner.stop();
    }

    @Test
    void testStartMultipleTimes() {
        playerRunner.start();
        playerRunner.stop();
        playerRunner.start();
        assertTrue(playerRunner.running);
        playerRunner.stop();
    }

    @Test
    void testEngineNotNull() {
        assertNotNull(engine);
    }
}

