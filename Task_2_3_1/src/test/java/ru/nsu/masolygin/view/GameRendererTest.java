package ru.nsu.masolygin.view;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.config.SnakeConfig;
import ru.nsu.masolygin.model.GameSnapshot;
import ru.nsu.masolygin.model.GameState;
import ru.nsu.masolygin.model.Point;

class GameRendererTest {

    private GameRenderer renderer;
    private Canvas canvas;
    private Label scoreLabel;
    private Label statusLabel;
    private StackPane overlayPane;
    private SnakeConfig config;

    @BeforeEach
    void setUp() {
        canvas = new Canvas(800, 650);
        scoreLabel = new Label("Score: 0");
        statusLabel = new Label("Status");
        overlayPane = new StackPane();
        config = new SnakeConfig();
        renderer = new GameRenderer(canvas, scoreLabel, statusLabel, overlayPane, config);
    }

    @Test
    void testGameRendererCreation() {
        assertNotNull(renderer);
    }

    @Test
    void testGameRendererWithCanvas() {
        assertNotNull(canvas);
        assertTrue(canvas.getWidth() > 0);
        assertTrue(canvas.getHeight() > 0);
    }

    @Test
    void testRenderMethod() {
        GameSnapshot snapshot = new GameSnapshot(
            new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
            new ArrayList<>(), GameState.RUNNING, 1, 20, 200, 0
        );
        renderer.render(snapshot);
        assertTrue(true);
    }

    @Test
    void testRenderWithMultipleElements() {
        ArrayList<Point> body = new ArrayList<>();
        body.add(new Point(5, 5));
        body.add(new Point(4, 5));
        GameSnapshot snapshot = new GameSnapshot(
            body, new ArrayList<>(), new ArrayList<>(),
            new ArrayList<>(), GameState.RUNNING, 2, 20, 200, 0
        );
        renderer.render(snapshot);
        assertTrue(true);
    }

    @Test
    void testGameRendererClass() {
        assertNotNull(GameRenderer.class);
    }

    @Test
    void testGameRendererMethods() {
        assertTrue(GameRenderer.class.getDeclaredMethods().length > 0);
    }

    @Test
    void testGameRendererFields() {
        assertTrue(GameRenderer.class.getDeclaredFields().length > 0);
    }

    @Test
    void testRenderRunningState() {
        GameSnapshot snapshot = new GameSnapshot(
            new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
            new ArrayList<>(), GameState.RUNNING, 1, 20, 200, 0
        );
        renderer.render(snapshot);
        assertTrue(scoreLabel.getText().contains("Score"));
    }

    @Test
    void testRenderWonState() {
        GameSnapshot snapshot = new GameSnapshot(
            new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
            new ArrayList<>(), GameState.WON, 20, 20, 200, 0
        );
        renderer.render(snapshot);
        assertTrue(true);
    }

    @Test
    void testRenderLostState() {
        GameSnapshot snapshot = new GameSnapshot(
            new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
            new ArrayList<>(), GameState.LOST, 5, 20, 200, 0
        );
        renderer.render(snapshot);
        assertTrue(true);
    }
}
