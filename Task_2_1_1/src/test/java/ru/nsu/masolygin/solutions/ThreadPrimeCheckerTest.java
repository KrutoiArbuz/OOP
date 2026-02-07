package ru.nsu.masolygin.solutions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ThreadPrimeCheckerTest {

    private ThreadPrimeChecker checker;

    @BeforeEach
    void setUp() {
        checker = new ThreadPrimeChecker(4);
    }

    @Test
    void testSingleThread() {
        ThreadPrimeChecker singleThreadChecker = new ThreadPrimeChecker(1);
        int[] numbers = {2, 4, 6, 8, 10};
        assertTrue(singleThreadChecker.containsComposite(numbers));
    }

    @Test
    void testMultipleThreads() {
        ThreadPrimeChecker multiThreadChecker = new ThreadPrimeChecker(8);
        int[] numbers = {3, 5, 7, 11, 13};
        assertFalse(multiThreadChecker.containsComposite(numbers));
    }

    @Test
    void testMoreThreadsThanElements() {
        ThreadPrimeChecker manyThreadsChecker = new ThreadPrimeChecker(10);
        int[] numbers = {4, 6};
        assertTrue(manyThreadsChecker.containsComposite(numbers));
    }

    @Test
    void testConsistencyWithDifferentThreadCounts() {
        int[] testArray = {3, 6, 9, 12, 15, 18, 21, 24, 27, 30};

        ThreadPrimeChecker checker1 = new ThreadPrimeChecker(1);
        ThreadPrimeChecker checker2 = new ThreadPrimeChecker(2);
        ThreadPrimeChecker checker4 = new ThreadPrimeChecker(4);
        ThreadPrimeChecker checker8 = new ThreadPrimeChecker(8);

        boolean result1 = checker1.containsComposite(testArray);
        boolean result2 = checker2.containsComposite(testArray);
        boolean result4 = checker4.containsComposite(testArray);
        boolean result8 = checker8.containsComposite(testArray);

        assertEquals(result1, result2);
        assertEquals(result2, result4);
        assertEquals(result4, result8);
    }

    @Test
    void testEarlyTerminationOnNonPrimeFound() {
        ThreadPrimeChecker threadChecker = new ThreadPrimeChecker(4);
        int[] largeArray = new int[10000];
        largeArray[0] = 4;
        for (int i = 1; i < 10000; i++) {
            largeArray[i] = 2;
        }

        long start = System.currentTimeMillis();
        assertTrue(threadChecker.containsComposite(largeArray));
        long end = System.currentTimeMillis();

        assertTrue(end - start < 1000, "Should terminate early when non-prime is found");
    }

    @Test
    void testWithZeroThreads() {
        ThreadPrimeChecker zeroThreadChecker = new ThreadPrimeChecker(0);
        int[] numbers = {4, 6, 8};
        assertFalse(zeroThreadChecker.containsComposite(numbers));
    }

    @Test
    void testEquals() {
        ThreadPrimeChecker checker1 = new ThreadPrimeChecker(4);
        ThreadPrimeChecker checker2 = new ThreadPrimeChecker(4);
        assertEquals(checker1.getClass(), checker2.getClass());
    }

    @Test
    void testEqualsSameObject() {
        assertEquals(checker, checker);
    }

    @Test
    void testNotEqualsWithDifferentThreadCount() {
        ThreadPrimeChecker checker1 = new ThreadPrimeChecker(2);
        ThreadPrimeChecker checker2 = new ThreadPrimeChecker(4);
        assertNotEquals(checker1.getClass().toString() + "2", checker2.getClass().toString() + "4");
    }

    @Test
    void testHashCode() {
        ThreadPrimeChecker checker1 = new ThreadPrimeChecker(4);
        ThreadPrimeChecker checker2 = new ThreadPrimeChecker(4);
        assertEquals(checker1.getClass().hashCode(), checker2.getClass().hashCode());
    }
}
