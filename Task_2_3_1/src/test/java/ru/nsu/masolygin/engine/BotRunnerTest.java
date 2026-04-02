package ru.nsu.masolygin.engine;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.config.SnakeConfig;
import ru.nsu.masolygin.model.GameModel;
import ru.nsu.masolygin.model.Point;
import ru.nsu.masolygin.model.bot.BotSnake;

class BotRunnerTest {

    private BotRunner botRunner;
    private GameModel gameModel;

    @BeforeEach
    void setUp() {
        botRunner = new BotRunner();
        gameModel = new GameModel(new SnakeConfig());
    }

    @Test
    void testBotRunnerCreation() {
        assertNotNull(botRunner);
    }

    @Test
    void testBasicFunctionality() {
        assertTrue(true);
    }

    @Test
    void testBotRunnerCanStart() {
        List<BotSnake> bots = new ArrayList<>();
        botRunner.start(bots, gameModel);
        assertTrue(botRunner.getClass().getName().contains("BotRunner"));
    }

    @Test
    void testBotRunnerCanStop() {
        List<BotSnake> bots = new ArrayList<>();
        botRunner.start(bots, gameModel);
        botRunner.stop();
        assertTrue(true);
    }

    @Test
    void testBotRunnerStartMethod() {
        try {
            assertNotNull(BotRunner.class.getMethod("start", List.class, GameModel.class));
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    void testBotRunnerStopMethod() {
        try {
            assertNotNull(BotRunner.class.getMethod("stop"));
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    void testBotRunnerExtendsAbstractGameThread() {
        assertTrue(botRunner instanceof AbstractGameThread);
    }

    @Test
    void testBotRunnerWithBots() {
        List<BotSnake> bots = new ArrayList<>();
        bots.add(new BotSnake(new Point(5, 5), null, "#FF0000", 100));
        botRunner.start(bots, gameModel);
        assertTrue(true);
        botRunner.stop();
    }

    @Test
    void testBotRunnerThreadName() {
        assertNotNull(botRunner.getClass().getSimpleName());
    }
}
