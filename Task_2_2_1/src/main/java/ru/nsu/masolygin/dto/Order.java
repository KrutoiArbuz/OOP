package ru.nsu.masolygin.dto;

public class Order {
    private int id;
    private OrderState state;

    public Order() {
    }

    public synchronized void setInfo(int id, OrderState state) {
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
