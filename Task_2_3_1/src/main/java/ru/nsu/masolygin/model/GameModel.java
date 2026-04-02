package ru.nsu.masolygin.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import ru.nsu.masolygin.config.SnakeConfig;
import ru.nsu.masolygin.model.bot.BotSnake;
import ru.nsu.masolygin.model.bot.StrategyFactory;
import ru.nsu.masolygin.model.bot.StrategyType;
import ru.nsu.masolygin.model.food.Food;
import ru.nsu.masolygin.model.food.FoodFactory;
import ru.nsu.masolygin.model.obstacle.Obstacle;

/**
 * Модель игры.
 */
public class GameModel {

    private final int width;
    private final int height;
    private final int foodCount;
    private final int winLength;
    private final SnakeConfig config;
    private final List<Food> foods = new ArrayList<>();
    private final List<Obstacle> obstacles = new ArrayList<>();
    private final Set<Point> obstaclePositions = new HashSet<>();
    private final SpeedEffect speedEffect;
    private final AtomicReference<Direction> pendingPlayerDirection = new AtomicReference<>();
    private final AtomicReference<GameSnapshot> latestSnapshot = new AtomicReference<>();
    private final ReentrantLock tickLock = new ReentrantLock();
    private final Random random = new Random();
    private Snake playerSnake;
    private List<BotSnake> botSnakes;
    private volatile GameState state;

    /**
     * Конструктор.
     *
     * @param config конфигурация игры
     */
    public GameModel(SnakeConfig config) {
        this.config = config;
        this.width = config.getFieldWidth();
        this.height = config.getFieldHeight();
        this.foodCount = config.getFoodCount();
        this.winLength = config.getWinLength();
        this.speedEffect = new SpeedEffect(config.getInitialSpeedMs());
        loadObstacles(config);
        reset();
    }

    /**
     * Загружает препятствия из конфигурации.
     *
     * @param config конфигурация
     */
    private void loadObstacles(SnakeConfig config) {
        obstacles.clear();
        obstaclePositions.clear();
        for (SnakeConfig.ObstacleConfig oc : config.getObstacles()) {
            Point p = new Point(oc.getX(), oc.getY());
            obstacles.add(new Obstacle(p));
            obstaclePositions.add(p);
        }
    }

    /**
     * Сбрасывает состояние игры.
     */
    public void reset() {
        tickLock.lock();
        try {
            playerSnake = new Snake(safeSpawn(width / 2, height / 2, Set.of()));

            Set<Point> occupiedByBots = new HashSet<>();
            occupiedByBots.add(playerSnake.getHead());

            botSnakes = new ArrayList<>();
            for (SnakeConfig.BotConfig bc : config.getBots()) {
                StrategyType type = StrategyType.valueOf(bc.getStrategy().toUpperCase());
                Point startPos = safeSpawn(bc.getStartX(), bc.getStartY(), occupiedByBots);
                BotSnake bot = new BotSnake(startPos, StrategyFactory.create(type),
                    bc.getColorHex(), bc.getSpeedMs());
                botSnakes.add(bot);
                occupiedByBots.add(startPos);
            }

            speedEffect.reset();
            pendingPlayerDirection.set(null);

            foods.clear();
            for (int i = 0; i < foodCount; i++) {
                spawnFood();
            }

            state = GameState.RUNNING;
            refreshSnapshot();
        } finally {
            tickLock.unlock();
        }
    }

    /**
     * Устанавливает направление игрока.
     *
     * @param direction направление
     */
    public void setDirection(Direction direction) {
        pendingPlayerDirection.set(direction);
    }

    /**
     * Переключает паузу.
     */
    public void togglePause() {
        if (state == GameState.RUNNING) {
            state = GameState.PAUSED;
        } else if (state == GameState.PAUSED) {
            state = GameState.RUNNING;
        }
    }

    /**
     * Выполняет один шаг игрока.
     */
    public void executePlayerStep() {
        tickLock.lock();
        try {
            Direction pending = pendingPlayerDirection.getAndSet(null);
            if (pending != null) {
                playerSnake.setDirection(pending);
            }

            if (state != GameState.RUNNING) {
                return;
            }

            Point playerNext = playerSnake.nextHeadPosition();
            if (isLethalForPlayer(playerNext)) {
                state = GameState.LOST;
                refreshSnapshot();
                return;
            }

            Food eatenByPlayer = foodAt(playerNext);
            if (eatenByPlayer != null) {
                playerSnake.addGrowth(eatenByPlayer.getType().getGrowAmount());
                speedEffect.apply(eatenByPlayer.getType().getSpeedDeltaMs());
                foods.remove(eatenByPlayer);
                spawnFood();
            }
            playerSnake.step();

            if (playerSnake.getLength() >= winLength) {
                state = GameState.WON;
            }

            speedEffect.tick();
            refreshSnapshot();
        } finally {
            tickLock.unlock();
        }
    }

    /**
     * Возвращает последний снимок игры.
     *
     * @return снимок игры
     */
    public GameSnapshot getLatestSnapshot() {
        return latestSnapshot.get();
    }

    /**
     * Обновляет текущий снимок игры.
     */
    private void refreshSnapshot() {
        latestSnapshot.set(buildSnapshot());
    }

    /**
     * Создает снимок текущего состояния.
     *
     * @return снимок игры
     */
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

    /**
     * Выполняет шаг одного бота.
     *
     * @param bot бот
     * @param dir направление
     */
    public void executeBotStep(BotSnake bot, Direction dir) {
        if (!bot.isAlive()) {
            return;
        }
        tickLock.lock();
        try {
            if (state != GameState.RUNNING || !bot.isAlive()) {
                return;
            }
            bot.setDirection(dir);
            Point next = bot.nextHeadPosition();
            if (isLethalForBot(next, bot)) {
                bot.kill();
                refreshSnapshot();
                return;
            }
            Food eaten = foodAt(next);
            if (eaten != null) {
                bot.addGrowth(eaten.getType().getGrowAmount());
                foods.remove(eaten);
                spawnFood();
            }
            bot.step();
            refreshSnapshot();
        } finally {
            tickLock.unlock();
        }
    }

    /**
     * Проверяет смертельное столкновение игрока.
     *
     * @param next следующая точка
     * @return true, если столкновение смертельное
     */
    private boolean isLethalForPlayer(Point next) {
        if (isOutOfBounds(next)) {
            return true;
        }
        if (obstaclePositions.contains(next)) {
            return true;
        }
        List<Point> body = playerSnake.getBody();
        for (int i = 0; i < body.size() - 1; i++) {
            if (body.get(i).equals(next)) {
                return true;
            }
        }
        for (BotSnake bot : botSnakes) {
            if (bot.isAlive() && bot.contains(next)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Проверяет смертельное столкновение бота.
     *
     * @param next следующая точка
     * @param self бот
     * @return true, если столкновение смертельное
     */
    private boolean isLethalForBot(Point next, BotSnake self) {
        if (isOutOfBounds(next)) {
            return true;
        }
        if (obstaclePositions.contains(next)) {
            return true;
        }
        if (playerSnake.contains(next)) {
            return true;
        }
        List<Point> selfBody = self.getBody();
        for (int i = 0; i < selfBody.size() - 1; i++) {
            if (selfBody.get(i).equals(next)) {
                return true;
            }
        }
        for (BotSnake other : botSnakes) {
            if (other == self || !other.isAlive()) {
                continue;
            }
            if (other.contains(next)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Проверяет проходимость клетки.
     *
     * @param p точка
     * @return true, если клетка проходима
     */
    public boolean isWalkable(Point p) {
        if (isOutOfBounds(p)) {
            return false;
        }
        if (obstaclePositions.contains(p)) {
            return false;
        }
        if (playerSnake.contains(p)) {
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
     * Проверяет выход за границы поля.
     *
     * @param p точка
     * @return true, если точка вне поля
     */
    private boolean isOutOfBounds(Point p) {
        return p.getX() < 0 || p.getX() >= width || p.getY() < 0 || p.getY() >= height;
    }

    /**
     * Возвращает еду в заданной точке.
     *
     * @param p точка
     * @return еда или null
     */
    private Food foodAt(Point p) {
        for (Food f : foods) {
            if (f.getPosition().equals(p)) {
                return f;
            }
        }
        return null;
    }

    /**
     * Создает новую еду на свободной клетке.
     */
    private void spawnFood() {
        Set<Point> occupied = new HashSet<>(playerSnake.getBody());
        for (BotSnake bot : botSnakes) {
            occupied.addAll(bot.getBody());
        }
        occupied.addAll(obstaclePositions);
        for (Food f : foods) {
            occupied.add(f.getPosition());
        }
        if (occupied.size() >= width * height) {
            return;
        }
        Point pos;
        do {
            pos = new Point(random.nextInt(width), random.nextInt(height));
        } while (occupied.contains(pos));
        foods.add(FoodFactory.createRandom(pos));
    }

    /**
     * Находит безопасную стартовую точку.
     *
     * @param preferX      предпочтительная X
     * @param preferY      предпочтительная Y
     * @param alsoOccupied дополнительные занятые точки
     * @return найденная точка
     */
    private Point safeSpawn(int preferX, int preferY, Set<Point> alsoOccupied) {
        for (int radius = 0; radius < Math.max(width, height); radius++) {
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dy = -radius; dy <= radius; dy++) {
                    if (Math.abs(dx) != radius && Math.abs(dy) != radius) {
                        continue;
                    }
                    Point candidate = new Point(preferX + dx, preferY + dy);
                    if (!isOutOfBounds(candidate)
                        && !obstaclePositions.contains(candidate)
                        && !alsoOccupied.contains(candidate)) {
                        return candidate;
                    }
                }
            }
        }
        return new Point(preferX, preferY);
    }

    /**
     * Возвращает список еды.
     *
     * @return список еды
     */
    public List<Food> getFoods() {
        return Collections.unmodifiableList(foods);
    }

    /**
     * Возвращает список препятствий.
     *
     * @return список препятствий
     */
    public List<Obstacle> getObstacles() {
        return Collections.unmodifiableList(obstacles);
    }

    /**
     * Возвращает список ботов.
     *
     * @return список ботов
     */
    public List<BotSnake> getBotSnakes() {
        return Collections.unmodifiableList(botSnakes);
    }

    /**
     * Возвращает состояние игры.
     *
     * @return состояние игры
     */
    public GameState getState() {
        return state;
    }

    /**
     * Возвращает ширину поля.
     *
     * @return ширина поля
     */
    public int getWidth() {
        return width;
    }

    /**
     * Возвращает высоту поля.
     *
     * @return высота поля
     */
    public int getHeight() {
        return height;
    }

    /**
     * Возвращает текущую скорость.
     *
     * @return скорость в миллисекундах
     */
    public int getSpeedMs() {
        return speedEffect.getSpeedMs();
    }

}