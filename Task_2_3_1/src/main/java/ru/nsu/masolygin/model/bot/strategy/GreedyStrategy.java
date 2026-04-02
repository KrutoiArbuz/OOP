package ru.nsu.masolygin.model.bot.strategy;

import java.util.ArrayList;
import java.util.List;
import ru.nsu.masolygin.model.Direction;
import ru.nsu.masolygin.model.GameModel;
import ru.nsu.masolygin.model.Point;
import ru.nsu.masolygin.model.bot.BotSnake;
import ru.nsu.masolygin.model.food.Food;

/**
 * Жадная стратегия бота.
 */
public class GreedyStrategy extends AbstractBotStrategy {

    /**
     * Выбирает направление движения.
     *
     * @param bot   бот
     * @param model модель
     * @return направление
     */
    @Override
    public Direction chooseDirection(BotSnake bot, GameModel model) {
        Point target = findNearestFood(bot.getHead(), model);

        if (target == null) {
            return anySafe(bot, model);
        }

        List<Direction> ranked = rankByDistance(bot, target);
        for (Direction dir : ranked) {
            if (model.isWalkable(nextPoint(bot.getHead(), dir))) {
                return dir;
            }
        }

        return bot.getDirection();
    }

    /**
     * Находит ближайшую еду.
     *
     * @param head  позиция головы
     * @param model модель
     * @return точка ближайшей еды
     */
    private Point findNearestFood(Point head, GameModel model) {
        Point nearest = null;
        int minDist = Integer.MAX_VALUE;

        for (Food food : model.getFoods()) {
            int dist = manhattan(head, food.getPosition());
            if (dist < minDist) {
                minDist = dist;
                nearest = food.getPosition();
            }
        }
        return nearest;
    }

    /**
     * Сортирует направления по расстоянию до цели.
     *
     * @param bot    бот
     * @param target цель
     * @return отсортированный список направлений
     */
    private List<Direction> rankByDistance(BotSnake bot, Point target) {
        Point head = bot.getHead();
        List<Direction> dirs = new ArrayList<>(3);

        for (Direction dir : Direction.values()) {
            if (!dir.isOpposite(bot.getDirection())) {
                dirs.add(dir);
            }
        }

        dirs.sort((a, b) -> {
            int da = manhattan(head.translate(a.getDx(), a.getDy()), target);
            int db = manhattan(head.translate(b.getDx(), b.getDy()), target);
            return Integer.compare(da, db);
        });
        return dirs;
    }

    /**
     * Вычисляет манхэттенское расстояние.
     *
     * @param a точка A
     * @param b точка B
     * @return расстояние
     */
    private int manhattan(Point a, Point b) {
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
    }
}
