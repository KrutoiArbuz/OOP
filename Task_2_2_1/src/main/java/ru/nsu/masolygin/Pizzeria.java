package ru.nsu.masolygin;

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

    private final Baker[] bakers;
    private final Courier[] couriers;

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

    public void employPeople() {
        for (Baker baker : bakers) {
            baker.employ(orderQueue, warehouse, orderLogger);
        }
        for (Courier courier : couriers) {
            courier.employ(warehouse, orderLogger);
        }
    }

    public void acceptOrder(Order order) {
        order.setInfo(numberOfOrders++,OrderState.IN_QUEUE);
        orderLogger.log(order, "Order accepted in pizzeria");
        orderQueue.addOrder(order);
    }

    public void start() {
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

    public void stop() {
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }

    public void work(){

        start();
        try {
            Thread.sleep(timeEnd);
        } catch (InterruptedException e) {
            System.out.println("Pizzeria execution interrupted unexpectedly.");
        }

        stop();

    }


}
