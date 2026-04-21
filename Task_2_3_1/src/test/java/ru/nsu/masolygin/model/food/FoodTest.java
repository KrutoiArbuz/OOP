package ru.nsu.masolygin.model.food;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.model.Point;

class FoodTest {

    private Food food;
    private Point position;

    @BeforeEach
    void setUp() {
        position = new Point(5, 5);
        food = new Food(position, FoodType.APPLE);
    }

    @Test
    void testFoodCreation() {
        assertNotNull(food);
    }

    @Test
    void testGetPosition() {
        assertEquals(position, food.getPosition());
    }

    @Test
    void testGetType() {
        assertEquals(FoodType.APPLE, food.getType());
    }

    @Test
    void testFoodCoordinates() {
        assertEquals(5, food.getPosition().getX());
        assertEquals(5, food.getPosition().getY());
    }

    @Test
    void testFoodCherry() {
        Food cherry = new Food(new Point(10, 10), FoodType.CHERRY);
        assertEquals(FoodType.CHERRY, cherry.getType());
    }

    @Test
    void testFoodGoldenApple() {
        Food golden = new Food(new Point(15, 15), FoodType.GOLDEN_APPLE);
        assertEquals(FoodType.GOLDEN_APPLE, golden.getType());
    }

    @Test
    void testFoodMushroom() {
        Food mushroom = new Food(new Point(20, 20), FoodType.MUSHROOM);
        assertEquals(FoodType.MUSHROOM, mushroom.getType());
    }

    @Test
    void testFoodToString() {
        assertNotNull(food.toString());
    }

    @Test
    void testMultipleFoodInstances() {
        Food food1 = new Food(new Point(1, 1), FoodType.APPLE);
        Food food2 = new Food(new Point(2, 2), FoodType.CHERRY);
        assertNotNull(food1);
        assertNotNull(food2);
    }
}

