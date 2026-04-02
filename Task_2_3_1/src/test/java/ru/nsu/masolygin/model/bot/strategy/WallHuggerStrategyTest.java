package ru.nsu.masolygin.model.bot.strategy;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WallHuggerStrategyTest {

    private WallHuggerStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new WallHuggerStrategy();
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

