package ru.nsu.masolygin.view;

import java.util.List;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import ru.nsu.masolygin.config.SnakeConfig;
import ru.nsu.masolygin.model.GameSnapshot;
import ru.nsu.masolygin.model.Point;
import ru.nsu.masolygin.model.food.Food;
import ru.nsu.masolygin.model.obstacle.Obstacle;

/**
 * Рендерер игрового поля.
 */
public class GameRenderer {

    private static final String CH_PLAYER_HEAD = "P";
    private static final String CH_BODY = "S";
    private static final String CH_FOOD = "F";
    private static final String CH_OBSTACLE = "#";

    private final Canvas canvas;
    private final Label scoreLabel;
    private final Label statusLabel;
    private final StackPane overlayPane;
    private final SnakeConfig config;
    private final Font cellFont;

    /**
     * Конструктор.
     *
     * @param canvas      холст
     * @param scoreLabel  метка счета
     * @param statusLabel метка статуса
     * @param overlayPane панель оверлея
     * @param config      конфигурация
     */
    public GameRenderer(Canvas canvas, Label scoreLabel, Label statusLabel,
        StackPane overlayPane, SnakeConfig config) {
        this.canvas = canvas;
        this.scoreLabel = scoreLabel;
        this.statusLabel = statusLabel;
        this.overlayPane = overlayPane;
        this.config = config;
        this.cellFont = Font.font("Courier New", FontWeight.BOLD, config.getCellSize() * 0.65);
    }

    /**
     * Рисует кадр игры.
     *
     * @param snap снимок состояния
     */
    public void render(GameSnapshot snap) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        int cell = config.getCellSize();
        int cols = config.getFieldWidth();
        int rows = config.getFieldHeight();

        drawBackground(gc, cols, rows, cell);
        drawObstacles(gc, snap.obstacles(), cell);
        drawFoods(gc, snap.foods(), cell);
        drawBots(gc, snap.bots(), cell);
        drawPlayer(gc, snap.playerBody(), cell);

        updateScoreLabel(snap);
        updateOverlay(snap);
    }

    /**
     * Рисует фон поля.
     *
     * @param gc   контекст
     * @param cols ширина поля в клетках
     * @param rows высота поля в клетках
     * @param cell размер клетки
     */
    private void drawBackground(GraphicsContext gc, int cols, int rows, int cell) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, (double) cols * cell, (double) rows * cell);
    }

    /**
     * Рисует препятствия.
     *
     * @param gc        контекст
     * @param obstacles препятствия
     * @param cell      размер клетки
     */
    private void drawObstacles(GraphicsContext gc, List<Obstacle> obstacles, int cell) {
        Color color = Color.rgb(120, 120, 120);
        for (Obstacle obs : obstacles) {
            drawChar(gc, CH_OBSTACLE, obs.getPosition(), cell, color);
        }
    }

    /**
     * Рисует еду.
     *
     * @param gc    контекст
     * @param foods еда
     * @param cell  размер клетки
     */
    private void drawFoods(GraphicsContext gc, List<Food> foods, int cell) {
        for (Food food : foods) {
            Color color = Color.web(food.getType().getColorHex());
            drawChar(gc, CH_FOOD, food.getPosition(), cell, color);
        }
    }

    /**
     * Рисует ботов.
     *
     * @param gc   контекст
     * @param bots боты
     * @param cell размер клетки
     */
    private void drawBots(GraphicsContext gc, List<GameSnapshot.BotSnapshot> bots, int cell) {
        for (GameSnapshot.BotSnapshot bot : bots) {
            if (!bot.alive()) {
                continue;
            }
            Color base = Color.web(bot.colorHex());
            Color head = base.brighter();
            Color body = base.darker();
            List<Point> segments = bot.body();
            for (int i = 0; i < segments.size(); i++) {
                drawChar(gc, CH_BODY, segments.get(i), cell, i == 0 ? head : body);
            }
        }
    }

    /**
     * Рисует игрока.
     *
     * @param gc   контекст
     * @param body тело игрока
     * @param cell размер клетки
     */
    private void drawPlayer(GraphicsContext gc, List<Point> body, int cell) {
        for (int i = 0; i < body.size(); i++) {
            if (i == 0) {
                drawChar(gc, CH_PLAYER_HEAD, body.get(i), cell, Color.rgb(100, 255, 80));
            } else {
                int green = Math.max(60, 200 - i * 4);
                drawChar(gc, CH_BODY, body.get(i), cell, Color.rgb(0, green, 0));
            }
        }
    }

    /**
     * Рисует символ в клетке.
     *
     * @param gc    контекст
     * @param ch    символ
     * @param p     координаты клетки
     * @param cell  размер клетки
     * @param color цвет
     */
    private void drawChar(GraphicsContext gc, String ch, Point p, int cell, Color color) {
        gc.setFill(color);
        gc.setFont(cellFont);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        double cx = p.getX() * cell + cell / 2.0;
        double cy = p.getY() * cell + cell / 2.0;
        gc.fillText(ch, cx, cy);
    }

    /**
     * Обновляет метку счета.
     *
     * @param snap снимок состояния
     */
    private void updateScoreLabel(GameSnapshot snap) {
        long aliveBots = snap.bots().stream().filter(GameSnapshot.BotSnapshot::alive).count();
        String effect = "";
        if (snap.speedEffectTicks() > 0) {
            boolean faster = snap.speedMs() < config.getInitialSpeedMs();
            effect = (faster ? "  ⚡" : "  🐢") + "+" + snap.speedEffectTicks();
        }
        scoreLabel.setText(
            "Длина: " + snap.playerLength() + "/" + snap.winLength()
                + "   " + snap.speedMs() + "мс/ход"
                + "   Ботов: " + aliveBots + "/" + snap.bots().size()
                + effect
        );
    }

    /**
     * Обновляет оверлей состояния.
     *
     * @param snap снимок состояния
     */
    private void updateOverlay(GameSnapshot snap) {
        switch (snap.state()) {
            case RUNNING -> overlayPane.setVisible(false);
            case PAUSED -> show("ПАУЗА\n[SPACE] продолжить   [R] рестарт");
            case WON -> show("ПОБЕДА!\nДлина: " + snap.playerLength() + "\n[R] снова");
            case LOST -> show("ИГРА ОКОНЧЕНА\n[R] попробовать снова");
        }
    }

    /**
     * Показывает сообщение оверлея.
     *
     * @param message текст сообщения
     */
    private void show(String message) {
        overlayPane.setVisible(true);
        statusLabel.setText(message);
    }
}