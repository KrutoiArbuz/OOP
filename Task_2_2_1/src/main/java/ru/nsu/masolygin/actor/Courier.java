package ru.nsu.masolygin.actor;

import ru.nsu.masolygin.OrderLogger;
import ru.nsu.masolygin.dto.Order;
import ru.nsu.masolygin.dto.OrderState;
import ru.nsu.masolygin.monitor.Warehouse;
import java.util.List;

public class Courier implements Runnable {
    private final int id;
    private final int deliveryTime;
    private final int backpackCapacity;
    private final Warehouse warehouse;
    private final OrderLogger orderLogger;



    public Courier(int id, int deliveryTime, int backpackCapacity, Warehouse warehouse, OrderLogger orderLogger) {
        this.id = id;
        this.deliveryTime = deliveryTime;
        this.backpackCapacity = backpackCapacity;
        this.warehouse = warehouse;
        this.orderLogger = orderLogger;
    }



    @Override
    public void run() {
        try {
            while(!Thread.currentThread().isInterrupted()){
                List<Order> orders = warehouse.takeOrders(backpackCapacity);

                for (Order order : orders) {
                    order.setState(OrderState.DELIVERING);
                    orderLogger.log(order);
                }

                for (Order order : orders) {
                    Thread.sleep(deliveryTime);
                    order.setState(OrderState.DELIVERED);
                    orderLogger.log(order);
                }


            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
