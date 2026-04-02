package ru.nsu.masolygin.model.bot;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.model.Point;

class BotSnakeTest {

    private BotSnake botSnake;
    private Point startPosition;

    @BeforeEach
    void setUp() {
        startPosition = new Point(15, 15);
        botSnake = new BotSnake(startPosition, null, "#FF0000", 200);
    }

    @Test
    void testBotSnakeCreation() {
        assertNotNull(botSnake);
    }

    @Test
    void testGetHeadPosition() {
        assertTrue(botSnake.getHead().equals(startPosition));
    }

    @Test
    void testBotInitiallyAlive() {
        assertTrue(botSnake.isAlive());
    }

    @Test
    void testKillBot() {
        botSnake.kill();
        assertFalse(botSnake.isAlive());
    }

    @Test
    void testGetColor() {
        assertNotNull(botSnake.getColorHex());
    }

    @Test
    void testGetSpeed() {
        assertTrue(botSnake.getSpeedMs() > 0);
    }
}

