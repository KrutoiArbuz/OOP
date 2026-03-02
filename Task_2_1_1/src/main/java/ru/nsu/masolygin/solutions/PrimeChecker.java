package ru.nsu.masolygin.solutions;

/**
 * Интерфейс для проверки наличия не простых чисел в массиве.
 */
public interface PrimeChecker {

    /**
     * Проверяет, есть ли в массиве не простые числа.
     *
     * @param arr массив целых чисел
     * @return true, если есть не простое число, иначе false
     */
    boolean containsComposite(int[] arr);

    /**
     * Проверяет, является ли число простым.
     *
     * @param n число для проверки
     * @return true, если число простое, иначе false
     */
    static boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        }
        if (n == 2) {
            return true;
        }
        if (n % 2 == 0) {
            return false;
        }

        for (int i = 3; (long) i * i <= n; i += 2) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}
