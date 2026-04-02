package ru.nsu.masolygin.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    void testGetWinLength() {
        assertEquals(20, snapshot.winLength());
    }

    @Test
    void testGetBots() {
        assertNotNull(snapshot.bots());
    }

    @Test
    void testGetFoods() {
        assertNotNull(snapshot.foods());
    }

    @Test
    void testGetObstacles() {
        assertNotNull(snapshot.obstacles());
    }

    @Test
    void testGetSpeedEffectTicks() {
        assertEquals(0, snapshot.speedEffectTicks());
    }

    @Test
    void testDifferentStates() {
        GameSnapshot runningSnap = new GameSnapshot(new ArrayList<>(), new ArrayList<>(),
            new ArrayList<>(), new ArrayList<>(), GameState.RUNNING, 1, 20, 200, 0);
        assertEquals(GameState.RUNNING, runningSnap.state());

        GameSnapshot pausedSnap = new GameSnapshot(new ArrayList<>(), new ArrayList<>(),
            new ArrayList<>(), new ArrayList<>(), GameState.PAUSED, 1, 20, 200, 0);
        assertEquals(GameState.PAUSED, pausedSnap.state());

        GameSnapshot wonSnap = new GameSnapshot(new ArrayList<>(), new ArrayList<>(),
            new ArrayList<>(), new ArrayList<>(), GameState.WON, 1, 20, 200, 0);
        assertEquals(GameState.WON, wonSnap.state());

        GameSnapshot lostSnap = new GameSnapshot(new ArrayList<>(), new ArrayList<>(),
            new ArrayList<>(), new ArrayList<>(), GameState.LOST, 1, 20, 200, 0);
        assertEquals(GameState.LOST, lostSnap.state());
    }

    @Test
    void testMultipleBodySegments() {
        List<Point> body = new ArrayList<>();
        body.add(new Point(10, 10));
        body.add(new Point(9, 10));
        body.add(new Point(8, 10));
        GameSnapshot snap = new GameSnapshot(body, new ArrayList<>(),
            new ArrayList<>(), new ArrayList<>(), GameState.RUNNING, 3, 20, 200, 0);
        assertEquals(3, snap.playerLength());
        assertEquals(3, snap.playerBody().size());
    }

    @Test
    void testWithBots() {
        List<GameSnapshot.BotSnapshot> bots = new ArrayList<>();
        bots.add(new GameSnapshot.BotSnapshot(new ArrayList<>(), "#FF0000", true));
        GameSnapshot snap = new GameSnapshot(new ArrayList<>(), bots,
            new ArrayList<>(), new ArrayList<>(), GameState.RUNNING, 1, 20, 200, 0);
        assertEquals(1, snap.bots().size());
    }
}
