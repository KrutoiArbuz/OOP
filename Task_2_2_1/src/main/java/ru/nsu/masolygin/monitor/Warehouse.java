package ru.nsu.masolygin.monitor;

import ru.nsu.masolygin.dto.Order;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class Warehouse {
    private final int capacity;
    private final ArrayDeque<Order> orders;

    public Warehouse(int capacity) {
        this.capacity = capacity;
        this.orders = new ArrayDeque<>();
    }

    public synchronized void addOrder(Order order) throws InterruptedException{
        while (orders.size() >= capacity) {
            wait();
        }
        orders.add(order);
        notifyAll();
    }

    public synchronized List<Order> takeOrders(int maxCount) throws InterruptedException{
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
