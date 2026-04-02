package ru.nsu.masolygin.model.bot.strategy;

import ru.nsu.masolygin.model.Direction;
import ru.nsu.masolygin.model.GameModel;
import ru.nsu.masolygin.model.Point;
import ru.nsu.masolygin.model.bot.BotSnake;

/**
 * Стратегия движения вдоль стены.
 */
public class WallHuggerStrategy extends AbstractBotStrategy {

    /**
     * Выбирает направление движения.
     *
     * @param bot бот
     * @param model модель
     * @return направление
     */
    @Override
    public Direction chooseDirection(BotSnake bot, GameModel model) {
        Direction current = bot.getDirection();
        Direction[] candidates = {
            current.turnRight(),
            current,
            current.turnLeft()
        };

        Direction bestSafe = null;
        int bestWallScore = -1;

        for (Direction dir : candidates) {
            Point next = bot.getHead().translate(dir.getDx(), dir.getDy());
            if (!model.isWalkable(next)) continue;

            int score = wallScore(next, model);
            if (bestSafe == null || score > bestWallScore) {
                bestSafe = dir;
                bestWallScore = score;
            }
        }

        return bestSafe != null ? bestSafe : current;
    }

    /**
     * Вычисляет оценку близости к стене.
     *
     * @param p точка
     * @param model модель
     * @return оценка
     */
    private int wallScore(Point p, GameModel model) {
        int distLeft   = p.getX();
        int distRight  = model.getWidth()  - 1 - p.getX();
        int distTop    = p.getY();
        int distBottom = model.getHeight() - 1 - p.getY();
        int minDist = Math.min(Math.min(distLeft, distRight), Math.min(distTop, distBottom));
        return -minDist;
    }
}
