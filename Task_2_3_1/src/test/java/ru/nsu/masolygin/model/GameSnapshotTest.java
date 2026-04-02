package ru.nsu.masolygin.model;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.model.food.Food;
import ru.nsu.masolygin.model.obstacle.Obstacle;

class GameSnapshotTest {

    private GameSnapshot snapshot;

    @BeforeEach
    void setUp() {
        List<Point> playerBody = new ArrayList<>();
        playerBody.add(new Point(10, 10));
        List<GameSnapshot.BotSnapshot> bots = new ArrayList<>();
        List<Food> foods = new ArrayList<>();
        List<Obstacle> obstacles = new ArrayList<>();

        snapshot = new GameSnapshot(playerBody, bots, foods, obstacles, GameState.RUNNING, 1, 20,
            200, 0);
    }

    @Test
    void testGameSnapshotCreation() {
        assertNotNull(snapshot);
    }

    @Test
    void testGetPlayerBody() {
        assertNotNull(snapshot.playerBody());
        assertTrue(snapshot.playerBody().size() > 0);
    }

    @Test
    void testGetGameState() {
        assertTrue(snapshot.state() == GameState.RUNNING);
    }

    @Test
    void testGetPlayerLength() {
        assertTrue(snapshot.playerLength() == 1);
    }

    @Test
    void testGetSpeed() {
        assertTrue(snapshot.speedMs() > 0);
    }
}

