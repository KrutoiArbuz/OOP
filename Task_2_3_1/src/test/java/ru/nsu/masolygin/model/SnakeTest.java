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
    void testAddGrowth() {
        snake.addGrowth(3);
        snake.step();
        assertEquals(2, snake.getLength());
    }

    @Test
    void testStep() {
        int initialLength = snake.getLength();
        snake.step();
        assertEquals(initialLength, snake.getLength());
    }

    @Test
    void testContains() {
        assertTrue(snake.contains(startPosition));
    }
}

