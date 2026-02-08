package ru.nsu.masolygin;

import static java.util.Collections.synchronizedList;

import java.util.ArrayList;
import java.util.List;
import ru.nsu.masolygin.actor.Baker;
import ru.nsu.masolygin.actor.Courier;
import ru.nsu.masolygin.dto.Order;
import ru.nsu.masolygin.dto.OrderState;
import ru.nsu.masolygin.monitor.OrderQueue;
import ru.nsu.masolygin.monitor.Warehouse;
import ru.nsu.masolygin.view.OrderLogger;

public class Pizzeria {

    private final int timeEnd;
    private final OrderQueue orderQueue;
    private final Warehouse warehouse;
    private final OrderLogger orderLogger;
    private int numberOfOrders = 0;
    private volatile boolean isOpen = false;

    private final Baker[] bakers;
    private final Courier[] couriers;

    private final List<Order> ordersDB = synchronizedList(new ArrayList<>());
    private final List<Thread> threads;

    public Pizzeria(int timeEnd, int warehouseCapacity, OrderLogger orderLogger, Baker[] bakers,
    Courier[] couriers) {
        this.timeEnd = timeEnd;
        this.orderQueue = new OrderQueue();
        this.warehouse = new Warehouse(warehouseCapacity);
        this.orderLogger = orderLogger;
        this.bakers = bakers;
        this.couriers = couriers;
        this.threads = new java.util.ArrayList<>();

        employPeople();
    }


    public void acceptOrder(Order order) {
        if (!isOpen) {
            System.out.println("Sorry, Pizzeria is closing. Order rejected.");
            return;
        }
        order.setInfo(numberOfOrders++, OrderState.IN_QUEUE);

        ordersDB.add(order);

        orderLogger.log(order, "Order accepted in pizzeria");
        orderQueue.addOrder(order);
    }


    public void workGracefulShutdown() {

        start();
        System.out.println("Pizzeria is starting work");
        try {
            Thread.sleep(timeEnd);
        } catch (InterruptedException e) {
            System.out.println("Pizzeria execution interrupted unexpectedly.");
        }
        System.out.println("Pizzeria is closing");
        gracefulShutdown();
    }


    private void employPeople() {
        for (Baker baker : bakers) {
            baker.employ(orderQueue, warehouse, orderLogger);
        }
        for (Courier courier : couriers) {
            courier.employ(warehouse, orderLogger);
        }
    }

    private void start() {

        isOpen = true;

        for (Baker baker : bakers) {
            Thread thread = new Thread(baker);
            threads.add(thread);
            thread.start();
        }

        for (Courier courier : couriers) {
            Thread thread = new Thread(courier);
            threads.add(thread);
            thread.start();
        }
    }

    private void stop() {
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }


    private void gracefulShutdown() {
        isOpen = false;

        boolean allDelivered = false;

        while (!allDelivered) {

            allDelivered = true;

            synchronized (ordersDB) {
                for (Order order : ordersDB) {
                    if (order.getState() != OrderState.DELIVERED) {
                        allDelivered = false;
                        break;
                    }
                }
            }

            if (!allDelivered) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }

        stop();

        ordersDB.clear();
    }

}
