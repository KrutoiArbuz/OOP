package ru.nsu.masolygin.model.food;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class FoodTypeTest {

    @Test
    void testFoodTypeApple() {
        FoodType type = FoodType.APPLE;
        assertNotNull(type);
    }

    @Test
    void testFoodTypeCherry() {
        FoodType type = FoodType.CHERRY;
        assertNotNull(type);
    }

    @Test
    void testFoodTypeGoldenApple() {
        FoodType type = FoodType.GOLDEN_APPLE;
        assertNotNull(type);
    }

    @Test
    void testFoodTypeMushroom() {
        FoodType type = FoodType.MUSHROOM;
        assertNotNull(type);
    }

    @Test
    void testAllFoodTypes() {
        assertTrue(FoodType.APPLE != null);
        assertTrue(FoodType.CHERRY != null);
        assertTrue(FoodType.GOLDEN_APPLE != null);
        assertTrue(FoodType.MUSHROOM != null);
    }
}

