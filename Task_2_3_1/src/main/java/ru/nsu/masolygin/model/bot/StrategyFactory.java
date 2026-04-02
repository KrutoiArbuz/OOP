package ru.nsu.masolygin.model.bot;

import ru.nsu.masolygin.model.bot.strategy.GreedyStrategy;
import ru.nsu.masolygin.model.bot.strategy.RandomStrategy;
import ru.nsu.masolygin.model.bot.strategy.WallHuggerStrategy;

/**
 * Фабрика стратегий.
 */
public final class StrategyFactory {

    /**
     * Конструктор.
     */
    private StrategyFactory() {
    }

    /**
     * Создает стратегию по типу.
     *
     * @param type тип стратегии
     * @return стратегия
     */
    public static BotStrategy create(StrategyType type) {
        return switch (type) {
            case RANDOM -> new RandomStrategy();
            case GREEDY -> new GreedyStrategy();
            case WALL_HUGGER -> new WallHuggerStrategy();
        };
    }
}
