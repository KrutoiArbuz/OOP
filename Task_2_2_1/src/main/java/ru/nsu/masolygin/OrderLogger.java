package ru.nsu.masolygin;

import ru.nsu.masolygin.dto.Order;

public class OrderLogger {

    public synchronized void log(Order order) {
        System.out.println("["+order.getId()+"] ["+order.getState().getDisplayName()+"]");
    }
}
