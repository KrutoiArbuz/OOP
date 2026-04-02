package ru.nsu.masolygin.model.food;

import ru.nsu.masolygin.model.Placeable;
import ru.nsu.masolygin.model.Point;

/**
 * Еда на игровом поле.
 */
public final class Food implements Placeable {

    private final Point position;
    private final FoodType type;

    /**
     * Конструктор.
     *
     * @param position позиция
     * @param type     тип еды
     */
    public Food(Point position, FoodType type) {
        this.position = position;
        this.type = type;
    }

    /**
     * Возвращает позицию еды.
     *
     * @return позиция
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Возвращает тип еды.
     *
     * @return тип еды
     */
    public FoodType getType() {
        return type;
    }

    /**
     * Возвращает строковое представление.
     *
     * @return строка
     */
    @Override
    public String toString() {
        return type.getDisplayName() + " at " + position;
    }
}
