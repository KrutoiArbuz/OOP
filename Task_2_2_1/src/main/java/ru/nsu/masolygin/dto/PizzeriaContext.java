package ru.nsu.masolygin.dto;

import ru.nsu.masolygin.monitor.OrderQueue;
import ru.nsu.masolygin.monitor.Warehouse;
import ru.nsu.masolygin.view.OrderLogger;

/**
 * Контекст пиццерии, содержащий очереди заказов, склад и логгер.
 */
public record PizzeriaContext(OrderQueue orderQueue, Warehouse warehouse, OrderLogger orderLogger) {

}
