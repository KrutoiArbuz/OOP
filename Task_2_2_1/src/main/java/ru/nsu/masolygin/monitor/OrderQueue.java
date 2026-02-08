package ru.nsu.masolygin.monitor;


import ru.nsu.masolygin.dto.Order;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Класс очереди заказов.
 */
public class OrderQueue {
    private final Queue<Order> orders;

    /**
     * Конструктор.
     */
    public OrderQueue() {
        this.orders = new ArrayDeque<>();
    }

    /**
     * Добавляет заказ в очередь.
     *
     * @param order заказ
     */
    public synchronized void addOrder(Order order) {
        orders.add(order);
        notifyAll();
    }

    /**
     * Извлекает заказ из очереди.
     *
     * @return заказ
     * @throws InterruptedException если поток прерван
     */
    public synchronized Order takeOrder() throws InterruptedException {
        while (orders.isEmpty()) {
            wait();
        }
        return orders.poll();
    }
}
