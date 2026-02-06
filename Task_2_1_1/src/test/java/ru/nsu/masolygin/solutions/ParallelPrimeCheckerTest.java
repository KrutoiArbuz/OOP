package ru.nsu.masolygin.solutions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

class ParallelPrimeCheckerTest {

    private ParallelPrimeChecker checker;

    @BeforeEach
    void setUp() {
        checker = new ParallelPrimeChecker();
    }

    @Test
    void testPerformanceWithLargeArray() {
        int[] largeArray = new int[100000];
        for (int i = 0; i < 100000; i++) {
            largeArray[i] = i * 2 + 4;
        }

        long start = System.currentTimeMillis();
        boolean result = checker.hasNoPrime(largeArray);
        long end = System.currentTimeMillis();

        assertTrue(result);
        assertTrue(end - start < 5000, "Parallel processing should be efficient");
    }

    @Test
    void testEarlyTerminationWithNonPrime() {
        int[] arrayWithEarlyNonPrime = new int[10000];
        arrayWithEarlyNonPrime[0] = 4;
        for (int i = 1; i < 10000; i++) {
            arrayWithEarlyNonPrime[i] = 2;
        }

        long start = System.currentTimeMillis();
        assertTrue(checker.hasNoPrime(arrayWithEarlyNonPrime));
        long end = System.currentTimeMillis();

        assertTrue(end - start < 1000, "Should terminate early when non-prime is found");
    }

    @Test
    void testParallelProcessingWithMixedData() {
        int[] mixedArray = new int[1000];
        for (int i = 0; i < 1000; i++) {
            if (i % 2 == 0) {
                mixedArray[i] = 2;
            } else {
                mixedArray[i] = 4;
            }
        }

        assertTrue(checker.hasNoPrime(mixedArray));
    }

    @Test
    void testConsistencyWithSequentialChecker() {
        SequentialPrimeChecker seqChecker = new SequentialPrimeChecker();
        ParallelPrimeChecker parallelChecker = new ParallelPrimeChecker();

        int[] testArray = {2, 4, 6, 8, 10, 11, 12, 14, 16, 17};

        assertEquals(seqChecker.hasNoPrime(testArray), parallelChecker.hasNoPrime(testArray));
    }

    @Test
    void testConsistencyWithThreadChecker() {
        ThreadPrimeChecker threadChecker = new ThreadPrimeChecker(4);
        ParallelPrimeChecker parallelChecker = new ParallelPrimeChecker();

        int[] testArray = {3, 6, 9, 12, 15, 18, 21, 24, 27, 30};

        assertEquals(threadChecker.hasNoPrime(testArray), parallelChecker.hasNoPrime(testArray));
    }

    @Test
    void testEquals() {
        ParallelPrimeChecker checker1 = new ParallelPrimeChecker();
        ParallelPrimeChecker checker2 = new ParallelPrimeChecker();
        assertEquals(checker1.getClass(), checker2.getClass());
    }

    @Test
    void testEqualsSameObject() {
        assertEquals(checker, checker);
    }

    @Test
    void testNotEqualsWithOtherChecker() {
        SequentialPrimeChecker seqChecker = new SequentialPrimeChecker();
        assertNotEquals(checker.getClass(), seqChecker.getClass());
    }

    @Test
    void testHashCode() {
        ParallelPrimeChecker checker1 = new ParallelPrimeChecker();
        ParallelPrimeChecker checker2 = new ParallelPrimeChecker();
        assertEquals(checker1.getClass().hashCode(), checker2.getClass().hashCode());
    }

    @Test
    void testToString() {
        ParallelPrimeChecker parallelChecker = new ParallelPrimeChecker();
        String result = parallelChecker.toString();
        assertTrue(result.contains("ParallelPrimeChecker"));
    }
}

