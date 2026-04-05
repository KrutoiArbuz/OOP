package ru.nsu.masolygin.model.bot;

import ru.nsu.masolygin.model.Direction;
import ru.nsu.masolygin.model.GameModel;
import ru.nsu.masolygin.model.Point;
import ru.nsu.masolygin.model.Snake;
import ru.nsu.masolygin.model.bot.strategy.AbstractBotStrategy;

/**
 * Змейка-бот.
 */
public class BotSnake extends Snake {

    private final String colorHex;
    private final int speedMs;
    private AbstractBotStrategy strategy;
    private boolean alive = true;

    /**
     * Конструктор.
     *
     * @param startPosition стартовая позиция
     * @param strategy      стратегия
     * @param colorHex      цвет
     * @param speedMs       скорость
     */
    public BotSnake(Point startPosition, AbstractBotStrategy strategy,
        String colorHex, int speedMs) {
        super(startPosition);
        this.strategy = strategy;
        this.colorHex = colorHex;
        this.speedMs = speedMs;
    }

    /**
     * Вычисляет направление бота.
     *
     * @param model игровая модель
     * @return направление
     */
    public Direction computeDirection(GameModel model) {
        return strategy.chooseDirection(this, model);
    }

    /**
     * Помечает бота мертвым.
     */
    public void kill() {
        alive = false;
    }

    /**
     * Возвращает состояние бота.
     *
     * @return true, если бот жив
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Возвращает скорость бота.
     *
     * @return скорость в миллисекундах
     */
    public int getSpeedMs() {
        return speedMs;
    }

    /**
     * Возвращает цвет бота.
     *
     * @return цвет в формате HEX
     */
    public String getColorHex() {
        return colorHex;
    }

    /**
     * Устанавливает стратегию бота.
     *
     * @param strategy стратегия
     */
    public void setStrategy(AbstractBotStrategy strategy) {
        this.strategy = strategy;
    }
}
