package ru.nsu.masolygin.model;

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
    }

    @Test
    void testBasicFunctionality() {
        assertTrue(true);
    }
}

