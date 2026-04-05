package ru.nsu.masolygin.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import ru.nsu.masolygin.config.SnakeConfig;
import ru.nsu.masolygin.model.bot.BotSnake;
import ru.nsu.masolygin.model.food.Food;
import ru.nsu.masolygin.model.obstacle.Obstacle;

/**
 * Модель игры.
 */
public class GameModel {

    final int width;
    final int height;
    final int foodCount;
    final int winLength;
    final List<Food> foods = new ArrayList<>();
    final List<Obstacle> obstacles = new ArrayList<>();
    final Set<Point> obstaclePositions = new HashSet<>();
    final SpeedEffect speedEffect;
    final AtomicReference<Direction> pendingPlayerDirection = new AtomicReference<>();
    final AtomicReference<GameSnapshot> latestSnapshot = new AtomicReference<>();
    final ReentrantLock tickLock = new ReentrantLock();
    final Random random = new Random();
    Snake playerSnake;
    List<BotSnake> botSnakes = new ArrayList<>();
    volatile GameState state;

    /**
     * Конструктор.
     *
     * @param config конфигурация игры
     */
    public GameModel(SnakeConfig config) {
        this.width = config.fieldWidth();
        this.height = config.fieldHeight();
        this.foodCount = config.foodCount();
        this.winLength = config.winLength();
        this.speedEffect = new SpeedEffect(config.initialSpeedMs());
    }

    /**
     * Проверяет, можно ли пройти через клетку.
     *
     * @param p точка на поле
     * @return true если клетка свободна
     */
    public boolean isWalkable(Point p) {
        if (isOutOfBounds(p)) {
            return false;
        }
        if (obstaclePositions.contains(p)) {
            return false;
        }
        if (playerSnake != null && playerSnake.contains(p)) {
            return false;
        }
        for (BotSnake bot : botSnakes) {
            if (bot.isAlive() && bot.contains(p)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Устанавливает желаемое направление игрока.
     *
     * @param direction новое направление
     */
    public void setDirection(Direction direction) {
        pendingPlayerDirection.set(direction);
    }

    /**
     * Переключает паузу.
     */
    public void togglePause() {
        tickLock.lock();
        try {
            if (state == GameState.RUNNING) {
                state = GameState.PAUSED;
                System.out.println(state);
                refreshSnapshot();
            } else if (state == GameState.PAUSED) {
                state = GameState.RUNNING;
                refreshSnapshot();
            }
        } finally {
            tickLock.unlock();
        }
    }

    /**
     * Возвращает текущее состояние игры.
     *
     * @return состояние
     */
    public GameState getState() {
        return state;
    }

    /**
     * Возвращает текущую скорость игрока.
     *
     * @return скорость в миллисекундах
     */
    public int getSpeedMs() {
        return speedEffect.getSpeedMs();
    }

    /**
     * Возвращает последний снимок для рендера.
     *
     * @return снимок или null если игра ещё не стартовала
     */
    public GameSnapshot getLatestSnapshot() {
        return latestSnapshot.get();
    }

    /**
     * Возвращает список ботов (только для чтения).
     *
     * @return список ботов
     */
    public List<BotSnake> getBotSnakes() {
        return List.copyOf(botSnakes);
    }

    /**
     * Возвращает список еды (только для чтения). Используется стратегиями ботов для поиска
     * ближайшей цели.
     *
     * @return список еды
     */
    public List<Food> getFoods() {
        return List.copyOf(foods);
    }

    /**
     * Возвращает список препятствий (только для чтения).
     *
     * @return список препятствий
     */
    public List<Obstacle> getObstacles() {
        return List.copyOf(obstacles);
    }

    /**
     * Возвращает ширину поля.
     *
     * @return ширина
     */
    public int getWidth() {
        return width;
    }

    /**
     * Возвращает высоту поля.
     *
     * @return высота
     */
    public int getHeight() {
        return height;
    }

    /**
     * Обновляет снимок — вызывается GameEngine после каждого изменения состояния.
     */
    void refreshSnapshot() {
        latestSnapshot.set(buildSnapshot());
    }

    private boolean isOutOfBounds(Point p) {
        return p.getX() < 0 || p.getX() >= width || p.getY() < 0 || p.getY() >= height;
    }

    private GameSnapshot buildSnapshot() {
        List<GameSnapshot.BotSnapshot> botSnaps = botSnakes.stream()
            .map(bot -> new GameSnapshot.BotSnapshot(bot.getBody(), bot.getColorHex(),
                bot.isAlive()))
            .collect(Collectors.toList());

        return new GameSnapshot(
            playerSnake.getBody(),
            List.copyOf(botSnaps),
            List.copyOf(foods),
            List.copyOf(obstacles),
            state,
            playerSnake.getLength(),
            winLength,
            speedEffect.getSpeedMs(),
            speedEffect.getTicksLeft()
        );
    }
}
