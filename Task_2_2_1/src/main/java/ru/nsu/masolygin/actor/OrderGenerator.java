package ru.nsu.masolygin.actor;

import java.util.List;
import ru.nsu.masolygin.Pizzeria;
import ru.nsu.masolygin.dto.Order;
import ru.nsu.masolygin.dto.OrderState;

public class OrderGenerator implements Runnable {

    private final Pizzeria pizzeria;

    public OrderGenerator(Pizzeria pizzeria) {
        this.pizzeria = pizzeria;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Order order = new Order();
                pizzeria.acceptOrder(order);

                Thread.sleep(250);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
