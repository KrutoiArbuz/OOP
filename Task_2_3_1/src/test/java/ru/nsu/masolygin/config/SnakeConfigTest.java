package ru.nsu.masolygin.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SnakeConfigTest {

    private SnakeConfig config;

    @BeforeEach
    void setUp() {
        config = new SnakeConfig();
    }

    @Test
    void testConfigCreation() {
        assertNotNull(config);
    }

    @Test
    void testGetFieldWidth() {
        assertEquals(25, config.getFieldWidth());
    }

    @Test
    void testSetFieldWidth() {
        config.setFieldWidth(30);
        assertEquals(30, config.getFieldWidth());
    }

    @Test
    void testGetFieldHeight() {
        assertEquals(25, config.getFieldHeight());
    }

    @Test
    void testSetFieldHeight() {
        config.setFieldHeight(35);
        assertEquals(35, config.getFieldHeight());
    }

    @Test
    void testGetCellSize() {
        assertEquals(26, config.getCellSize());
    }

    @Test
    void testGetFoodCount() {
        assertEquals(3, config.getFoodCount());
    }

    @Test
    void testSetFoodCount() {
        config.setFoodCount(5);
        assertEquals(5, config.getFoodCount());
    }

    @Test
    void testGetWinLength() {
        assertEquals(20, config.getWinLength());
    }

    @Test
    void testSetWinLength() {
        config.setWinLength(50);
        assertEquals(50, config.getWinLength());
    }

    @Test
    void testGetInitialSpeedMs() {
        assertTrue(config.getInitialSpeedMs() > 0);
    }

    @Test
    void testSetInitialSpeedMs() {
        config.setInitialSpeedMs(150);
        assertEquals(150, config.getInitialSpeedMs());
    }

    @Test
    void testGetObstacles() {
        assertNotNull(config.getObstacles());
    }

    @Test
    void testGetBots() {
        assertNotNull(config.getBots());
    }

    @Test
    void testSetObstacles() {
        assertNotNull(config.getObstacles());
    }

    @Test
    void testSetBots() {
        assertNotNull(config.getBots());
    }

    @Test
    void testMultipleFieldWidthChanges() {
        config.setFieldWidth(30);
        assertEquals(30, config.getFieldWidth());
        config.setFieldWidth(40);
        assertEquals(40, config.getFieldWidth());
    }

    @Test
    void testMultipleFieldHeightChanges() {
        config.setFieldHeight(35);
        assertEquals(35, config.getFieldHeight());
        config.setFieldHeight(45);
        assertEquals(45, config.getFieldHeight());
    }

    @Test
    void testDefaultValues() {
        SnakeConfig defaultConfig = new SnakeConfig();
        assertEquals(25, defaultConfig.getFieldWidth());
        assertEquals(25, defaultConfig.getFieldHeight());
        assertEquals(26, defaultConfig.getCellSize());
        assertEquals(3, defaultConfig.getFoodCount());
        assertEquals(20, defaultConfig.getWinLength());
    }
}

