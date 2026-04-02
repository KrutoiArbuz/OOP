package ru.nsu.masolygin.model.bot;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        assertEquals("#FF0000", botSnake.getColorHex());
    }

    @Test
    void testGetSpeed() {
        assertTrue(botSnake.getSpeedMs() > 0);
        assertEquals(200, botSnake.getSpeedMs());
    }

    @Test
    void testBotIsSnake() {
        assertNotNull(botSnake);
        assertEquals(1, botSnake.getLength());
    }

    @Test
    void testBotStep() {
        botSnake.step();
        assertEquals(1, botSnake.getLength());
    }

    @Test
    void testBotGrow() {
        botSnake.addGrowth(3);
        botSnake.step();
        assertEquals(2, botSnake.getLength());
    }

    @Test
    void testDifferentColors() {
        BotSnake blueBot = new BotSnake(new Point(10, 10), null, "#0000FF", 150);
        assertEquals("#0000FF", blueBot.getColorHex());
        assertEquals("#FF0000", botSnake.getColorHex());
    }

    @Test
    void testDifferentSpeeds() {
        BotSnake fastBot = new BotSnake(new Point(10, 10), null, "#00FF00", 100);
        assertEquals(100, fastBot.getSpeedMs());
        assertEquals(200, botSnake.getSpeedMs());
    }

    @Test
    void testKillBotStaysKilled() {
        botSnake.kill();
        assertFalse(botSnake.isAlive());
        botSnake.kill();
        assertFalse(botSnake.isAlive());
    }

    @Test
    void testBotBody() {
        assertNotNull(botSnake.getBody());
        assertTrue(botSnake.getBody().size() > 0);
    }

    @Test
    void testBotContains() {
        assertTrue(botSnake.contains(startPosition));
    }
}

