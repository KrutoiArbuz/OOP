package ru.nsu.masolygin.model.bot;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class StrategyTypeTest {

    @Test
    void testStrategyTypeRandom() {
        StrategyType type = StrategyType.RANDOM;
        assertNotNull(type);
    }

    @Test
    void testStrategyTypeGreedy() {
        StrategyType type = StrategyType.GREEDY;
        assertNotNull(type);
    }

    @Test
    void testStrategyTypeWallHugger() {
        StrategyType type = StrategyType.WALL_HUGGER;
        assertNotNull(type);
    }

    @Test
    void testAllStrategyTypes() {
        assertNotNull(StrategyType.RANDOM);
        assertNotNull(StrategyType.GREEDY);
        assertNotNull(StrategyType.WALL_HUGGER);
    }
}

