package ru.nsu.masolygin.solutions;

import java.util.Arrays;

/**
 * Параллельная реализация проверки простых чисел через Stream API.
 */
public class ParallelPrimeChecker implements PrimeChecker {

    /**
     * Проверяет массив параллельно используя Stream API.
     *
     * @param arr массив для проверки
     * @return true, если есть не простое число, иначе false
     */
    @Override
    public boolean containsComposite(int[] arr) {

        return Arrays.stream(arr).parallel().anyMatch(num -> !PrimeChecker.isPrime(num));
    }
}
