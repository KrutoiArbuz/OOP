package ru.nsu.masolygin;

import static java.util.Collections.synchronizedList;

import java.util.ArrayList;
import java.util.List;
import ru.nsu.masolygin.actor.employee.BakerProfile;
import ru.nsu.masolygin.actor.employee.CourierProfile;
import ru.nsu.masolygin.actor.employee.Worker;
import ru.nsu.masolygin.dto.Order;
import ru.nsu.masolygin.dto.OrderState;
import ru.nsu.masolygin.monitor.OrderQueue;
import ru.nsu.masolygin.view.OrderLogger;

/**
 * Класс пиццерия.
 */
public class Pizzeria {

    private final int timeEnd;
    private final OrderQueue orderQueue;
    private final OrderLogger orderLogger;
    private final List<Worker<?>> allWorkers;

    private final List<Order> ordersDb = synchronizedList(new ArrayList<>());
    private final List<Thread> threads = new ArrayList<>();

    private int numberOfOrders = 0;
    private volatile boolean isOpen = false;


    /**
     * Конструктор.
     *
     * @param timeEnd     время работы пиццерии
     * @param orderLogger логгер заказов
     * @param bakers      массив пекарей
     * @param couriers    массив курьеров
     */
    public Pizzeria(int timeEnd, OrderQueue orderQueue,
    OrderLogger orderLogger, List<Worker<BakerProfile>> bakers,
    List<Worker<CourierProfile>> couriers) {
        this.timeEnd = timeEnd;
        this.orderQueue = orderQueue;
        this.orderLogger = orderLogger;
        this.allWorkers = new ArrayList<>();
        this.allWorkers.addAll(bakers);
        this.allWorkers.addAll(couriers);
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

        ordersDb.add(order);

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
     * Запускает все рабочие потоки.
     */
    private void start() {

        isOpen = true;

        for (Runnable worker: allWorkers) {
            Thread thread = new Thread(worker);
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

            synchronized (ordersDb) {
                for (Order order : ordersDb) {
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

        ordersDb.clear();
    }

}
