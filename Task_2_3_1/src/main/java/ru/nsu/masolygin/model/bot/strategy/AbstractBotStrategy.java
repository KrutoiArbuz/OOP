package ru.nsu.masolygin.model.bot.strategy;

import java.util.ArrayList;
import java.util.List;
import ru.nsu.masolygin.model.Direction;
import ru.nsu.masolygin.model.GameModel;
import ru.nsu.masolygin.model.Point;
import ru.nsu.masolygin.model.bot.BotSnake;
import ru.nsu.masolygin.model.bot.BotStrategy;

/**
 * Базовый класс стратегий бота.
 */
public abstract class AbstractBotStrategy implements BotStrategy {

    /**
     * Возвращает безопасные направления.
     *
     * @param bot бот
     * @param model модель
     * @return список направлений
     */
    protected List<Direction> safeDirections(BotSnake bot, GameModel model) {
        List<Direction> safe = new ArrayList<>();
        for (Direction dir : Direction.values()) {
            if (dir.isOpposite(bot.getDirection())) continue;
            if (model.isWalkable(nextPoint(bot.getHead(), dir))) {
                safe.add(dir);
            }
        }
        return safe;
    }

    /**
     * Возвращает любое безопасное направление.
     *
     * @param bot бот
     * @param model модель
     * @return направление
     */
    protected Direction anySafe(BotSnake bot, GameModel model) {
        List<Direction> safe = safeDirections(bot, model);
        return safe.isEmpty() ? bot.getDirection() : safe.getFirst();
    }

    /**
     * Возвращает соседнюю точку по направлению.
     *
     * @param from исходная точка
     * @param dir направление
     * @return новая точка
     */
    protected Point nextPoint(Point from, Direction dir) {
        return new Point(from.getX() + dir.getDx(), from.getY() + dir.getDy());
    }
}
