package ru.nsu.masolygin.model;

import java.util.Objects;

/**
 * Точка на игровом поле.
 */
public final class Point {

    private final int xCoord;
    private final int yCoord;

    /**
     * Конструктор.
     *
     * @param x координата X
     * @param y координата Y
     */
    public Point(int x, int y) {
        this.xCoord = x;
        this.yCoord = y;
    }

    /**
     * Возвращает координату X.
     *
     * @return координата X
     */
    public int getX() {
        return xCoord;
    }

    /**
     * Возвращает координату Y.
     *
     * @return координата Y
     */
    public int getY() {
        return yCoord;
    }

    /**
     * Возвращает новую точку со смещением.
     *
     * @param dx смещение по X
     * @param dy смещение по Y
     * @return новая точка
     */
    public Point translate(int dx, int dy) {
        return new Point(xCoord + dx, yCoord + dy);
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
        return xCoord == other.xCoord && yCoord == other.yCoord;
    }

    /**
     * Возвращает хеш-код точки.
     *
     * @return хеш-код
     */
    @Override
    public int hashCode() {
        return Objects.hash(xCoord, yCoord);
    }

    /**
     * Возвращает строковое представление точки.
     *
     * @return строка точки
     */
    @Override
    public String toString() {
        return "(" + xCoord + ", " + yCoord + ")";
    }
}
