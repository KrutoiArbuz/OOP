package ru.nsu.masolygin;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TimerTest {

    private Timer timer;

    @BeforeEach
    void setUp() {
        timer = new Timer();
    }

    @Test
    void testTimerBasicFunctionality() throws InterruptedException {
        timer.start();
        Thread.sleep(100);
        timer.stop();

        long elapsed = timer.getTime();
        assertTrue(elapsed >= 90 && elapsed <= 200);
    }

    @Test
    void testTimerWithShortDelay() throws InterruptedException {
        timer.start();
        Thread.sleep(10);
        timer.stop();

        long elapsed = timer.getTime();
        assertTrue(elapsed >= 0 && elapsed <= 50);
    }

    @Test
    void testTimerWithNoDelay() {
        timer.start();
        timer.stop();

        long elapsed = timer.getTime();
        assertTrue(elapsed >= 0 && elapsed <= 10);
    }

    @Test
    void testTimerMultipleMeasurements() throws InterruptedException {
        timer.start();
        Thread.sleep(50);
        timer.stop();
        long firstMeasurement = timer.getTime();

        timer.start();
        Thread.sleep(100);
        timer.stop();
        long secondMeasurement = timer.getTime();

        assertTrue(secondMeasurement >= firstMeasurement);
    }

    @Test
    void testTimerRestart() throws InterruptedException {
        timer.start();
        Thread.sleep(50);
        timer.stop();

        timer.start();
        Thread.sleep(30);
        timer.stop();

        long elapsed = timer.getTime();
        assertTrue(elapsed >= 20 && elapsed <= 70);
    }

    @Test
    void testTimerConsistency() throws InterruptedException {
        Timer timer1 = new Timer();
        Timer timer2 = new Timer();

        timer1.start();
        timer2.start();
        Thread.sleep(50);
        timer1.stop();
        timer2.stop();

        long time1 = timer1.getTime();
        long time2 = timer2.getTime();

        long difference = Math.abs(time1 - time2);
        assertTrue(difference <= 10);
    }

    @Test
    void testTimerAccuracy() throws InterruptedException {
        timer.start();
        Thread.sleep(200);
        timer.stop();

        long elapsed = timer.getTime();
        assertTrue(elapsed >= 180 && elapsed <= 250);
    }

    @Test
    void testTimerLongDuration() throws InterruptedException {
        timer.start();
        Thread.sleep(500);
        timer.stop();

        long elapsed = timer.getTime();
        assertTrue(elapsed >= 480);
    }
}

