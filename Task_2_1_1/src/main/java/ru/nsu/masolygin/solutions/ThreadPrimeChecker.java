package ru.nsu.masolygin.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Многопоточная реализация проверки простых чисел.
 */
public class ThreadPrimeChecker implements PrimeChecker {

    private final int numThreads;

    private volatile boolean hasPrime = false;

    /**
     * Конструктор.
     *
     * @param numThreads количество потоков
     */
    public ThreadPrimeChecker(int numThreads) {
        this.numThreads = numThreads;
    }

    /**
     * Проверяет массив используя несколько потоков.
     *
     * @param arr массив для проверки
     * @return true, если есть не простое число, иначе false
     */
    @Override
    public boolean hasNoPrime(int[] arr) {

        hasPrime = false;

        List<Thread> threads = new ArrayList<>();

        int chunkSize = (int) Math.ceil((double) arr.length / numThreads);

        for (int i = 0; i < numThreads; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, arr.length);

            if (start >= arr.length) {
                break;
            }

            Thread thread = new Thread(() -> {
                for (int j = start; j < end; j++) {
                    if ((j & 127) == 0 && hasPrime) {
                        return;
                    }
                    if (!PrimeChecker.isPrime(arr[j])) {
                        hasPrime = true;
                        return;
                    }
                }
            });
            threads.add(thread);
            thread.start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return hasPrime;
    }
}
