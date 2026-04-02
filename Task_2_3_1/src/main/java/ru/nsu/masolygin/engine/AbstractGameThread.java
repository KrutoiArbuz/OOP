package ru.nsu.masolygin.engine;

/**
 * Базовый класс игровых потоков.
 */
public abstract class AbstractGameThread {

    protected volatile boolean running = false;
    private Thread thread;

    /**
     * Запускает поток.
     */
    public void start() {
        running = true;
        thread = new Thread(this::loop, threadName());
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Останавливает поток.
     */
    public void stop() {
        running = false;
        if (thread != null) {
            thread.interrupt();
        }
    }

    /**
     * Возвращает имя потока.
     *
     * @return имя потока
     */
    protected abstract String threadName();

    /**
     * Выполняет цикл потока.
     */
    protected abstract void loop();

    /**
     * Ждет оставшееся время шага.
     *
     * @param tickStart время начала шага
     * @param targetMs  целевая длительность шага
     */
    protected void sleepRemaining(long tickStart, long targetMs) {
        long elapsed = System.currentTimeMillis() - tickStart;
        long sleep = Math.max(0, targetMs - elapsed);
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
