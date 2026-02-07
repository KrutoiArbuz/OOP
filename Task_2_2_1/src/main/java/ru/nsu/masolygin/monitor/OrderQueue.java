package ru.nsu.masolygin.monitor;


import ru.nsu.masolygin.dto.Order;
import java.util.ArrayDeque;
import java.util.Queue;

public class OrderQueue {
    private final Queue<Order> orders;

    public OrderQueue() {
        this.orders = new ArrayDeque<>();
    }

    public synchronized void addOrder(Order order) {
        orders.add(order);
        notifyAll();
    }

    public synchronized Order takeOrder() throws InterruptedException {
        while (orders.isEmpty()) {
            wait();
        }
        return orders.poll();
    }
}
