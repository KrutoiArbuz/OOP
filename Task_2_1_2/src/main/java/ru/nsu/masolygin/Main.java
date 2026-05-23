package ru.nsu.masolygin;

import java.util.Arrays;
import ru.nsu.masolygin.solutions.DistributedPrimeChecker;
import ru.nsu.masolygin.solutions.PrimeChecker;

/**
 * Класс main.
 */
public class Main {

    /**
     * Генерирует массив с большими простыми числами.
     *
     * @param size размер массива
     * @return массив целых чисел
     */
    public static int[] generateArray(int size) {
        int[] arr = new int[size];

        int heavyPrime = Integer.MAX_VALUE;

        int heavyPrime2 = 2147483629;

        for (int i = 0; i < size; i++) {
            arr[i] = (i % 2 == 0) ? heavyPrime : heavyPrime2;
        }

        return arr;
    }

    /**
     * Генерирует массив с одним не простым числом в конце.
     *
     * @param size размер массива
     * @return массив целых чисел
     */
    public static int[] generateFunnyArray(int size) {
        int[] arr = new int[size];

        Arrays.fill(arr, Integer.MAX_VALUE);

        arr[size - 1] = 22;

        return arr;
    }

    /**
     * Тестирует алгоритм проверки простых чисел и выводит результат.
     *
     * @param checker алгоритм проверки
     * @param data    массив для проверки
     * @param name    название теста
     */
    public static void tester(PrimeChecker checker, int[] data, String name) {
        Timer timer = new Timer();
        timer.start();
        boolean result = checker.containsComposite(data);
        timer.stop();
        System.out.println(name + ": " + result + ", Time: " + timer.getTime() + " ms");
    }

    /**
     * Точка входа в программу.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {

        DistributedPrimeChecker sequentialChecker = new DistributedPrimeChecker();

        int[] bigData = generateArray(10_000);
        int[] funnyData = generateFunnyArray(10_000);

        tester(sequentialChecker, bigData, "Sequential Checker (Big Data)");

        tester(sequentialChecker, funnyData, "Sequential Checker (Funny Data)");

    }
}
