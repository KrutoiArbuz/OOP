package ru.nsu.masolygin.actor;

import ru.nsu.masolygin.view.OrderLogger;
import ru.nsu.masolygin.dto.Order;
import ru.nsu.masolygin.dto.OrderState;
import ru.nsu.masolygin.monitor.Warehouse;
import java.util.List;

public class Courier implements Runnable {

    private final int id;
    private final int deliveryTime;
    private final int backpackCapacity;
    private Warehouse warehouse;
    private OrderLogger orderLogger;

    public Courier(int id, int deliveryTime, int backpackCapacity) {
        this.id = id;
        this.deliveryTime = deliveryTime;
        this.backpackCapacity = backpackCapacity;
    }

    public void employ(Warehouse warehouse, OrderLogger orderLogger) {
        this.warehouse = warehouse;
        this.orderLogger = orderLogger;
    }

    @Override
    public void run() {
        if (warehouse == null || orderLogger == null) {
            throw new IllegalStateException(
            "Courier " + id + " is not employed yet! Call employ() before starting the thread.");
        }
        try {
            while (!Thread.currentThread().isInterrupted()) {
                List<Order> orders = warehouse.takeOrders(backpackCapacity);

                for (Order order : orders) {
                    order.setState(OrderState.DELIVERING);
                    orderLogger.log(order, "Courier " + id + " started delivering order");
                }

                for (Order order : orders) {
                    Thread.sleep(deliveryTime);
                    order.setState(OrderState.DELIVERED);
                    orderLogger.log(order, "Courier " + id + " delivered order");
                }


            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
