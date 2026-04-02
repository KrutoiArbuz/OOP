package ru.nsu.masolygin.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SnakeTest {

    private Snake snake;
    private Point startPosition;

    @BeforeEach
    void setUp() {
        startPosition = new Point(10, 10);
        snake = new Snake(startPosition);
    }

    @Test
    void testSnakeCreation() {
        assertNotNull(snake);
    }

    @Test
    void testGetHead() {
        assertEquals(startPosition, snake.getHead());
    }

    @Test
    void testGetLength() {
        assertEquals(1, snake.getLength());
    }

    @Test
    void testGetDirection() {
        assertEquals(Direction.RIGHT, snake.getDirection());
    }

    @Test
    void testSetDirection() {
        snake.setDirection(Direction.UP);
        assertEquals(Direction.UP, snake.getDirection());
    }

    @Test
    void testSetOppositeDirection() {
        assertEquals(Direction.RIGHT, snake.getDirection());
        snake.setDirection(Direction.LEFT);
        assertEquals(Direction.RIGHT, snake.getDirection());
    }

    @Test
    void testGetBody() {
        assertNotNull(snake.getBody());
        assertEquals(1, snake.getBody().size());
    }

    @Test
    void testNextHeadPosition() {
        Point nextPos = snake.nextHeadPosition();
        assertEquals(11, nextPos.getX());
        assertEquals(10, nextPos.getY());
    }

    @Test
    void testNextHeadPositionUp() {
        snake.setDirection(Direction.UP);
        Point nextPos = snake.nextHeadPosition();
        assertEquals(10, nextPos.getX());
        assertEquals(9, nextPos.getY());
    }

    @Test
    void testNextHeadPositionDown() {
        snake.setDirection(Direction.DOWN);
        Point nextPos = snake.nextHeadPosition();
        assertEquals(10, nextPos.getX());
        assertEquals(11, nextPos.getY());
    }

    @Test
    void testAddGrowth() {
        snake.addGrowth(3);
        snake.step();
        assertEquals(2, snake.getLength());
    }

    @Test
    void testAddGrowthMultiple() {
        snake.addGrowth(5);
        for (int i = 0; i < 5; i++) {
            snake.step();
        }
        assertEquals(6, snake.getLength());
    }

    @Test
    void testStep() {
        int initialLength = snake.getLength();
        snake.step();
        assertEquals(initialLength, snake.getLength());
    }

    @Test
    void testStepMultiple() {
        snake.step();
        snake.step();
        snake.step();
        assertEquals(1, snake.getLength());
    }

    @Test
    void testContains() {
        assertTrue(snake.contains(startPosition));
    }

    @Test
    void testContainsAfterStep() {
        snake.addGrowth(1);
        snake.step();
        assertTrue(snake.getBody().size() >= 1);
    }

    @Test
    void testDirectionSequence() {
        assertEquals(Direction.RIGHT, snake.getDirection());
        snake.setDirection(Direction.UP);
        assertEquals(Direction.UP, snake.getDirection());
        snake.setDirection(Direction.LEFT);
        assertEquals(Direction.LEFT, snake.getDirection());
        snake.setDirection(Direction.DOWN);
        assertEquals(Direction.DOWN, snake.getDirection());
    }

    @Test
    void testBodyOrder() {
        assertNotNull(snake.getBody());
        assertEquals(snake.getHead(), snake.getBody().get(0));
    }

    @Test
    void testNegativeGrowth() {
        snake.addGrowth(-1);
        snake.step();
        assertEquals(1, snake.getLength());
    }

    @Test
    void testZeroGrowth() {
        snake.addGrowth(0);
        snake.step();
        assertEquals(1, snake.getLength());
    }
}

