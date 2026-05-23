package ru.nsu.masolygin.solutions;

import java.net.InetSocketAddress;
import java.util.List;
import ru.nsu.masolygin.master.Master;

/**
 * Распределённая реализация проверки наличия непростых чисел.
 */
public class DistributedPrimeChecker implements PrimeChecker {

    private final List<InetSocketAddress> workers;
    private final int chunkSize;

    /**
     * Создает распределённый проверяющий с указанными рабочими процессами.
     *
     * @param workers   адреса рабочих процессов
     * @param chunkSize размер блока для каждого рабочего
     */
    public DistributedPrimeChecker(List<InetSocketAddress> workers, int chunkSize) {
        this.workers = workers;
        this.chunkSize = chunkSize;
    }

    /**
     * Проверяет, содержит ли массив непростые числа, используя рабочие процессы.
     *
     * @param arr массив целых чисел
     * @return true, если найдено непростое число, иначе false
     */
    @Override
    public boolean containsComposite(int[] arr) {
        return new Master(workers, chunkSize).containsComposite(arr);
    }
}
