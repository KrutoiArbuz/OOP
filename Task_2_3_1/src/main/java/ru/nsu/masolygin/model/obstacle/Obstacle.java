package ru.nsu.masolygin.model.obstacle;

import ru.nsu.masolygin.model.Placeable;
import ru.nsu.masolygin.model.Point;

/**
 * Препятствие на игровом поле.
 */
public final class Obstacle implements Placeable {

    private final Point position;

    /**
     * Конструктор.
     *
     * @param position позиция
     */
    public Obstacle(Point position) {
        this.position = position;
    }

    /**
     * Возвращает позицию препятствия.
     *
     * @return позиция
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Возвращает строковое представление.
     *
     * @return строка
     */
    @Override
    public String toString() {
        return "Obstacle" + position;
    }
}
