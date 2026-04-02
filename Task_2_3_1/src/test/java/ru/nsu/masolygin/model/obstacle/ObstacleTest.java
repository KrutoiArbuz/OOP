package ru.nsu.masolygin.model.obstacle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.model.Point;

class ObstacleTest {

    private Obstacle obstacle;
    private Point position;

    @BeforeEach
    void setUp() {
        position = new Point(10, 10);
        obstacle = new Obstacle(position);
    }

    @Test
    void testObstacleCreation() {
        assertNotNull(obstacle);
    }

    @Test
    void testGetPosition() {
        assertEquals(position, obstacle.getPosition());
    }

    @Test
    void testObstacleCoordinates() {
        assertEquals(10, obstacle.getPosition().getX());
        assertEquals(10, obstacle.getPosition().getY());
    }

    @Test
    void testMultipleObstacles() {
        Obstacle obs1 = new Obstacle(new Point(5, 5));
        Obstacle obs2 = new Obstacle(new Point(15, 15));
        assertEquals(5, obs1.getPosition().getX());
        assertEquals(15, obs2.getPosition().getX());
    }

    @Test
    void testObstacleWithNegativeCoordinates() {
        Obstacle obs = new Obstacle(new Point(-5, -10));
        assertEquals(-5, obs.getPosition().getX());
        assertEquals(-10, obs.getPosition().getY());
    }

    @Test
    void testObstacleToString() {
        assertNotNull(obstacle.toString());
    }
}

