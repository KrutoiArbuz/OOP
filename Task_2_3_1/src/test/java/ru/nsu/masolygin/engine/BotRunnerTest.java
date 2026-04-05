package ru.nsu.masolygin.engine;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.config.SnakeConfig;
import ru.nsu.masolygin.model.GameEngine;
import ru.nsu.masolygin.model.GameModel;
import ru.nsu.masolygin.model.Point;
import ru.nsu.masolygin.model.bot.BotSnake;

class BotRunnerTest {

    private BotRunner botRunner;
    private GameEngine engine;

    @BeforeEach
    void setUp() {
        SnakeConfig config = SnakeConfig.defaults();
        GameModel model = new GameModel(config);
        engine = new GameEngine(model, config);
        engine.reset();
        botRunner = new BotRunner();
    }

    @Test
    void testCreation() {
        assertNotNull(botRunner);
    }

    @Test
    void testExtendsAbstractGameThread() {
        assertTrue(botRunner instanceof AbstractGameThread);
    }

    @Test
    void testStartAndStopEmpty() {
        botRunner.start(new ArrayList<>(), engine);
        botRunner.stop();
        assertTrue(true);
    }

    @Test
    void testStartAndStopWithBot() {
        List<BotSnake> bots = new ArrayList<>();
        bots.add(new BotSnake(new Point(5, 5), null, "#FF0000", 100));
        botRunner.start(bots, engine);
        botRunner.stop();
        assertTrue(true);
    }

    @Test
    void testStopWithoutStart() {
        botRunner.stop();
        assertTrue(true);
    }
}
