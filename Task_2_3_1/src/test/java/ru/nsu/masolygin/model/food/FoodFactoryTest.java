package ru.nsu.masolygin.model.food;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.model.Point;

class FoodFactoryTest {

    @Test
    void testCreateRandomFood() {
        Point position = new Point(5, 5);
        Food food = FoodFactory.createRandom(position);
        assertNotNull(food);
    }

    @Test
    void testCreatedFoodPosition() {
        Point position = new Point(10, 15);
        Food food = FoodFactory.createRandom(position);
        assertNotNull(food.getPosition());
    }

    @Test
    void testCreatedFoodType() {
        Point position = new Point(5, 5);
        Food food = FoodFactory.createRandom(position);
        assertNotNull(food.getType());
    }

    @Test
    void testCreateMultipleFood() {
        for (int i = 0; i < 10; i++) {
            Food food = FoodFactory.createRandom(new Point(i, i));
            assertNotNull(food);
        }
    }
}

