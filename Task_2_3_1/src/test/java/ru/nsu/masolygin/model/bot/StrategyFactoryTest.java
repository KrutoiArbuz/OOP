package ru.nsu.masolygin.model.bot;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class StrategyFactoryTest {

    @Test
    void testCreateRandomStrategy() {
        BotStrategy strategy = StrategyFactory.create(StrategyType.RANDOM);
        assertNotNull(strategy);
    }

    @Test
    void testCreateGreedyStrategy() {
        BotStrategy strategy = StrategyFactory.create(StrategyType.GREEDY);
        assertNotNull(strategy);
    }

    @Test
    void testCreateWallHuggerStrategy() {
        BotStrategy strategy = StrategyFactory.create(StrategyType.WALL_HUGGER);
        assertNotNull(strategy);
    }

    @Test
    void testAllStrategiesImplementInterface() {
        BotStrategy random = StrategyFactory.create(StrategyType.RANDOM);
        BotStrategy greedy = StrategyFactory.create(StrategyType.GREEDY);
        BotStrategy wallHugger = StrategyFactory.create(StrategyType.WALL_HUGGER);

        assertNotNull(random);
        assertNotNull(greedy);
        assertNotNull(wallHugger);
    }
}

