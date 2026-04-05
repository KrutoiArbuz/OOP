package ru.nsu.masolygin.model.bot.strategy;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.config.SnakeConfig;
import ru.nsu.masolygin.model.Direction;
import ru.nsu.masolygin.model.GameEngine;
import ru.nsu.masolygin.model.GameModel;
import ru.nsu.masolygin.model.Point;
import ru.nsu.masolygin.model.bot.BotSnake;

class RandomStrategyTest {

    private RandomStrategy strategy;
    private BotSnake bot;
    private GameModel model;

    @BeforeEach
    void setUp() {
        strategy = new RandomStrategy();
        bot = new BotSnake(new Point(10, 10), strategy, "#FF0000", 200);
        SnakeConfig config = SnakeConfig.defaults();
        model = new GameModel(config);
        new GameEngine(model, config).reset();
    }

    @Test
    void testCreation() {
        assertNotNull(strategy);
    }

    @Test
    void testIsAbstractBotStrategy() {
        assertTrue(strategy instanceof AbstractBotStrategy);
    }

    @Test
    void testChooseDirection() {
        Direction dir = strategy.chooseDirection(bot, model);
        assertNotNull(dir);
    }

    @Test
    void testChooseDirectionIsValid() {
        for (int i = 0; i < 10; i++) {
            Direction dir = strategy.chooseDirection(bot, model);
            assertTrue(dir == Direction.UP || dir == Direction.DOWN
                || dir == Direction.LEFT || dir == Direction.RIGHT);
        }
    }

    @Test
    void testMultipleChoices() {
        assertNotNull(strategy.chooseDirection(bot, model));
        assertNotNull(strategy.chooseDirection(bot, model));
    }
}
