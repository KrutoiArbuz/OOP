package ru.nsu.masolygin.dto;

/**
 * Перечисление состояний заказа.
 */
public enum OrderState {
    IN_QUEUE("In Queue"),
    COOKING("Cooking"),
    COOKED("Cooked"),
    DELIVERING("Delivering"),
    DELIVERED("Delivered");

    private final String displayName;

    /**
     * Конструктор.
     *
     * @param displayName отображаемое имя
     */
    OrderState(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Возвращает отображаемое имя.
     *
     * @return отображаемое имя
     */
    public String getDisplayName() {
        return displayName;
    }
}
