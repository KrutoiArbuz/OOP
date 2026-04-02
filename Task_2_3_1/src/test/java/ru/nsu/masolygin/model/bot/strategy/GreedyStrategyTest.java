package ru.nsu.masolygin.model.bot.strategy;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.model.Direction;
import ru.nsu.masolygin.model.Point;
import ru.nsu.masolygin.model.bot.BotSnake;
import ru.nsu.masolygin.config.SnakeConfig;
import ru.nsu.masolygin.model.GameModel;
import ru.nsu.masolygin.model.bot.BotStrategy;

class GreedyStrategyTest {

    private GreedyStrategy strategy;
    private BotSnake bot;
    private GameModel gameModel;

    @BeforeEach
    void setUp() {
        strategy = new GreedyStrategy();
        bot = new BotSnake(new Point(10, 10), strategy, "#FF0000", 200);
        gameModel = new GameModel(new SnakeConfig());
    }

    @Test
    void testStrategyCreation() {
        assertNotNull(strategy);
    }

    @Test
    void testIsBotStrategy() {
        assertNotNull(strategy);
        assertTrue(strategy instanceof BotStrategy);
    }

    @Test
    void testChooseDirection() {
        Direction dir = strategy.chooseDirection(bot, gameModel);
        assertNotNull(dir);
    }

    @Test
    void testChooseDirectionIsValid() {
        for (int i = 0; i < 10; i++) {
            Direction dir = strategy.chooseDirection(bot, gameModel);
            assertTrue(dir == Direction.UP || dir == Direction.DOWN
                || dir == Direction.LEFT || dir == Direction.RIGHT);
        }
    }

    @Test
    void testMultipleDirectionChoices() {
        Direction dir1 = strategy.chooseDirection(bot, gameModel);
        Direction dir2 = strategy.chooseDirection(bot, gameModel);
        assertNotNull(dir1);
        assertNotNull(dir2);
    }
}

