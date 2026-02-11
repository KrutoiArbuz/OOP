package ru.nsu.masolygin.dto;

/**
 * Класс заказа.
 */
public class Order {

    private int id;
    private OrderState state;

    /**
     * Конструктор.
     */
    public Order() {
    }

    /**
     * Устанавливает информацию о заказе.
     *
     * @param id    идентификатор заказа
     * @param state состояние заказа
     */
    public synchronized void setInfo(int id, OrderState state) {
        this.id = id;
        this.state = state;
    }

    /**
     * Возвращает идентификатор заказа.
     *
     * @return идентификатор
     */
    public int getId() {
        return id;
    }

    /**
     * Возвращает состояние заказа.
     *
     * @return состояние заказа
     */
    public synchronized OrderState getState() {
        return state;
    }

    /**
     * Устанавливает состояние заказа.
     *
     * @param state состояние заказа
     */
    public synchronized void setState(OrderState state) {
        this.state = state;
    }
}
