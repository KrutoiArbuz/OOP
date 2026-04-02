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
}

