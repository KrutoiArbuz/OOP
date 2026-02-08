package ru.nsu.masolygin.actor;

import ru.nsu.masolygin.dto.Order;
import ru.nsu.masolygin.dto.OrderState;
import ru.nsu.masolygin.monitor.OrderQueue;
import ru.nsu.masolygin.monitor.Warehouse;
import ru.nsu.masolygin.view.OrderLogger;

/**
 * Класс пекаря.
 */
public class Baker implements Runnable {

    private final int id;
    private final int cookingTime;
    private OrderQueue orderQueue;
    private Warehouse warehouse;
    private OrderLogger orderLogger;

    /**
     * Конструктор.
     *
     * @param id          идентификатор пекаря
     * @param cookingTime время приготовления
     */
    public Baker(int id, int cookingTime) {
        this.id = id;
        this.cookingTime = cookingTime;
    }

    /**
     * Нанимает пекаря.
     *
     * @param orderQueue  очередь заказов
     * @param warehouse   склад
     * @param orderLogger логгер заказов
     */
    public void employ(OrderQueue orderQueue, Warehouse warehouse, OrderLogger orderLogger) {
        this.orderQueue = orderQueue;
        this.warehouse = warehouse;
        this.orderLogger = orderLogger;
    }

    /**
     * Запускает рабочий цикл пекаря.
     */
    @Override
    public void run() {
        if (orderQueue == null || warehouse == null || orderLogger == null) {
            throw new IllegalStateException(
            "Baker " + id + " is not employed yet! Call employ() before starting the thread.");
        }
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Order order = orderQueue.takeOrder();

                order.setState(OrderState.COOKING);
                orderLogger.log(order, "Baker " + id + " started cooking order");

                Thread.sleep(cookingTime);

                order.setState(OrderState.COOKED);
                orderLogger.log(order, "Baker " + id + " finished cooking order");

                warehouse.addOrder(order);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
