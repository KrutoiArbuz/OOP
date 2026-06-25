package ru.nsu.masolygin.master;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import ru.nsu.masolygin.protocol.payload.ResultPayload;

/**
 * Мастер для распределенной проверки наличия непростых чисел в массиве.
 */
public class Master {

    private static final long TASK_TIMEOUT_MS = 500;
    private static final long WATCHDOG_INTERVAL_MS = 100;

    private final List<InetSocketAddress> addresses;
    private final int chunkSize;
    private final CountDownLatch finished = new CountDownLatch(1);

    private List<WorkerSession> workers;
    private volatile boolean foundComposite = false;
    private TaskQueue queue;
    private List<WorkerReader> readers;
    private Watchdog watchdog;

    /**
     * Создает мастер для работы с набором рабочих процессов.
     *
     * @param addresses адреса рабочих процессов
     * @param chunkSize размер блока для обработки одним рабочим
     */
    public Master(List<InetSocketAddress> addresses, int chunkSize) {
        this.addresses = addresses;
        this.chunkSize = chunkSize;
    }

    /**
     * Проверяет, содержит ли массив непростые числа.
     *
     * @param data массив чисел для проверки
     * @return true, если найдено непростое число, иначе false
     */
    public boolean containsComposite(int[] data) {
        if (data.length == 0) {
            return false;
        }
        this.queue = new TaskQueue(data, chunkSize);

        List<WorkerSession> connected = connectAll();
        if (connected.isEmpty()) {
            throw new IllegalStateException("No live workers");
        }
        this.workers = List.copyOf(connected);

        startReaders();
        startWatchdog();
        dispatch();

        try {
            finished.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        shutdown();
        return foundComposite;
    }

    private List<WorkerSession> connectAll() {
        List<WorkerSession> connected = new ArrayList<>();
        for (InetSocketAddress addr : addresses) {
            try {
                connected.add(new WorkerSession(addr, 2000));
            } catch (IOException e) {
                System.err.println("Cannot connect to " + addr + ": " + e.getMessage());
            }
        }
        return connected;
    }

    private void startReaders() {
        this.readers = new ArrayList<>();
        for (WorkerSession w : workers) {
            WorkerReader reader = new WorkerReader(w, this::onResult, this::onDisconnect);
            reader.start();
            readers.add(reader);
        }
    }

    private void startWatchdog() {
        this.watchdog = new Watchdog(workers, TASK_TIMEOUT_MS, WATCHDOG_INTERVAL_MS,
            stuckTaskId -> {
                queue.reassign(stuckTaskId);
                dispatch();
            });
        watchdog.start();
    }

    private synchronized void onResult(WorkerSession w, ResultPayload r) {
        if (!queue.isKnown(r.taskId())) {
            return;
        }
        queue.markDone(r.taskId());
        w.clearCurrentTask();

        if (r.hasComposite()) {
            foundComposite = true;
            broadcastCancel();
            finished.countDown();
            return;
        }
        if (queue.isAllDone()) {
            finished.countDown();
            return;
        }
        dispatch();
    }

    private synchronized void onDisconnect(WorkerSession w) {
        System.out.println("dead "+w.address());
        if (w.currentTaskId() >= 0) {
            queue.reassign(w.currentTaskId());
            System.out.println("reasign task "+w.currentTaskId());

        }
        dispatch();

    }

    private synchronized void dispatch() {
        if (foundComposite) {
            return;
        }
        for (WorkerSession w : workers) {
            if (!w.isAlive() || w.currentTaskId() >= 0) {
                continue;
            }
            Chunk c = queue.pollPending();
            if (c == null) {
                return;
            }
            try {
                w.sendTask(c);
            } catch (IOException e) {
                w.markDead();
                queue.reassign(c.taskId());
            }
        }
    }

    private void broadcastCancel() {
        for (WorkerSession w : workers) {
            if (w.isAlive()) {
                w.sendCancel();
            }
        }
    }

    private void shutdown() {
        if (watchdog != null) {
            watchdog.stop();
        }
        if (readers != null) {
            readers.forEach(WorkerReader::stop);
        }
        if (workers != null) {
            for (WorkerSession w : workers) {
                w.close();
            }
        }
    }
}
