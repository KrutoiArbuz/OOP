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
}

