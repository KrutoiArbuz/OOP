package ru.nsu.masolygin.worker;

import ru.nsu.masolygin.solutions.PrimeChecker;

public class ChunkProcessor {

    private final int[] chunk;
    private volatile boolean cancelled = false;

    public ChunkProcessor(int[] chunk) {
        this.chunk = chunk;
    }

    public ProcessResult run() {
        for (int n : chunk) {
            if (cancelled) {
                return ProcessResult.CANCELLED;
            }
            if (!PrimeChecker.isPrime(n)) {
                return ProcessResult.COMPOSITE_FOUND;
            }
        }
        return ProcessResult.ALL_PRIME;
    }

    public void cancel() {
        cancelled = true;
    }
}
