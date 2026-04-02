package ru.nsu.masolygin.model.bot.strategy;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RandomStrategyTest {

    private RandomStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new RandomStrategy();
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

