package ru.nsu.masolygin.model.food;

/**
 * Типы еды.
 */
public enum FoodType {

    APPLE       (1,    0,  "#e74c3c", "Яблоко",         55),
    CHERRY      (2,    0,  "#9b59b6", "Вишня (+2)",      25),
    GOLDEN_APPLE(3,  -40,  "#f1c40f","Золотое яблоко",  10),
    MUSHROOM    (0,  +70,  "#2ecc71","Гриб (замедление)", 10);

    private final int growAmount;
    private final int speedDeltaMs;
    private final String colorHex;
    private final String displayName;
    private final int weight;

    FoodType(int growAmount, int speedDeltaMs, String colorHex, String displayName, int weight) {
        this.growAmount   = growAmount;
        this.speedDeltaMs = speedDeltaMs;
        this.colorHex     = colorHex;
        this.displayName  = displayName;
        this.weight       = weight;
    }

    /**
     * Возвращает рост длины.
     *
     * @return рост длины
     */
    public int getGrowAmount() { return growAmount; }

    /**
     * Возвращает изменение скорости.
     *
     * @return изменение скорости в миллисекундах
     */
    public int getSpeedDeltaMs() { return speedDeltaMs; }

    /**
     * Возвращает цвет.
     *
     * @return цвет в формате HEX
     */
    public String getColorHex() { return colorHex; }

    /**
     * Возвращает отображаемое название.
     *
     * @return название
     */
    public String getDisplayName() { return displayName; }

    /**
     * Возвращает вес выпадения.
     *
     * @return вес
     */
    public int getWeight() { return weight; }
}
