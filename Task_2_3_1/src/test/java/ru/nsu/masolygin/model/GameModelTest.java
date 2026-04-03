package ru.nsu.masolygin.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.config.SnakeConfig;

class GameModelTest {

    private GameModel gameModel;
    private SnakeConfig config;

    @BeforeEach
    void setUp() {
        config = new SnakeConfig();
        gameModel = new GameModel(config);
    }

    @Test
    void testGameModelCreation() {
        assertNotNull(gameModel);
    }

    @Test
    void testGetWidth() {
        assertEquals(25, gameModel.getWidth());
    }

    @Test
    void testGetHeight() {
        assertEquals(25, gameModel.getHeight());
    }

    @Test
    void testGetState() {
        assertEquals(GameState.RUNNING, gameModel.getState());
    }

    @Test
    void testGetFoods() {
        assertNotNull(gameModel.getFoods());
        assertTrue(gameModel.getFoods().size() > 0);
    }

    @Test
    void testGetObstacles() {
        assertNotNull(gameModel.getObstacles());
    }

    @Test
    void testGetBotSnakes() {
        assertNotNull(gameModel.getBotSnakes());
    }

    @Test
    void testGetSpeedMs() {
        assertTrue(gameModel.getSpeedMs() > 0);
    }

    @Test
    void testSetDirection() {
        gameModel.setDirection(Direction.UP);
    }

    @Test
    void testTogglePause() {
        assertEquals(GameState.RUNNING, gameModel.getState());
        gameModel.togglePause();
        assertEquals(GameState.PAUSED, gameModel.getState());
        gameModel.togglePause();
        assertEquals(GameState.RUNNING, gameModel.getState());
    }

    @Test
    void testReset() {
        gameModel.reset();
        assertEquals(GameState.RUNNING, gameModel.getState());
        assertNotNull(gameModel.getFoods());
    }

    @Test
    void testIsWalkableOutOfBounds() {
        assertFalse(gameModel.isWalkable(new Point(-1, 0)));
        assertFalse(gameModel.isWalkable(new Point(25, 0)));
        assertFalse(gameModel.isWalkable(new Point(0, 25)));
    }

    @Test
    void testExecutePlayerStep() {
        gameModel.setDirection(Direction.UP);
        gameModel.executePlayerStep();
        assertEquals(GameState.RUNNING, gameModel.getState());
    }

    @Test
    void testExecutePlayerStepPaused() {
        gameModel.togglePause();
        gameModel.setDirection(Direction.UP);
        gameModel.executePlayerStep();
        assertEquals(GameState.PAUSED, gameModel.getState());
    }

    @Test
    void testGetLatestSnapshot() {
        GameSnapshot snapshot = gameModel.getLatestSnapshot();
        assertNotNull(snapshot);
        assertNotNull(snapshot.playerBody());
        assertTrue(snapshot.playerBody().size() > 0);
    }

    @Test
    void testExecuteBotStep() {
        if (!gameModel.getBotSnakes().isEmpty()) {
            ru.nsu.masolygin.model.bot.BotSnake bot = gameModel.getBotSnakes().get(0);
            gameModel.executeBotStep(bot, Direction.UP);
        }
    }

    @Test
    void testExecuteBotStepDeadBot() {
        if (!gameModel.getBotSnakes().isEmpty()) {
            ru.nsu.masolygin.model.bot.BotSnake bot = gameModel.getBotSnakes().get(0);
            bot.kill();
            gameModel.executeBotStep(bot, Direction.UP);
            assertFalse(bot.isAlive());
        }
    }

    @Test
    void testMultipleSteps() {
        for (int i = 0; i < 10; i++) {
            gameModel.executePlayerStep();
        }
    }

    @Test
    void testSetMultipleDirections() {
        gameModel.setDirection(Direction.UP);
        gameModel.setDirection(Direction.RIGHT);
        gameModel.setDirection(Direction.DOWN);
        gameModel.setDirection(Direction.LEFT);
    }

    @Test
    void testExecutePlayerStepMultipleTimes() {
        for (int i = 0; i < 20; i++) {
            gameModel.executePlayerStep();
        }
    }

    @Test
    void testExecuteBotStepWithDifferentDirections() {
        if (!gameModel.getBotSnakes().isEmpty()) {
            ru.nsu.masolygin.model.bot.BotSnake bot = gameModel.getBotSnakes().get(0);
            gameModel.executeBotStep(bot, Direction.UP);
            gameModel.executeBotStep(bot, Direction.DOWN);
            gameModel.executeBotStep(bot, Direction.LEFT);
            gameModel.executeBotStep(bot, Direction.RIGHT);
        }
    }

    @Test
    void testIsWalkableDifferentPoints() {
        assertTrue(gameModel.isWalkable(new Point(5, 5)));
        assertTrue(gameModel.isWalkable(new Point(10, 10)));
        assertTrue(gameModel.isWalkable(new Point(24, 24)));
    }

    @Test
    void testResetMultipleTimes() {
        gameModel.reset();
        gameModel.executePlayerStep();
        gameModel.reset();
        assertEquals(GameState.RUNNING, gameModel.getState());
    }

    @Test
    void testTogglePauseMultipleTimes() {
        for (int i = 0; i < 4; i++) {
            gameModel.togglePause();
        }
        assertEquals(GameState.RUNNING, gameModel.getState());
    }

    @Test
    void testExecutePlayerStepWithDirectionChanges() {
        gameModel.setDirection(Direction.UP);
        gameModel.executePlayerStep();
        gameModel.setDirection(Direction.RIGHT);
        gameModel.executePlayerStep();
        gameModel.setDirection(Direction.DOWN);
        gameModel.executePlayerStep();
    }

    @Test
    void testGameModelSnapshot() {
        GameSnapshot snapshot = gameModel.getLatestSnapshot();
        assertNotNull(snapshot.playerBody());
        assertNotNull(snapshot.foods());
        assertNotNull(snapshot.bots());
        assertNotNull(snapshot.obstacles());
    }

    @Test
    void testBotSnakesListNotNull() {
        assertNotNull(gameModel.getBotSnakes());
    }

    @Test
    void testFoodsListNotNull() {
        assertNotNull(gameModel.getFoods());
    }

    @Test
    void testObstaclesListNotNull() {
        assertNotNull(gameModel.getObstacles());
    }
}

