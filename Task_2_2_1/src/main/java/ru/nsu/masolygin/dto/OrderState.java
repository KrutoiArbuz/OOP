package ru.nsu.masolygin.dto;

public enum OrderState {
    IN_QUEUE("In Queue"),
    COOKING("Cooking"),
    COOKED("Cooked"),
    DELIVERING("Delivering"),
    DELIVERED("Delivered");

    private final String displayName;

    OrderState(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
