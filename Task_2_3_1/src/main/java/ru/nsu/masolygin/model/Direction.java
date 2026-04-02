package ru.nsu.masolygin.model;

/**
 * Направления движения.
 */
public enum Direction {

    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    private final int dx;
    private final int dy;

    /**
     * Конструктор.
     *
     * @param dx смещение по X
     * @param dy смещение по Y
     */
    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Возвращает смещение по X.
     *
     * @return смещение по X
     */
    public int getDx() {
        return dx;
    }

    /**
     * Возвращает смещение по Y.
     *
     * @return смещение по Y
     */
    public int getDy() {
        return dy;
    }

    /**
     * Проверяет противоположность направлений.
     *
     * @param other другое направление
     * @return true, если направления противоположны
     */
    public boolean isOpposite(Direction other) {
        return this.dx == -other.dx && this.dy == -other.dy;
    }

    /**
     * Поворачивает направление вправо.
     *
     * @return новое направление
     */
    public Direction turnRight() {
        return switch (this) {
            case UP -> RIGHT;
            case RIGHT -> DOWN;
            case DOWN -> LEFT;
            case LEFT -> UP;
        };
    }

    /**
     * Поворачивает направление влево.
     *
     * @return новое направление
     */
    public Direction turnLeft() {
        return switch (this) {
            case UP -> LEFT;
            case LEFT -> DOWN;
            case DOWN -> RIGHT;
            case RIGHT -> UP;
        };
    }
}
