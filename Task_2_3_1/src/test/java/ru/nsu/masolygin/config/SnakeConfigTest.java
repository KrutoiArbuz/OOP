package ru.nsu.masolygin.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SnakeConfigTest {

    private SnakeConfig config;

    @BeforeEach
    void setUp() {
        config = SnakeConfig.defaults();
    }

    @Test
    void testConfigCreation() {
        assertNotNull(config);
    }

    @Test
    void testFieldWidth() {
        assertEquals(25, config.fieldWidth());
    }

    @Test
    void testFieldHeight() {
        assertEquals(25, config.fieldHeight());
    }

    @Test
    void testCellSize() {
        assertEquals(26, config.cellSize());
    }

    @Test
    void testFoodCount() {
        assertEquals(3, config.foodCount());
    }

    @Test
    void testWinLength() {
        assertEquals(20, config.winLength());
    }

    @Test
    void testInitialSpeedMs() {
        assertTrue(config.initialSpeedMs() > 0);
    }

    @Test
    void testObstacles() {
        assertNotNull(config.obstacles());
    }

    @Test
    void testBots() {
        assertNotNull(config.bots());
    }

    @Test
    void testCustomConfig() {
        SnakeConfig custom = new SnakeConfig(30, 35, 20, 5, 50, 150, List.of(), List.of());
        assertEquals(30, custom.fieldWidth());
        assertEquals(35, custom.fieldHeight());
        assertEquals(20, custom.cellSize());
        assertEquals(5, custom.foodCount());
        assertEquals(50, custom.winLength());
        assertEquals(150, custom.initialSpeedMs());
    }

    @Test
    void testDefaultValues() {
        SnakeConfig def = SnakeConfig.defaults();
        assertEquals(25, def.fieldWidth());
        assertEquals(25, def.fieldHeight());
        assertEquals(26, def.cellSize());
        assertEquals(3, def.foodCount());
        assertEquals(20, def.winLength());
    }

    @Test
    void testNullObstaclesBecomesEmpty() {
        SnakeConfig c = new SnakeConfig(10, 10, 10, 1, 10, 100, null, null);
        assertNotNull(c.obstacles());
        assertNotNull(c.bots());
        assertEquals(0, c.obstacles().size());
        assertEquals(0, c.bots().size());
    }
}
