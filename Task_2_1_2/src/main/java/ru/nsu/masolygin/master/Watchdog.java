package ru.nsu.masolygin.master;

import java.util.List;


/**
 * Следит за живостью рабочих процессов и таймаутами выполнения задач.
 */
public class Watchdog {

    private final List<WorkerSession> workers;
    private final long timeoutMs;
    private final long intervalMs;
    private final long pingAfterMs;
    private final OnTimeout onTimeout;
    private Thread thread;

    /**
     * Создает контролер живости рабочих процессов.
     *
     * @param workers    список рабочих процессов для мониторинга
     * @param timeoutMs  таймаут выполнения задачи в миллисекундах
     * @param intervalMs интервал проверки в миллисекундах
     * @param onTimeout  обработчик события таймаута
     */
    public Watchdog(List<WorkerSession> workers,
        long timeoutMs,
        long intervalMs,
        OnTimeout onTimeout) {
        this.workers = workers;
        this.timeoutMs = timeoutMs;
        this.intervalMs = intervalMs;
        this.pingAfterMs = intervalMs * 2;
        this.onTimeout = onTimeout;
    }

    /**
     * Запускает мониторинг в отдельном потоке.
     */
    public void start() {
        thread = new Thread(this::loop, "watchdog");
        thread.start();
    }

    /**
     * Останавливает мониторинг.
     */
    public void stop() {
        if (thread != null) {
            thread.interrupt();
        }
    }

    private void loop() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(intervalMs);
            } catch (InterruptedException e) {
                return;
            }
            tick();
        }
    }

    private void tick() {
        long now = System.currentTimeMillis();
        for (WorkerSession w : workers) {
            if (!w.isAlive()) {
                continue;
            }

            long silence = now - w.lastResponseTime();

            if (w.currentTaskId() >= 0 && silence > timeoutMs) {
                long stuck = w.currentTaskId();
                System.out.println(w.address()
                    + " silence: " + silence + "ms, change task " + stuck);
                w.markDead();
                w.close();
                onTimeout.handle(stuck);
            } else if (silence > pingAfterMs) {
                System.out.println(w.address() + " silence: " + silence + "ms");
                w.sendPing();
            }
        }
    }

    /**
     * Обработчик события таймаута выполнения задачи.
     */
    public interface OnTimeout {

        /**
         * Обрабатывает таймаут выполнения задачи.
         *
         * @param stuckTaskId идентификатор зависшей задачи
         */
        void handle(long stuckTaskId);
    }
}
