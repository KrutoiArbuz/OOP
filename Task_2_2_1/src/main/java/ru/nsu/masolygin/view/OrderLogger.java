package ru.nsu.masolygin.view;

import ru.nsu.masolygin.dto.Order;

/**
 * Логгер заказов.
 */
public interface OrderLogger {

    void log(Order order, String message);
}
