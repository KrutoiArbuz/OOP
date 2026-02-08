package ru.nsu.masolygin.view;

import ru.nsu.masolygin.dto.Order;

public class OrderLogger {

    public synchronized void log(Order order, String message) {
        System.out.println("["+order.getId()+"] ["+order.getState().getDisplayName()+"] - " + message);
    }
}
