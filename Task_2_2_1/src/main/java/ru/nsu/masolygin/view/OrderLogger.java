package ru.nsu.masolygin.view;

import ru.nsu.masolygin.dto.Order;

/**
 * Класс логирования заказов.
 */
public class OrderLogger {

    /**
     * Логирует информацию о заказе.
     *
     * @param order заказ
     * @param message сообщение
     */
    public synchronized void log(Order order, String message) {
        System.out.println("["+order.getId()+"] ["+order.getState().getDisplayName()+"] - " + message);
    }
}
