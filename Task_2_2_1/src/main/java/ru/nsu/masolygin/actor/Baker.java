package ru.nsu.masolygin.actor;

import ru.nsu.masolygin.dto.Order;
import ru.nsu.masolygin.dto.OrderState;
import ru.nsu.masolygin.monitor.OrderQueue;
import ru.nsu.masolygin.monitor.Warehouse;
import ru.nsu.masolygin.OrderLogger;

public class Baker implements Runnable {

    private final int id;
    private final int cookingTime;
    private final OrderQueue orderQueue;
    private final Warehouse warehouse;
    private final OrderLogger orderLogger;


    public Baker(int id, int cookingTime, OrderQueue orderQueue, Warehouse warehouse, OrderLogger orderLogger) {
        this.id = id;
        this.cookingTime = cookingTime;
        this.orderQueue = orderQueue;
        this.warehouse = warehouse;
        this.orderLogger = orderLogger;
    }

    @Override
    public void run() {
        try {
            while(!Thread.currentThread().isInterrupted()){
                Order order = orderQueue.takeOrder();

                order.setState(OrderState.COOKING);
                orderLogger.log(order);

                Thread.sleep(cookingTime);

                order.setState(OrderState.COOKED);
                orderLogger.log(order);

                warehouse.addOrder(order);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
