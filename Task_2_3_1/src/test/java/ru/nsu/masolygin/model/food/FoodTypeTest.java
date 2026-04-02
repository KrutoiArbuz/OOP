package ru.nsu.masolygin.model.food;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class FoodTypeTest {

    @Test
    void testFoodTypeApple() {
        FoodType type = FoodType.APPLE;
        assertNotNull(type);
        assertTrue(type.getGrowAmount() > 0);
    }

    @Test
    void testFoodTypeCherry() {
        FoodType type = FoodType.CHERRY;
        assertNotNull(type);
        assertTrue(type.getGrowAmount() > 0);
    }

    @Test
    void testFoodTypeGoldenApple() {
        FoodType type = FoodType.GOLDEN_APPLE;
        assertNotNull(type);
        assertTrue(type.getGrowAmount() > 0);
    }

    @Test
    void testFoodTypeMushroom() {
        FoodType type = FoodType.MUSHROOM;
        assertNotNull(type);
        assertEquals(0, type.getGrowAmount());
    }

    @Test
    void testAllFoodTypes() {
        assertTrue(FoodType.APPLE != null);
        assertTrue(FoodType.CHERRY != null);
        assertTrue(FoodType.GOLDEN_APPLE != null);
        assertTrue(FoodType.MUSHROOM != null);
    }

    @Test
    void testFoodTypeValues() {
        FoodType[] types = FoodType.values();
        assertEquals(4, types.length);
    }

    @Test
    void testFoodTypeGetGrowAmount() {
        assertEquals(1, FoodType.APPLE.getGrowAmount());
        assertEquals(2, FoodType.CHERRY.getGrowAmount());
        assertEquals(3, FoodType.GOLDEN_APPLE.getGrowAmount());
        assertEquals(0, FoodType.MUSHROOM.getGrowAmount());
    }

    @Test
    void testFoodTypeGetSpeedDelta() {
        assertEquals(0, FoodType.APPLE.getSpeedDeltaMs());
        assertEquals(0, FoodType.CHERRY.getSpeedDeltaMs());
        assertTrue(FoodType.GOLDEN_APPLE.getSpeedDeltaMs() < 0);
        assertTrue(FoodType.MUSHROOM.getSpeedDeltaMs() > 0);
    }

    @Test
    void testFoodTypeGetColorHex() {
        assertNotNull(FoodType.APPLE.getColorHex());
        assertNotNull(FoodType.CHERRY.getColorHex());
        assertNotNull(FoodType.GOLDEN_APPLE.getColorHex());
        assertNotNull(FoodType.MUSHROOM.getColorHex());
    }

    @Test
    void testFoodTypeGetDisplayName() {
        assertNotNull(FoodType.APPLE.getDisplayName());
        assertNotNull(FoodType.CHERRY.getDisplayName());
        assertNotNull(FoodType.GOLDEN_APPLE.getDisplayName());
        assertNotNull(FoodType.MUSHROOM.getDisplayName());
    }

    @Test
    void testFoodTypeGetWeight() {
        assertTrue(FoodType.APPLE.getWeight() > 0);
        assertTrue(FoodType.CHERRY.getWeight() > 0);
        assertTrue(FoodType.GOLDEN_APPLE.getWeight() > 0);
        assertTrue(FoodType.MUSHROOM.getWeight() > 0);
    }

    @Test
    void testFoodTypeValueOf() {
        assertEquals(FoodType.APPLE, FoodType.valueOf("APPLE"));
        assertEquals(FoodType.CHERRY, FoodType.valueOf("CHERRY"));
        assertEquals(FoodType.GOLDEN_APPLE, FoodType.valueOf("GOLDEN_APPLE"));
        assertEquals(FoodType.MUSHROOM, FoodType.valueOf("MUSHROOM"));
    }
}

