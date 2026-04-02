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

    @Test
    void testMultipleTicks() {
        speedEffect.apply(50);
        for (int i = 0; i < 30; i++) {
            speedEffect.tick();
        }
        assertEquals(BASE_SPEED, speedEffect.getSpeedMs());
    }

    @Test
    void testGetTicksLeft() {
        speedEffect.apply(50);
        assertNotNull(speedEffect.getTicksLeft());
    }

    @Test
    void testApplyZeroEffect() {
        speedEffect.apply(0);
        assertEquals(BASE_SPEED, speedEffect.getSpeedMs());
    }

    @Test
    void testApplyLargePositiveEffect() {
        speedEffect.apply(1000);
        assertEquals(800, speedEffect.getSpeedMs());
    }

    @Test
    void testApplyLargeNegativeEffect() {
        speedEffect.apply(-1000);
        assertEquals(60, speedEffect.getSpeedMs());
    }

    @Test
    void testMultipleEffects() {
        speedEffect.apply(50);
        speedEffect.reset();
        speedEffect.apply(-30);
        assertEquals(BASE_SPEED - 30, speedEffect.getSpeedMs());
    }
}
