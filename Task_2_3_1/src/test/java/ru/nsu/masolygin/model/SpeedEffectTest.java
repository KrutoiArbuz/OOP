package ru.nsu.masolygin.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SpeedEffectTest {

    private static final int BASE_SPEED = 200;
    private SpeedEffect speedEffect;

    @BeforeEach
    void setUp() {
        speedEffect = new SpeedEffect(BASE_SPEED);
    }

    @Test
    void testSpeedEffectCreation() {
        assertNotNull(speedEffect);
    }

    @Test
    void testGetCurrentSpeed() {
        assertEquals(BASE_SPEED, speedEffect.getSpeedMs());
    }

    @Test
    void testApplySpeedEffect() {
        speedEffect.apply(50);
        assertEquals(BASE_SPEED + 50, speedEffect.getSpeedMs());
    }

    @Test
    void testApplySlowEffect() {
        speedEffect.apply(-50);
        assertEquals(BASE_SPEED - 50, speedEffect.getSpeedMs());
    }

    @Test
    void testReset() {
        speedEffect.apply(100);
        speedEffect.reset();
        assertEquals(BASE_SPEED, speedEffect.getSpeedMs());
    }

    @Test
    void testTick() {
        speedEffect.apply(50);
        speedEffect.tick();
        assertEquals(BASE_SPEED + 50, speedEffect.getSpeedMs());
    }
}

