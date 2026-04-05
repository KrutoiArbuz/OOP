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

    @BeforeEach
    void setUp() {
        SnakeConfig config = SnakeConfig.defaults();
        GameModel model = new GameModel(config);
        GameEngine engine = new GameEngine(model, config);
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
    void testStartAndStop() {
        playerRunner.start();
        playerRunner.stop();
        assertTrue(true);
    }
}
