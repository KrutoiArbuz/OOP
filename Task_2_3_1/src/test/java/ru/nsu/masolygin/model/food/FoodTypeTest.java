package ru.nsu.masolygin.model.food;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class FoodTypeTest {

    @Test
    void testApple() {
        assertNotNull(FoodType.APPLE);
        assertTrue(FoodType.APPLE.getGrowAmount() > 0);
    }

    @Test
    void testCherry() {
        assertNotNull(FoodType.CHERRY);
        assertTrue(FoodType.CHERRY.getGrowAmount() > 0);
    }

    @Test
    void testGoldenApple() {
        assertNotNull(FoodType.GOLDEN_APPLE);
        assertTrue(FoodType.GOLDEN_APPLE.getGrowAmount() > 0);
    }

    @Test
    void testMushroom() {
        assertNotNull(FoodType.MUSHROOM);
        assertEquals(0, FoodType.MUSHROOM.getGrowAmount());
    }

    @Test
    void testAllFourTypesExist() {
        assertEquals(4, FoodType.values().length);
    }

    @Test
    void testGrowAmounts() {
        assertEquals(1, FoodType.APPLE.getGrowAmount());
        assertEquals(2, FoodType.CHERRY.getGrowAmount());
        assertEquals(3, FoodType.GOLDEN_APPLE.getGrowAmount());
        assertEquals(0, FoodType.MUSHROOM.getGrowAmount());
    }

    @Test
    void testSpeedDeltas() {
        assertEquals(0, FoodType.APPLE.getSpeedDeltaMs());
        assertEquals(0, FoodType.CHERRY.getSpeedDeltaMs());
        assertTrue(FoodType.GOLDEN_APPLE.getSpeedDeltaMs() < 0);
        assertTrue(FoodType.MUSHROOM.getSpeedDeltaMs() > 0);
    }

    @Test
    void testWeights() {
        assertTrue(FoodType.APPLE.getWeight() > 0);
        assertTrue(FoodType.CHERRY.getWeight() > 0);
        assertTrue(FoodType.GOLDEN_APPLE.getWeight() > 0);
        assertTrue(FoodType.MUSHROOM.getWeight() > 0);
    }

    @Test
    void testValueOf() {
        assertEquals(FoodType.APPLE, FoodType.valueOf("APPLE"));
        assertEquals(FoodType.CHERRY, FoodType.valueOf("CHERRY"));
        assertEquals(FoodType.GOLDEN_APPLE, FoodType.valueOf("GOLDEN_APPLE"));
        assertEquals(FoodType.MUSHROOM, FoodType.valueOf("MUSHROOM"));
    }
}
