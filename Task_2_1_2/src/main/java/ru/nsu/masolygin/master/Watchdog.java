package ru.nsu.masolygin.master;

import java.util.List;


public class Watchdog {

    private final List<WorkerSession> workers;
    private final long timeoutMs;
    private final long intervalMs;
    private final long pingAfterMs;
    private final OnTimeout onTimeout;
    private Thread thread;

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

    public void start() {
        thread = new Thread(this::loop, "watchdog");
        thread.start();
    }

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
                w.markDead();
                w.close();
                onTimeout.handle(stuck);
            } else if (silence > pingAfterMs) {
                w.sendPing();
            }
        }
    }

    public interface OnTimeout {

        void handle(long stuckTaskId);
    }
}
