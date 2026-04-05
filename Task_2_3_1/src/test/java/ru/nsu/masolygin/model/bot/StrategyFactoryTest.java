package ru.nsu.masolygin.model.bot;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.model.bot.strategy.AbstractBotStrategy;

class StrategyFactoryTest {

    @Test
    void testCreateRandom() {
        AbstractBotStrategy strategy = StrategyFactory.create(StrategyType.RANDOM);
        assertNotNull(strategy);
    }

    @Test
    void testCreateGreedy() {
        AbstractBotStrategy strategy = StrategyFactory.create(StrategyType.GREEDY);
        assertNotNull(strategy);
    }

    @Test
    void testCreateWallHugger() {
        AbstractBotStrategy strategy = StrategyFactory.create(StrategyType.WALL_HUGGER);
        assertNotNull(strategy);
    }

    @Test
    void testAllStrategiesAreAbstractBotStrategy() {
        assertTrue(StrategyFactory.create(StrategyType.RANDOM) instanceof AbstractBotStrategy);
        assertTrue(StrategyFactory.create(StrategyType.GREEDY) instanceof AbstractBotStrategy);
        assertTrue(StrategyFactory.create(StrategyType.WALL_HUGGER) instanceof AbstractBotStrategy);
    }
}
