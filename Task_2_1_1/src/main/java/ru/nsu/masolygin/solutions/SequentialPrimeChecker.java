package ru.nsu.masolygin.solutions;

/**
 * Последовательная реализация проверки простых чисел.
 */
public class SequentialPrimeChecker implements PrimeChecker {

    /**
     * Проверяет массив последовательно.
     *
     * @param arr массив для проверки
     * @return true, если есть не простое число, иначе false
     */
    @Override
    public boolean hasNoPrime(int[] arr) {
        for (int num : arr) {
            if (!PrimeChecker.isPrime(num)) {
                return true;
            }
        }
        return false;
    }
}