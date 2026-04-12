package ru.nsu.masolygin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import ru.nsu.masolygin.SnakeApp;
import ru.nsu.masolygin.config.ConfigLoader;
import ru.nsu.masolygin.config.SnakeConfig;
import ru.nsu.masolygin.model.Point;
import ru.nsu.masolygin.view.CellType;
import ru.nsu.masolygin.view.MenuRenderer;

/**
 * Контроллер меню игры.
 */
public class MenuController {

    private final Map<Point, CellType> cellMap = new HashMap<>();
    @FXML
    private Spinner<Integer> widthSpinner;
    @FXML
    private Spinner<Integer> heightSpinner;
    @FXML
    private Slider speedSlider;
    @FXML
    private Slider botSpeedSlider;
    @FXML
    private Spinner<Integer> foodSpinner;
    @FXML
    private Spinner<Integer> winSpinner;

    @FXML
    private Label speedLabel;
    @FXML
    private Label winLabel;

    @FXML
    private CheckBox infiniteWinBox;
    @FXML
    private CheckBox noPlayerBox;

    @FXML
    private ToggleGroup brushGroup;
    @FXML
    private RadioButton brushObstacle;
    @FXML
    private RadioButton brushPlayer;
    @FXML
    private RadioButton brushBotGreedy;
    @FXML
    private RadioButton brushBotWall;
    @FXML
    private RadioButton brushBotRandom;
    @FXML
    private RadioButton brushEraser;

    @FXML
    private Button clearBtn;
    @FXML
    private Button startBtn;
    @FXML
    private GridPane gridPane;
    private CellType currentBrush = CellType.OBSTACLE;
    private SnakeApp app;
    private SnakeConfig baseConfig;
    private MenuRenderer menuRenderer;

    /**
     * Инициализирует элементы управления меню.
     */
    @FXML
    public void initialize() {
        try {
            ConfigLoader configLoader = new ConfigLoader();
            baseConfig = configLoader.load("/config.json");

            for (SnakeConfig.ObstacleConfig obs : baseConfig.obstacles()) {
                cellMap.put(new Point(obs.x(), obs.y()), CellType.OBSTACLE);
            }
            for (SnakeConfig.BotConfig bot : baseConfig.bots()) {
                CellType type = CellType.EMPTY;
                switch (bot.strategy()) {
                    case "GREEDY" -> type = CellType.BOT_GREEDY;
                    case "RANDOM" -> type = CellType.BOT_RANDOM;
                    case "WALL_HUGGER" -> type = CellType.BOT_WALL_HUGGER;
                }
                if (type != CellType.EMPTY) {
                    cellMap.put(new Point(bot.startX(), bot.startY()), type);
                }
            }
            if (baseConfig.playerEnabled() && baseConfig.playerStartX() != null
                && baseConfig.playerStartY() != null) {
                cellMap.put(new Point(baseConfig.playerStartX(), baseConfig.playerStartY()),
                    CellType.PLAYER);
            }
        } catch (Exception e) {
            baseConfig = SnakeConfig.defaults();
        }

        widthSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(6, 100,
            Math.max(6, baseConfig.fieldWidth())));
        heightSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(6, 100,
            Math.max(6, baseConfig.fieldHeight())));
        foodSpinner.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, baseConfig.foodCount()));

        int wLen = baseConfig.winLength() > 10000 ? 30 : baseConfig.winLength();
        winSpinner.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(3, 10000, wLen));
        speedSlider.setValue(baseConfig.initialSpeedMs());

        infiniteWinBox.setSelected(baseConfig.winLength() == Integer.MAX_VALUE);
        noPlayerBox.setSelected(!baseConfig.playerEnabled());

        winSpinner.visibleProperty().bind(infiniteWinBox.selectedProperty().not());
        winSpinner.managedProperty().bind(infiniteWinBox.selectedProperty().not());
        winLabel.visibleProperty().bind(infiniteWinBox.selectedProperty().not());
        winLabel.managedProperty().bind(infiniteWinBox.selectedProperty().not());

        speedSlider.disableProperty().bind(noPlayerBox.selectedProperty());
        speedLabel.disableProperty().bind(noPlayerBox.selectedProperty());
        brushPlayer.disableProperty().bind(noPlayerBox.selectedProperty());

        brushObstacle.setUserData(CellType.OBSTACLE);
        brushPlayer.setUserData(CellType.PLAYER);
        brushBotGreedy.setUserData(CellType.BOT_GREEDY);
        brushBotWall.setUserData(CellType.BOT_WALL_HUGGER);
        brushBotRandom.setUserData(CellType.BOT_RANDOM);
        brushEraser.setUserData(CellType.EMPTY);

        brushGroup.selectedToggleProperty().addListener((obs, oldT, newT) -> {
            if (newT != null) {
                currentBrush = (CellType) newT.getUserData();
            }
        });

        clearBtn.setOnAction(e -> {
            cellMap.clear();
            buildGrid();
        });

        startBtn.setOnAction(e -> startGame());

        menuRenderer = new MenuRenderer(gridPane, this::applyBrush);

        Runnable rebuildGrid = this::buildGrid;
        widthSpinner.valueProperty().addListener((obs, ov, nv) -> rebuildGrid.run());
        heightSpinner.valueProperty().addListener((obs, ov, nv) -> rebuildGrid.run());

        buildGrid();
    }

    /**
     * Устанавливает ссылку на главное приложение игры.
     *
     * @param app класс приложения
     */
    public void setApp(SnakeApp app) {
        this.app = app;
    }

    /**
     * Создает или обновляет превью-сетку.
     */
    private void buildGrid() {
        if (menuRenderer != null) {
            menuRenderer.renderGrid(widthSpinner.getValue(), heightSpinner.getValue(), cellMap);
        }
    }

    /**
     * Применяет текущую кисть к клетке.
     *
     * @param p    точка на поле
     * @param cell визуальный компонент клетки
     */
    private void applyBrush(Point p, Rectangle cell) {
        if (currentBrush == CellType.PLAYER) {
            Point oldPlayer = null;
            for (Map.Entry<Point, CellType> entry : cellMap.entrySet()) {
                if (entry.getValue() == CellType.PLAYER) {
                    oldPlayer = entry.getKey();
                    break;
                }
            }
            if (oldPlayer != null && !oldPlayer.equals(p)) {
                cellMap.remove(oldPlayer);
                buildGrid();
                return;
            }
        }

        if (currentBrush == CellType.EMPTY) {
            cellMap.remove(p);
        } else {
            cellMap.put(p, currentBrush);
        }
        cell.setFill(currentBrush.getColor());
    }

    /**
     * Собирает конфигурацию и запускает игру.
     */
    private void startGame() {
        if (app == null) {
            return;
        }

        List<SnakeConfig.ObstacleConfig> obstacles = new ArrayList<>();
        List<SnakeConfig.BotConfig> bots = new ArrayList<>();
        Integer px = null;
        Integer py = null;

        int w = widthSpinner.getValue();
        int h = heightSpinner.getValue();

        for (Map.Entry<Point, CellType> entry : cellMap.entrySet()) {
            Point p = entry.getKey();
            CellType type = entry.getValue();

            if (p.getX() >= w || p.getY() >= h) {
                continue;
            }

            if (type == CellType.PLAYER) {
                px = p.getX();
                py = p.getY();
            } else if (type == CellType.OBSTACLE) {
                obstacles.add(new SnakeConfig.ObstacleConfig(p.getX(), p.getY()));
            } else if (type != CellType.EMPTY) {
                String strategy = switch (type) {
                    case BOT_GREEDY -> "GREEDY";
                    case BOT_RANDOM -> "RANDOM";
                    case BOT_WALL_HUGGER -> "WALL_HUGGER";
                    default -> "GREEDY";
                };
                bots.add(new SnakeConfig.BotConfig(
                    p.getX(), p.getY(), strategy,
                    "#" + type.getColor().toString().substring(2, 8).toLowerCase(),
                    (int) botSpeedSlider.getValue()
                ));
            }
        }

        int maxWindowSize = 650;
        int cellSize = Math.min(maxWindowSize / w, maxWindowSize / h);
        if (cellSize < 5) {
            cellSize = 5;
        }

        int winLen = infiniteWinBox.isSelected() ? Integer.MAX_VALUE : winSpinner.getValue();

        SnakeConfig finalConfig = new SnakeConfig(
            w, h, cellSize,
            foodSpinner.getValue(), winLen,
            (int) speedSlider.getValue(),
            !noPlayerBox.isSelected(),
            obstacles, bots, px, py
        );

        app.startGame(finalConfig);
    }
}
