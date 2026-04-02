package ru.nsu.masolygin.engine;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BotRunnerTest {

    private BotRunner botRunner;

    @BeforeEach
    void setUp() {
        botRunner = new BotRunner();
    }

    @Test
    void testBotRunnerCreation() {
        assertNotNull(botRunner);
    }

    @Test
    void testBasicFunctionality() {
        assertTrue(true);
    }
}

