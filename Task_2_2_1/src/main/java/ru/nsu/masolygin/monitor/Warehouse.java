package ru.nsu.masolygin.monitor;

import java.util.Queue;
import ru.nsu.masolygin.dto.Order;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс склада заказов.
 */
public class Warehouse {

    private final int capacity;
    private final Queue<Order> orders;

    /**
     * Конструктор.
     *
     * @param capacity вместимость склада
     */
    public Warehouse(int capacity) {
        this.capacity = capacity;
        this.orders = new ArrayDeque<>();
    }

    /**
     * Добавляет заказ на склад.
     *
     * @param order заказ
     * @throws InterruptedException если поток прерван
     */
    public synchronized void addOrder(Order order) throws InterruptedException {
        while (orders.size() >= capacity) {
            wait();
        }
        orders.add(order);
        notifyAll();
    }

    /**
     * Извлекает заказы со склада.
     *
     * @param maxCount максимальное количество заказов
     * @return список заказов
     * @throws InterruptedException если поток прерван
     */
    public synchronized List<Order> takeOrders(int maxCount) throws InterruptedException {
        while (orders.isEmpty()) {
            wait();
        }
        List<Order> takenOrders = new ArrayList<>();
        for (int i = 0; i < maxCount && !orders.isEmpty(); i++) {
            takenOrders.add(orders.poll());
        }

        notifyAll();

        return takenOrders;
    }
}
