package ru.nsu.masolygin.solutions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PrimeCheckerCommonTest {

    static Stream<Arguments> primeCheckerProvider() {
        return Stream.of(
            Arguments.of(new SequentialPrimeChecker(), "Sequential"),
            Arguments.of(new ParallelPrimeChecker(), "Parallel"),
            Arguments.of(new ThreadPrimeChecker(1), "Thread-1"),
            Arguments.of(new ThreadPrimeChecker(2), "Thread-2"),
            Arguments.of(new ThreadPrimeChecker(4), "Thread-4"),
            Arguments.of(new ThreadPrimeChecker(8), "Thread-8")
        );
    }

    @ParameterizedTest(name = "{1}: Array with all primes")
    @MethodSource("primeCheckerProvider")
    void testHasNoPrimeWithAllPrimes(PrimeChecker checker, String name) {
        int[] allPrimes = {2, 3, 5, 7, 11, 13, 17, 19};
        assertFalse(checker.hasNoPrime(allPrimes));
    }

    @ParameterizedTest(name = "{1}: Array with all non-primes")
    @MethodSource("primeCheckerProvider")
    void testHasNoPrimeWithAllNonPrimes(PrimeChecker checker, String name) {
        int[] allNonPrimes = {1, 4, 6, 8, 9, 10, 12, 15};
        assertTrue(checker.hasNoPrime(allNonPrimes));
    }

    @ParameterizedTest(name = "{1}: Array with mixed numbers")
    @MethodSource("primeCheckerProvider")
    void testHasNoPrimeWithMixedNumbers(PrimeChecker checker, String name) {
        int[] mixedNumbers = {2, 4, 6, 8};
        assertTrue(checker.hasNoPrime(mixedNumbers));
    }

    @ParameterizedTest(name = "{1}: Array with primes first")
    @MethodSource("primeCheckerProvider")
    void testHasNoPrimeWithMixedPrimesFirst(PrimeChecker checker, String name) {
        int[] mixedNumbers = {7, 11, 4, 6};
        assertTrue(checker.hasNoPrime(mixedNumbers));
    }

    @ParameterizedTest(name = "{1}: Single prime")
    @MethodSource("primeCheckerProvider")
    void testHasNoPrimeWithSinglePrime(PrimeChecker checker, String name) {
        int[] singlePrime = {13};
        assertFalse(checker.hasNoPrime(singlePrime));
    }

    @ParameterizedTest(name = "{1}: Single non-prime")
    @MethodSource("primeCheckerProvider")
    void testHasNoPrimeWithSingleNonPrime(PrimeChecker checker, String name) {
        int[] singleNonPrime = {8};
        assertTrue(checker.hasNoPrime(singleNonPrime));
    }

    @ParameterizedTest(name = "{1}: Empty array")
    @MethodSource("primeCheckerProvider")
    void testHasNoPrimeWithEmptyArray(PrimeChecker checker, String name) {
        int[] emptyArray = {};
        assertFalse(checker.hasNoPrime(emptyArray));
    }

    @ParameterizedTest(name = "{1}: Array with one")
    @MethodSource("primeCheckerProvider")
    void testHasNoPrimeWithOne(PrimeChecker checker, String name) {
        int[] arrayWithOne = {1};
        assertTrue(checker.hasNoPrime(arrayWithOne));
    }

    @ParameterizedTest(name = "{1}: Array with zero")
    @MethodSource("primeCheckerProvider")
    void testHasNoPrimeWithZero(PrimeChecker checker, String name) {
        int[] arrayWithZero = {0};
        assertTrue(checker.hasNoPrime(arrayWithZero));
    }

    @ParameterizedTest(name = "{1}: Negative numbers")
    @MethodSource("primeCheckerProvider")
    void testHasNoPrimeWithNegativeNumbers(PrimeChecker checker, String name) {
        int[] negativeNumbers = {-5, -3, -1};
        assertTrue(checker.hasNoPrime(negativeNumbers));
    }

    @ParameterizedTest(name = "{1}: Large prime numbers")
    @MethodSource("primeCheckerProvider")
    void testHasNoPrimeWithLargeNumbers(PrimeChecker checker, String name) {
        int[] largeNumbers = {97, 101, 103, 107};
        assertFalse(checker.hasNoPrime(largeNumbers));
    }

    @ParameterizedTest(name = "{1}: Large mixed numbers")
    @MethodSource("primeCheckerProvider")
    void testHasNoPrimeWithLargeMixed(PrimeChecker checker, String name) {
        int[] largeMixed = {97, 100, 101, 102};
        assertTrue(checker.hasNoPrime(largeMixed));
    }

    @ParameterizedTest(name = "{1}: Task example 1")
    @MethodSource("primeCheckerProvider")
    void testTaskExample1(PrimeChecker checker, String name) {
        int[] input = {6, 8, 7, 13, 5, 9, 4};
        assertTrue(checker.hasNoPrime(input));
    }

    @ParameterizedTest(name = "{1}: Task example 2")
    @MethodSource("primeCheckerProvider")
    void testTaskExample2(PrimeChecker checker, String name) {
        int[] input = {20319251, 6997901, 6997927, 6997937, 17858849, 6997967,
            6998009, 6998029, 6998039, 20165149, 6998051, 6998053};
        assertFalse(checker.hasNoPrime(input));
    }

    @ParameterizedTest(name = "{1}: Large array with non-primes")
    @MethodSource("primeCheckerProvider")
    void testLargeArrayWithAllNonPrimes(PrimeChecker checker, String name) {
        int[] largeArray = new int[1000];
        for (int i = 0; i < 1000; i++) {
            largeArray[i] = (i + 2) * 2;
        }
        assertTrue(checker.hasNoPrime(largeArray));
    }

    @ParameterizedTest(name = "{1}: Large array with one prime at end")
    @MethodSource("primeCheckerProvider")
    void testLargeArrayWithOnePrime(PrimeChecker checker, String name) {
        int[] largeArray = new int[1000];
        for (int i = 0; i < 999; i++) {
            largeArray[i] = (i + 2) * 2;
        }
        largeArray[999] = 17;
        assertTrue(checker.hasNoPrime(largeArray));
    }

    @ParameterizedTest(name = "{1}: All primes array")
    @MethodSource("primeCheckerProvider")
    void testLargeArrayWithAllPrimes(PrimeChecker checker, String name) {
        int[] primes = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47};
        assertFalse(checker.hasNoPrime(primes));
    }

    @ParameterizedTest(name = "{1}: Consistency test")
    @MethodSource("primeCheckerProvider")
    void testConsistency(PrimeChecker checker, String name) {
        SequentialPrimeChecker reference = new SequentialPrimeChecker();
        int[] testArray = {2, 4, 6, 8, 10, 11, 12, 14, 16, 17};

        assertEquals(reference.hasNoPrime(testArray), checker.hasNoPrime(testArray));
    }

    @ParameterizedTest(name = "{1}: Special numbers")
    @MethodSource("primeCheckerProvider")
    void testSpecialNumbers(PrimeChecker checker, String name) {
        int[] specialNumbers = {0, 1, 2, 4, 9, 16, 25};
        assertTrue(checker.hasNoPrime(specialNumbers));
    }
}

