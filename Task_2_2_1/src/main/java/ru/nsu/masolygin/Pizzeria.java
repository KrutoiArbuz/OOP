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

/**
 * Класс пиццерия.
 */
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

    /**
     * Конструктор.
     *
     * @param timeEnd время работы пиццерии
     * @param warehouseCapacity вместимость склада
     * @param orderLogger логгер заказов
     * @param bakers массив пекарей
     * @param couriers массив курьеров
     */
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

    /**
     * Принимает новый заказ.
     *
     * @param order заказ для обработки
     */
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

    /**
     * Запускает работу пиццерии и корректное завершение.
     */
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

    /**
     * Нанимает работников пиццерии.
     */
    private void employPeople() {
        for (Baker baker : bakers) {
            baker.employ(orderQueue, warehouse, orderLogger);
        }
        for (Courier courier : couriers) {
            courier.employ(warehouse, orderLogger);
        }
    }

    /**
     * Запускает все рабочие потоки.
     */
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

    /**
     * Останавливает все рабочие потоки.
     */
    private void stop() {
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }

    /**
     * Выполняет корректное завершение работы.
     */
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
