package ru.nsu.masolygin.view;

import ru.nsu.masolygin.dto.Order;

/**
 * Класс логирования заказов.
 */
public class ConsoleLogger implements OrderLogger {

    @Override
    public synchronized void log(Order order, String message) {
        System.out.println(
        "["
        + order.getId()
        + "] ["
        + order.getState().getDisplayName()
        + "] - "
        + message);
    }
}
