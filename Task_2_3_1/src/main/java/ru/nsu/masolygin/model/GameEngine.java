package ru.nsu.masolygin.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import ru.nsu.masolygin.config.SnakeConfig;
import ru.nsu.masolygin.model.bot.BotSnake;
import ru.nsu.masolygin.model.bot.StrategyFactory;
import ru.nsu.masolygin.model.bot.StrategyType;
import ru.nsu.masolygin.model.food.Food;
import ru.nsu.masolygin.model.food.FoodFactory;

/**
 * Игровой движок — правила и исполнение.
 */
public class GameEngine {

    private final GameModel model;
    private final SnakeConfig config;

    /**
     * Конструктор.
     *
     * @param model  состояние игры
     * @param config конфигурация
     */
    public GameEngine(GameModel model, SnakeConfig config) {
        this.model = model;
        this.config = config;
    }

    /**
     * Возвращает модель — для стратегий ботов, которые читают isWalkable().
     *
     * @return модель
     */
    public GameModel getModel() {
        return model;
    }

    /**
     * Возвращает состояние игры (делегат к модели).
     *
     * @return состояние
     */
    public GameState getState() {
        return model.getState();
    }

    /**
     * Возвращает скорость игрока (делегат к модели).
     *
     * @return скорость в миллисекундах
     */
    public int getSpeedMs() {
        return model.getSpeedMs();
    }

    /**
     * Полный сброс игры.
     */
    public void reset() {
        model.tickLock.lock();
        try {
            loadObstacles();

            model.playerSnake = new Snake(safeSpawn(
                model.width / 2, model.height / 2, Set.of()));

            Set<Point> occupiedByBots = new HashSet<>();
            occupiedByBots.add(model.playerSnake.getHead());

            model.botSnakes = new ArrayList<>();
            for (SnakeConfig.BotConfig bc : config.bots()) {
                StrategyType type = StrategyType.valueOf(bc.strategy().toUpperCase());
                Point startPos = safeSpawn(bc.startX(), bc.startY(), occupiedByBots);
                BotSnake bot = new BotSnake(startPos, StrategyFactory.create(type),
                    bc.colorHex(), bc.speedMs());
                model.botSnakes.add(bot);
                occupiedByBots.add(startPos);
            }

            model.speedEffect.reset();
            model.pendingPlayerDirection.set(null);

            model.foods.clear();
            for (int i = 0; i < model.foodCount; i++) {
                spawnFood();
            }

            model.state = GameState.RUNNING;
            model.refreshSnapshot();
        } finally {
            model.tickLock.unlock();
        }
    }

    /**
     * Выполняет один шаг игрока. Захватывает tickLock — гарантирует атомарность относительно шагов
     * ботов.
     */
    public void executePlayerStep() {
        model.tickLock.lock();
        try {
            Direction pending = model.pendingPlayerDirection.getAndSet(null);
            if (pending != null) {
                model.playerSnake.setDirection(pending);
            }

            if (model.state != GameState.RUNNING) {
                return;
            }

            Point next = model.playerSnake.nextHeadPosition();
            if (isLethalForPlayer(next)) {
                model.state = GameState.LOST;
                model.refreshSnapshot();
                return;
            }

            Food eaten = foodAt(next);
            if (eaten != null) {
                model.playerSnake.addGrowth(eaten.getType().getGrowAmount());
                model.speedEffect.apply(eaten.getType().getSpeedDeltaMs());
                model.foods.remove(eaten);
                spawnFood();
            }
            model.playerSnake.step();

            if (model.playerSnake.getLength() >= model.winLength) {
                model.state = GameState.WON;
            }

            model.speedEffect.tick();
            model.refreshSnapshot();
        } finally {
            model.tickLock.unlock();
        }
    }

    /**
     * Выполняет шаг одного бота. Захватывает tickLock — один ход за раз (игрок или бот, не оба).
     *
     * @param bot бот
     * @param dir направление (вычислено заранее без lock)
     */
    public void executeBotStep(BotSnake bot, Direction dir) {
        if (!bot.isAlive()) {
            return;
        }
        model.tickLock.lock();
        try {
            if (model.state != GameState.RUNNING || !bot.isAlive()) {
                return;
            }
            bot.setDirection(dir);
            Point next = bot.nextHeadPosition();
            if (isLethalForBot(next, bot)) {
                bot.kill();
                model.refreshSnapshot();
                return;
            }
            Food eaten = foodAt(next);
            if (eaten != null) {
                bot.addGrowth(eaten.getType().getGrowAmount());
                model.foods.remove(eaten);
                spawnFood();
            }
            bot.step();
            model.refreshSnapshot();
        } finally {
            model.tickLock.unlock();
        }
    }

    private void loadObstacles() {
        model.obstacles.clear();
        model.obstaclePositions.clear();
        for (SnakeConfig.ObstacleConfig oc : config.obstacles()) {
            Point p = new Point(oc.x(), oc.y());
            model.obstacles.add(new ru.nsu.masolygin.model.obstacle.Obstacle(p));
            model.obstaclePositions.add(p);
        }
    }

    private boolean isLethalForPlayer(Point next) {
        if (isOutOfBounds(next) || model.obstaclePositions.contains(next)) {
            return true;
        }
        List<Point> body = model.playerSnake.getBody();
        for (int i = 0; i < body.size() - 1; i++) {
            if (body.get(i).equals(next)) {
                return true;
            }
        }
        for (BotSnake bot : model.botSnakes) {
            if (bot.isAlive() && bot.contains(next)) {
                return true;
            }
        }
        return false;
    }

    private boolean isLethalForBot(Point next, BotSnake self) {
        if (isOutOfBounds(next) || model.obstaclePositions.contains(next)) {
            return true;
        }
        if (model.playerSnake.contains(next)) {
            return true;
        }
        List<Point> selfBody = self.getBody();
        for (int i = 0; i < selfBody.size() - 1; i++) {
            if (selfBody.get(i).equals(next)) {
                return true;
            }
        }
        for (BotSnake other : model.botSnakes) {
            if (other != self && other.isAlive() && other.contains(next)) {
                return true;
            }
        }
        return false;
    }

    private Food foodAt(Point p) {
        for (Food f : model.foods) {
            if (f.getPosition().equals(p)) {
                return f;
            }
        }
        return null;
    }

    private void spawnFood() {
        Set<Point> occupied = new HashSet<>(model.playerSnake.getBody());
        for (BotSnake bot : model.botSnakes) {
            occupied.addAll(bot.getBody());
        }
        occupied.addAll(model.obstaclePositions);
        for (Food f : model.foods) {
            occupied.add(f.getPosition());
        }
        if (occupied.size() >= model.width * model.height) {
            return;
        }
        Point pos;
        do {
            pos = new Point(model.random.nextInt(model.width), model.random.nextInt(model.height));
        } while (occupied.contains(pos));
        model.foods.add(FoodFactory.createRandom(pos));
    }

    private Point safeSpawn(int preferX, int preferY, Set<Point> alsoOccupied) {
        for (int radius = 0; radius < Math.max(model.width, model.height); radius++) {
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dy = -radius; dy <= radius; dy++) {
                    if (Math.abs(dx) != radius && Math.abs(dy) != radius) {
                        continue;
                    }
                    Point candidate = new Point(preferX + dx, preferY + dy);
                    if (!isOutOfBounds(candidate)
                        && !model.obstaclePositions.contains(candidate)
                        && !alsoOccupied.contains(candidate)) {
                        return candidate;
                    }
                }
            }
        }
        return new Point(preferX, preferY);
    }

    private boolean isOutOfBounds(Point p) {
        return p.getX() < 0 || p.getX() >= model.width
            || p.getY() < 0 || p.getY() >= model.height;
    }
}
