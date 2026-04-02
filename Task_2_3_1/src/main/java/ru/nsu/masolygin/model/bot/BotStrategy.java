package ru.nsu.masolygin.model.bot;

import ru.nsu.masolygin.model.Direction;
import ru.nsu.masolygin.model.GameModel;

/**
 * Интерфейс стратегии бота.
 */
public interface BotStrategy {

    /**
     * Выбирает направление движения.
     *
     * @param bot   бот
     * @param model модель
     * @return направление
     */
    Direction chooseDirection(BotSnake bot, GameModel model);
}
