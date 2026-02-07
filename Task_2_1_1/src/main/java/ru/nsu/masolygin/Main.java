package ru.nsu.masolygin;

import java.util.Arrays;
import ru.nsu.masolygin.solutions.ParallelPrimeChecker;
import ru.nsu.masolygin.solutions.PrimeChecker;
import ru.nsu.masolygin.solutions.SequentialPrimeChecker;
import ru.nsu.masolygin.solutions.ThreadPrimeChecker;

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

        SequentialPrimeChecker sequentialChecker = new SequentialPrimeChecker();
        ThreadPrimeChecker threadedChecker1 = new ThreadPrimeChecker(1);
        ThreadPrimeChecker threadedChecker3 = new ThreadPrimeChecker(3);
        ThreadPrimeChecker threadedChecker4 = new ThreadPrimeChecker(5);
        ThreadPrimeChecker threadedChecker8 = new ThreadPrimeChecker(8);
        ParallelPrimeChecker parallelChecker = new ParallelPrimeChecker();

        int[] bigData = generateArray(10_000);
        int[] funnyData = generateFunnyArray(10_000);

        tester(sequentialChecker, bigData, "Sequential Checker (Big Data)");
        tester(threadedChecker1, bigData, "Threaded Checker (1 Thread) (Big Data)");
        tester(threadedChecker3, bigData, "Threaded Checker (3 Threads) (Big Data)");
        tester(threadedChecker4, bigData, "Threaded Checker (5 Threads) (Big Data)");
        tester(threadedChecker8, bigData, "Threaded Checker (8 Threads) (Big Data)");
        tester(parallelChecker, bigData, "Parallel Checker (Big Data)");

        tester(sequentialChecker, funnyData, "Sequential Checker (Funny Data)");
        tester(threadedChecker1, funnyData, "Threaded Checker (1 Thread) (Funny Data)");
        tester(threadedChecker3, funnyData, "Threaded Checker (3 Threads) (Funny Data)");
        tester(threadedChecker4, funnyData, "Threaded Checker (5 Threads) (Funny Data)");
        tester(threadedChecker8, funnyData, "Threaded Checker (8 Threads) (Funny Data)");
        tester(parallelChecker, funnyData, "Parallel Checker (Funny Data)");
    }
}
