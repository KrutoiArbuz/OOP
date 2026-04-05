package ru.nsu.masolygin.controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import ru.nsu.masolygin.config.SnakeConfig;
import ru.nsu.masolygin.engine.BotRunner;
import ru.nsu.masolygin.engine.PlayerRunner;
import ru.nsu.masolygin.engine.RenderLoop;
import ru.nsu.masolygin.model.Direction;
import ru.nsu.masolygin.model.GameEngine;
import ru.nsu.masolygin.model.GameModel;
import ru.nsu.masolygin.view.GameRenderer;

/**
 * Контроллер игрового окна.
 */
public class GameController {

    @FXML
    private Canvas gameCanvas;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private StackPane overlayPane;

    private GameModel model;
    private GameEngine engine;
    private PlayerRunner playerRunner;
    private BotRunner botRunner;
    private RenderLoop renderLoop;

    /**
     * Инициализирует модель, движок, рендер и потоки.
     *
     * @param config конфигурация игры
     */
    public void init(SnakeConfig config) {
        model = new GameModel(config);
        engine = new GameEngine(model, config);
        engine.reset();

        gameCanvas.setWidth((double) config.fieldWidth() * config.cellSize());
        gameCanvas.setHeight((double) config.fieldHeight() * config.cellSize());

        GameRenderer renderer = new GameRenderer(
            gameCanvas, scoreLabel, statusLabel, overlayPane, config);

        botRunner = new BotRunner();
        botRunner.start(model.getBotSnakes(), engine);

        playerRunner = new PlayerRunner(engine);
        playerRunner.start();

        renderLoop = new RenderLoop(model, renderer::render);
        renderLoop.start();
    }

    /**
     * Останавливает потоки игры.
     */
    public void cleanup() {
        if (playerRunner != null) {
            playerRunner.stop();
        }
        if (botRunner != null) {
            botRunner.stop();
        }
        if (renderLoop != null) {
            renderLoop.stop();
        }
    }

    /**
     * Обрабатывает нажатие клавиш.
     *
     * @param event событие клавиатуры
     */
    public void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
            case UP, W -> model.setDirection(Direction.UP);
            case DOWN, S -> model.setDirection(Direction.DOWN);
            case LEFT, A -> model.setDirection(Direction.LEFT);
            case RIGHT, D -> model.setDirection(Direction.RIGHT);
            case SPACE -> model.togglePause();
            case R -> {
                engine.reset();
                botRunner.restart(model.getBotSnakes(), engine);
            }
        }
        event.consume();
    }
}
