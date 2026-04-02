package ru.nsu.masolygin.model;

import java.util.Objects;

/**
 * Точка на игровом поле.
 */
public final class Point {

    private final int x;
    private final int y;

    /**
     * Конструктор.
     *
     * @param x координата X
     * @param y координата Y
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Возвращает координату X.
     *
     * @return координата X
     */
    public int getX() {
        return x;
    }

    /**
     * Возвращает координату Y.
     *
     * @return координата Y
     */
    public int getY() {
        return y;
    }

    /**
     * Возвращает новую точку со смещением.
     *
     * @param dx смещение по X
     * @param dy смещение по Y
     * @return новая точка
     */
    public Point translate(int dx, int dy) {
        return new Point(x + dx, y + dy);
    }

    /**
     * Сравнивает точки по координатам.
     *
     * @param o другой объект
     * @return true, если координаты равны
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Point other)) {
            return false;
        }
        return x == other.x && y == other.y;
    }

    /**
     * Возвращает хеш-код точки.
     *
     * @return хеш-код
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Возвращает строковое представление точки.
     *
     * @return строка точки
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
