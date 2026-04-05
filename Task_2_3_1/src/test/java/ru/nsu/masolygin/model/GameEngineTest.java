package ru.nsu.masolygin.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.config.SnakeConfig;

class GameEngineTest {

    private GameEngine engine;
    private GameModel model;
    private SnakeConfig config;

    @BeforeEach
    void setUp() {
        config = SnakeConfig.defaults();
        model = new GameModel(config);
        engine = new GameEngine(model, config);
        engine.reset();
    }

    @Test
    void testCreation() {
        assertNotNull(engine);
    }

    @Test
    void testGetModel() {
        assertNotNull(engine.getModel());
        assertEquals(model, engine.getModel());
    }

    @Test
    void testGetState() {
        assertNotNull(engine.getState());
        assertEquals(GameState.RUNNING, engine.getState());
    }

    @Test
    void testGetSpeedMs() {
        assertTrue(engine.getSpeedMs() > 0);
    }

    @Test
    void testReset() {
        engine.reset();
        assertEquals(GameState.RUNNING, engine.getState());
        assertNotNull(model.playerSnake);
        assertTrue(model.foods.size() > 0);
    }

    @Test
    void testResetMultipleTimes() {
        engine.reset();
        engine.reset();
        engine.reset();
        assertEquals(GameState.RUNNING, engine.getState());
    }

    @Test
    void testExecutePlayerStep() {
        engine.reset();
        GameState initialState = engine.getState();
        engine.executePlayerStep();
        assertEquals(initialState, engine.getState());
    }

    @Test
    void testPlayerSnakeExists() {
        engine.reset();
        assertNotNull(model.playerSnake);
    }

    @Test
    void testBotSnakesInitialized() {
        engine.reset();
        assertNotNull(model.botSnakes);
    }

    @Test
    void testFoodsSpawned() {
        engine.reset();
        assertTrue(model.foods.size() > 0);
    }

    @Test
    void testObstaclesLoaded() {
        engine.reset();
        assertNotNull(model.obstacles);
    }
}

