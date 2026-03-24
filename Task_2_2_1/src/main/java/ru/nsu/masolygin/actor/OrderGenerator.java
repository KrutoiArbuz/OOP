package ru.nsu.masolygin.actor;

import ru.nsu.masolygin.Pizzeria;
import ru.nsu.masolygin.dto.Order;

/**
 * Класс генератора заказов.
 */
public class OrderGenerator implements Runnable {

    private final Pizzeria pizzeria;

    /**
     * Конструктор.
     *
     * @param pizzeria пиццерия
     */
    public OrderGenerator(Pizzeria pizzeria) {
        this.pizzeria = pizzeria;
    }

    /**
     * Запускает генерацию заказов.
     */
    @Override
    public void run() {
        try {
            while (pizzeria.isOpen()) {
                Order order = new Order();
                pizzeria.acceptOrder(order);

                Thread.sleep(250);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
