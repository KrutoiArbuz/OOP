package ru.nsu.masolygin.model.bot.strategy;

import java.util.List;
import java.util.Random;
import ru.nsu.masolygin.model.Direction;
import ru.nsu.masolygin.model.GameModel;
import ru.nsu.masolygin.model.bot.BotSnake;

/**
 * Случайная стратегия бота.
 */
public class RandomStrategy extends AbstractBotStrategy {

    private final Random random = new Random();

    /**
     * Выбирает направление движения.
     *
     * @param bot бот
     * @param model модель
     * @return направление
     */
    @Override
    public Direction chooseDirection(BotSnake bot, GameModel model) {
        List<Direction> safe = safeDirections(bot, model);
        if (safe.isEmpty()) return bot.getDirection();
        return safe.get(random.nextInt(safe.size()));
    }
}
