package ru.nsu.masolygin.model.food;

/**
 * Типы еды.
 */
public enum FoodType {

    APPLE(1, 0, 55),
    CHERRY(2, 0, 25),
    GOLDEN_APPLE(3, -40, 10),
    MUSHROOM(0, +70, 10);

    private final int growAmount;
    private final int speedDeltaMs;
    private final int weight;

    FoodType(int growAmount, int speedDeltaMs, int weight) {
        this.growAmount = growAmount;
        this.speedDeltaMs = speedDeltaMs;
        this.weight = weight;
    }

    /**
     * Возвращает рост длины змейки.
     *
     * @return рост длины
     */
    public int getGrowAmount() {
        return growAmount;
    }

    /**
     * Возвращает изменение скорости.
     *
     * @return изменение скорости в миллисекундах
     */
    public int getSpeedDeltaMs() {
        return speedDeltaMs;
    }

    /**
     * Возвращает вес выпадения.
     *
     * @return вес
     */
    public int getWeight() {
        return weight;
    }
}
