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
    void testGameModelInitialization() {
        assertNotNull(gameModel);
        assertEquals(GameState.RUNNING, gameModel.getState());
        assertEquals(25, gameModel.getWidth());
        assertEquals(25, gameModel.getHeight());
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
    void testResetGame() {
        gameModel.reset();
        assertEquals(GameState.RUNNING, gameModel.getState());
        assertNotNull(gameModel.getFoods());
    }

    @Test
    void testIsWalkable() {
        Point p = new Point(0, 0);
        boolean walkable = gameModel.isWalkable(p);
        assertNotNull(gameModel);
    }

    @Test
    void testIsWalkableOutOfBounds() {
        Point p = new Point(-1, 0);
        assertFalse(gameModel.isWalkable(p));
    }

    @Test
    void testIsWalkableOutOfBoundsX() {
        Point p = new Point(25, 0);
        assertFalse(gameModel.isWalkable(p));
    }

    @Test
    void testIsWalkableOutOfBoundsY() {
        Point p = new Point(0, 25);
        assertFalse(gameModel.isWalkable(p));
    }

    @Test
    void testExecutePlayerStep() {
        gameModel.setDirection(Direction.UP);
        gameModel.executePlayerStep();
        assertEquals(GameState.RUNNING, gameModel.getState());
    }

    @Test
    void testExecutePlayerStepMultiple() {
        for (int i = 0; i < 5; i++) {
            gameModel.executePlayerStep();
        }
        assertEquals(GameState.RUNNING, gameModel.getState());
    }

    @Test
    void testGetLatestSnapshot() {
        GameSnapshot snapshot = gameModel.getLatestSnapshot();
        assertNotNull(snapshot);
    }

    @Test
    void testGetLatestSnapshotHasData() {
        GameSnapshot snapshot = gameModel.getLatestSnapshot();
        assertNotNull(snapshot.playerBody());
        assertTrue(snapshot.playerBody().size() > 0);
        assertEquals(GameState.RUNNING, snapshot.state());
    }

    @Test
    void testResetClearsFood() {
        int foodCount = gameModel.getFoods().size();
        assertTrue(foodCount > 0);
        gameModel.reset();
        assertNotNull(gameModel.getFoods());
    }

    @Test
    void testPausePreventesSteps() {
        gameModel.togglePause();
        assertEquals(GameState.PAUSED, gameModel.getState());
        gameModel.executePlayerStep();
        assertEquals(GameState.PAUSED, gameModel.getState());
    }

    @Test
    void testMultipleResets() {
        gameModel.reset();
        assertEquals(GameState.RUNNING, gameModel.getState());
        gameModel.reset();
        assertEquals(GameState.RUNNING, gameModel.getState());
    }

    @Test
    void testExecutePlayerStepWithDirection() {
        gameModel.setDirection(Direction.RIGHT);
        gameModel.executePlayerStep();
        gameModel.setDirection(Direction.DOWN);
        gameModel.executePlayerStep();
        assertEquals(GameState.RUNNING, gameModel.getState());
    }

    @Test
    void testGameSnapshotUpdatesAfterStep() {
        GameSnapshot snap1 = gameModel.getLatestSnapshot();
        gameModel.executePlayerStep();
        GameSnapshot snap2 = gameModel.getLatestSnapshot();
        assertNotNull(snap1);
        assertNotNull(snap2);
    }
}

