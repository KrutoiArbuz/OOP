package ru.nsu.masolygin.model;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class GameStateTest {

    @Test
    void testGameStateRunning() {
        GameState state = GameState.RUNNING;
        assertNotNull(state);
    }

    @Test
    void testGameStatePaused() {
        GameState state = GameState.PAUSED;
        assertNotNull(state);
    }

    @Test
    void testGameStateWon() {
        GameState state = GameState.WON;
        assertNotNull(state);
    }

    @Test
    void testGameStateLost() {
        GameState state = GameState.LOST;
        assertNotNull(state);
    }

    @Test
    void testAllGameStates() {
        assertTrue(GameState.RUNNING != null);
        assertTrue(GameState.PAUSED != null);
        assertTrue(GameState.WON != null);
        assertTrue(GameState.LOST != null);
    }
}

