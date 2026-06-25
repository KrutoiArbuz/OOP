package ru.nsu.masolygin;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.solutions.DistributedPrimeChecker;
import ru.nsu.masolygin.worker.Worker;

class DistributedIntegrationTest {

    private static final int BASE_PORT = 17000;
    private final List<Thread> workerThreads = new ArrayList<>();

    @AfterEach
    void tearDown() {
        for (Thread t : workerThreads) {
            t.interrupt();
        }
        workerThreads.clear();
    }

    private List<InetSocketAddress> startWorkers(int n) throws InterruptedException {
        List<InetSocketAddress> addrs = new ArrayList<>();
        int basePort = BASE_PORT + (int) (System.nanoTime() % 1000) * 10;
        for (int i = 0; i < n; i++) {
            int port = basePort + i;
            Thread t = new Thread(() -> {
                try {
                    new Worker(port).getClass();
                    Worker.main(new String[]{String.valueOf(port)});
                } catch (Exception ignored) {
                }
            }, "test-worker-" + port);
            t.setDaemon(true);
            t.start();
            workerThreads.add(t);
            addrs.add(new InetSocketAddress("localhost", port));
        }
        Thread.sleep(500);
        return addrs;
    }

    @Test
    void detectsCompositeInArray() throws InterruptedException {
        List<InetSocketAddress> workers = startWorkers(2);
        DistributedPrimeChecker checker = new DistributedPrimeChecker(workers, 2);
        assertTrue(checker.containsComposite(new int[]{6, 8, 7, 13, 5, 9, 4}));
    }

    @Test
    void allPrimesReturnsFalse() throws InterruptedException {
        List<InetSocketAddress> workers = startWorkers(2);
        DistributedPrimeChecker checker = new DistributedPrimeChecker(workers, 3);
        assertFalse(checker.containsComposite(new int[]{2, 3, 5, 7, 11, 13, 17, 19, 23}));
    }

    @Test
    void emptyArrayReturnsFalse() throws InterruptedException {
        List<InetSocketAddress> workers = startWorkers(2);
        DistributedPrimeChecker checker = new DistributedPrimeChecker(workers, 2);
        assertFalse(checker.containsComposite(new int[]{}));
    }

    @Test
    void singleWorkerWorks() throws InterruptedException {
        List<InetSocketAddress> workers = startWorkers(1);
        DistributedPrimeChecker checker = new DistributedPrimeChecker(workers, 2);
        assertTrue(checker.containsComposite(new int[]{4, 6, 8}));
    }

    @Test
    void heavyPrimesAllPrime() throws InterruptedException {
        List<InetSocketAddress> workers = startWorkers(3);
        DistributedPrimeChecker checker = new DistributedPrimeChecker(workers, 4);
        int[] data = new int[20];
        for (int i = 0; i < data.length; i++) {
            data[i] = (i % 2 == 0) ? Integer.MAX_VALUE : 2147483629;
        }
        assertFalse(checker.containsComposite(data));
    }

    @Test
    void compositeAtTheEndStillDetected() throws InterruptedException {
        List<InetSocketAddress> workers = startWorkers(2);
        DistributedPrimeChecker checker = new DistributedPrimeChecker(workers, 3);
        int[] data = new int[50];
        for (int i = 0; i < data.length - 1; i++) {
            data[i] = 7;
        }
        data[data.length - 1] = 9;
        assertTrue(checker.containsComposite(data));
    }

    @Test
    void noLiveWorkersThrows() {
        List<InetSocketAddress> deadAddrs = List.of(
            new InetSocketAddress("localhost", 1),
            new InetSocketAddress("localhost", 2)
        );
        DistributedPrimeChecker checker = new DistributedPrimeChecker(deadAddrs, 2);
        assertThrows(IllegalStateException.class,
            () -> checker.containsComposite(new int[]{2, 3, 5}));
    }

    @Test
    void chunkSizeBiggerThanArray() throws InterruptedException {
        List<InetSocketAddress> workers = startWorkers(2);
        DistributedPrimeChecker checker = new DistributedPrimeChecker(workers, 1000);
        assertTrue(checker.containsComposite(new int[]{6, 8, 7, 13, 5, 9, 4}));
    }
}
