package ru.nsu.masolygin.solutions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PrimeCheckerTest {

    @Test
    void testIsPrimeWithPrimeNumbers() {
        assertTrue(PrimeChecker.isPrime(2));
        assertTrue(PrimeChecker.isPrime(3));
        assertTrue(PrimeChecker.isPrime(5));
        assertTrue(PrimeChecker.isPrime(7));
        assertTrue(PrimeChecker.isPrime(11));
        assertTrue(PrimeChecker.isPrime(13));
        assertTrue(PrimeChecker.isPrime(17));
        assertTrue(PrimeChecker.isPrime(19));
        assertTrue(PrimeChecker.isPrime(97));
    }

    @Test
    void testIsPrimeWithNonPrimeNumbers() {
        assertFalse(PrimeChecker.isPrime(1));
        assertFalse(PrimeChecker.isPrime(4));
        assertFalse(PrimeChecker.isPrime(6));
        assertFalse(PrimeChecker.isPrime(8));
        assertFalse(PrimeChecker.isPrime(9));
        assertFalse(PrimeChecker.isPrime(10));
        assertFalse(PrimeChecker.isPrime(12));
        assertFalse(PrimeChecker.isPrime(15));
        assertFalse(PrimeChecker.isPrime(100));
    }

    @Test
    void testIsPrimeWithNegativeNumbers() {
        assertFalse(PrimeChecker.isPrime(-1));
        assertFalse(PrimeChecker.isPrime(-5));
        assertFalse(PrimeChecker.isPrime(-10));
    }

    @Test
    void testIsPrimeWithZero() {
        assertFalse(PrimeChecker.isPrime(0));
    }

    @Test
    void testIsPrimeWithOne() {
        assertFalse(PrimeChecker.isPrime(1));
    }

    @Test
    void testIsPrimeWithTwo() {
        assertTrue(PrimeChecker.isPrime(2));
    }

    @Test
    void testIsPrimeWithLargePrimes() {
        assertTrue(PrimeChecker.isPrime(101));
        assertTrue(PrimeChecker.isPrime(103));
        assertTrue(PrimeChecker.isPrime(107));
        assertTrue(PrimeChecker.isPrime(109));
        assertTrue(PrimeChecker.isPrime(113));
    }

    @Test
    void testIsPrimeWithLargeNonPrimes() {
        assertFalse(PrimeChecker.isPrime(121)); // 11^2
        assertFalse(PrimeChecker.isPrime(169)); // 13^2
        assertFalse(PrimeChecker.isPrime(289)); // 17^2
    }

    @Test
    void testIsPrimeWithEvenNumbers() {
        assertFalse(PrimeChecker.isPrime(4));
        assertFalse(PrimeChecker.isPrime(6));
        assertFalse(PrimeChecker.isPrime(8));
        assertFalse(PrimeChecker.isPrime(10));
        assertFalse(PrimeChecker.isPrime(20));
        assertFalse(PrimeChecker.isPrime(50));
        assertFalse(PrimeChecker.isPrime(100));
    }

    @Test
    void testIsPrimeWithOddNonPrimes() {
        assertFalse(PrimeChecker.isPrime(9));  // 3^2
        assertFalse(PrimeChecker.isPrime(15)); // 3*5
        assertFalse(PrimeChecker.isPrime(21)); // 3*7
        assertFalse(PrimeChecker.isPrime(25)); // 5^2
        assertFalse(PrimeChecker.isPrime(27)); // 3^3
        assertFalse(PrimeChecker.isPrime(33)); // 3*11
        assertFalse(PrimeChecker.isPrime(35)); // 5*7
    }
}
