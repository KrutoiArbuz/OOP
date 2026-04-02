package ru.nsu.masolygin.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
}

