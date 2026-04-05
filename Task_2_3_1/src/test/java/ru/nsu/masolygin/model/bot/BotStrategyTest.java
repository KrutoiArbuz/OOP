package ru.nsu.masolygin.model.bot;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.model.bot.strategy.AbstractBotStrategy;

class BotStrategyTest {

    @Test
    void testAbstractBotStrategyExists() {
        assertNotNull(AbstractBotStrategy.class);
    }
}
