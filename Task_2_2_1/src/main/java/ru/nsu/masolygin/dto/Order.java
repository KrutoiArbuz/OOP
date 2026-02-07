package ru.nsu.masolygin.dto;

public class Order {
    private final int id;
    private OrderState state;

    public Order(int id, OrderState state) {
        this.id = id;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public synchronized void setState(OrderState state) {
        this.state = state;
    }

    public synchronized OrderState getState() {
        return state;
    }
}
