package ru.nsu.masolygin.model;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * Модель змейки.
 */
public class Snake {

    private final Deque<Point> body;
    private Direction direction;
    private int pendingGrowth = 0;

    /**
     * Конструктор.
     *
     * @param startPosition стартовая позиция
     */
    public Snake(Point startPosition) {
        body = new ArrayDeque<>();
        body.addFirst(startPosition);
        direction = Direction.RIGHT;
    }

    /**
     * Возвращает голову змейки.
     *
     * @return голова змейки
     */
    public Point getHead() {
        return body.peekFirst();
    }

    /**
     * Возвращает тело змейки.
     *
     * @return список сегментов
     */
    public List<Point> getBody() {
        return List.copyOf(body);
    }

    /**
     * Возвращает длину змейки.
     *
     * @return длина змейки
     */
    public int getLength() {
        return body.size();
    }

    /**
     * Возвращает текущее направление.
     *
     * @return направление
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Устанавливает направление.
     *
     * @param newDirection новое направление
     */
    public void setDirection(Direction newDirection) {
        if (!newDirection.isOpposite(this.direction)) {
            this.direction = newDirection;
        }
    }

    /**
     * Вычисляет следующую позицию головы.
     *
     * @return следующая позиция
     */
    public Point nextHeadPosition() {
        return getHead().translate(direction.getDx(), direction.getDy());
    }

    /**
     * Выполняет один шаг движения.
     */
    public void step() {
        body.addFirst(nextHeadPosition());
        if (pendingGrowth > 0) {
            pendingGrowth--;
        } else {
            body.removeLast();
        }
    }

    /**
     * Добавляет рост змейки.
     *
     * @param amount количество сегментов роста
     */
    public void addGrowth(int amount) {
        if (amount > 0) pendingGrowth += amount;
    }

    /**
     * Проверяет принадлежность точки телу змейки.
     *
     * @param p точка
     * @return true, если точка принадлежит телу
     */
    public boolean contains(Point p) {
        return body.contains(p);
    }
}
