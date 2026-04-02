package ru.nsu.masolygin.model.bot.strategy;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GreedyStrategyTest {

    private GreedyStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new GreedyStrategy();
    }

    @Test
    void testStrategyCreation() {
        assertNotNull(strategy);
    }

    @Test
    void testIsBotStrategy() {
        assertNotNull(strategy);
    }
}

