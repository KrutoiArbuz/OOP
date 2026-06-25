package ru.nsu.masolygin;

/**
 * Класс таймер.
 */
public class Timer {

    private long startTime;
    private long endTime;

    /**
     * Запускает таймер.
     */
    public void start() {
        startTime = System.nanoTime();
    }

    /**
     * Останавливает таймер.
     */
    public void stop() {
        endTime = System.nanoTime();
    }

    /**
     * Возвращает прошедшее время.
     *
     * @return время в миллисекундах
     */
    public long getTime() {
        return (endTime - startTime) / 1_000_000;
    }
}
