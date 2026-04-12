package ru.nsu.masolygin.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.config.SnakeConfig;
import ru.nsu.masolygin.model.bot.BotSnake;

class GameModelTest {

    private GameModel model;
    private GameEngine engine;

    @BeforeEach
    void setUp() {
        SnakeConfig config = SnakeConfig.defaults();
        model = new GameModel(config);
        engine = new GameEngine(model, config);
        engine.reset();
    }

    @Test
    void testCreation() {
        assertNotNull(model);
    }

    @Test
    void testGetWidth() {
        assertEquals(25, model.getWidth());
    }

    @Test
    void testGetHeight() {
        assertEquals(25, model.getHeight());
    }

    @Test
    void testInitialState() {
        assertEquals(GameState.RUNNING, model.getState());
    }

    @Test
    void testGetFoods() {
        assertNotNull(model.getFoods());
        assertFalse(model.getFoods().isEmpty());
    }

    @Test
    void testFoodsIsCopyOnWriteArrayList() throws Exception {
        java.lang.reflect.Field field = GameModel.class.getDeclaredField("foods");
        field.setAccessible(true);
        Object foodsObj = field.get(model);
        assertTrue(foodsObj instanceof java.util.concurrent.CopyOnWriteArrayList);
    }

    @Test
    void testGetObstacles() {
        assertNotNull(model.getObstacles());
    }

    @Test
    void testGetBotSnakes() {
        assertNotNull(model.getBotSnakes());
    }

    @Test
    void testGetSpeedMs() {
        assertTrue(model.getSpeedMs() > 0);
    }

    @Test
    void testSetDirection() {
        model.setDirection(Direction.UP);
    }

    @Test
    void testTogglePause() {
        assertEquals(GameState.RUNNING, model.getState());
        model.togglePause();
        assertEquals(GameState.PAUSED, model.getState());
        model.togglePause();
        assertEquals(GameState.RUNNING, model.getState());
    }

    @Test
    void testReset() {
        engine.reset();
        assertEquals(GameState.RUNNING, model.getState());
        assertFalse(model.getFoods().isEmpty());
    }

    @Test
    void testIsWalkableOutOfBounds() {
        assertFalse(model.isWalkable(new Point(-1, 0)));
        assertFalse(model.isWalkable(new Point(25, 0)));
        assertFalse(model.isWalkable(new Point(0, 25)));
    }

    @Test
    void testIsWalkableFreeCell() {
        assertTrue(model.isWalkable(new Point(5, 5)));
        assertTrue(model.isWalkable(new Point(10, 10)));
        assertTrue(model.isWalkable(new Point(24, 24)));
    }

    @Test
    void testExecutePlayerStep() {
        model.setDirection(Direction.UP);
        engine.executePlayerStep();
        assertEquals(GameState.RUNNING, model.getState());
    }

    @Test
    void testExecutePlayerStepPaused() {
        model.togglePause();
        model.setDirection(Direction.UP);
        engine.executePlayerStep();
        assertEquals(GameState.PAUSED, model.getState());
    }

    @Test
    void testGetLatestSnapshot() {
        GameSnapshot snapshot = model.getLatestSnapshot();
        assertNotNull(snapshot);
        assertFalse(snapshot.playerBody().isEmpty());
    }

    @Test
    void testExecuteBotStep() {
        if (!model.getBotSnakes().isEmpty()) {
            BotSnake bot = model.getBotSnakes().get(0);
            engine.executeBotStep(bot, Direction.UP);
        }
    }

    @Test
    void testExecuteBotStepDeadBot() {
        if (!model.getBotSnakes().isEmpty()) {
            BotSnake bot = model.getBotSnakes().get(0);
            bot.kill();
            engine.executeBotStep(bot, Direction.UP);
            assertFalse(bot.isAlive());
        }
    }

    @Test
    void testMultiplePlayerSteps() {
        for (int i = 0; i < 10; i++) {
            engine.executePlayerStep();
        }
    }

    @Test
    void testSetMultipleDirections() {
        model.setDirection(Direction.UP);
        model.setDirection(Direction.RIGHT);
        model.setDirection(Direction.DOWN);
        model.setDirection(Direction.LEFT);
    }

    @Test
    void testResetMultipleTimes() {
        engine.reset();
        engine.executePlayerStep();
        engine.reset();
        assertEquals(GameState.RUNNING, model.getState());
    }

    @Test
    void testTogglePauseMultipleTimes() {
        for (int i = 0; i < 4; i++) {
            model.togglePause();
        }
        assertEquals(GameState.RUNNING, model.getState());
    }

    @Test
    void testSnapshot() {
        GameSnapshot snapshot = model.getLatestSnapshot();
        assertNotNull(snapshot.playerBody());
        assertNotNull(snapshot.foods());
        assertNotNull(snapshot.bots());
        assertNotNull(snapshot.obstacles());
    }
}
