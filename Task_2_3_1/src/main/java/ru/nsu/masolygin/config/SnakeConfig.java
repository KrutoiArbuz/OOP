package ru.nsu.masolygin.config;

import java.util.Collections;
import java.util.List;

/**
 * Конфигурация игры.
 */
public class SnakeConfig {

    private int fieldWidth    = 25;
    private int fieldHeight   = 25;
    private int cellSize      = 26;
    private int foodCount     = 3;
    private int winLength     = 20;
    private int initialSpeedMs = 200;
    private List<ObstacleConfig> obstacles = Collections.emptyList();
    private List<BotConfig> bots = Collections.emptyList();

    /**
     * Конструктор.
     */
    public SnakeConfig() {}

    /**
     * Возвращает ширину поля.
     *
     * @return ширина поля
     */
    public int getFieldWidth() { return fieldWidth; }

    /**
     * Устанавливает ширину поля.
     *
     * @param v ширина поля
     */
    public void setFieldWidth(int v) { fieldWidth = v; }

    /**
     * Возвращает высоту поля.
     *
     * @return высота поля
     */
    public int getFieldHeight() { return fieldHeight; }

    /**
     * Устанавливает высоту поля.
     *
     * @param v высота поля
     */
    public void setFieldHeight(int v) { fieldHeight = v; }

    /**
     * Возвращает размер клетки.
     *
     * @return размер клетки
     */
    public int getCellSize() { return cellSize; }

    /**
     * Устанавливает размер клетки.
     *
     * @param v размер клетки
     */
    public void setCellSize(int v) { cellSize = v; }

    /**
     * Возвращает количество еды.
     *
     * @return количество еды
     */
    public int getFoodCount() { return foodCount; }

    /**
     * Устанавливает количество еды.
     *
     * @param v количество еды
     */
    public void setFoodCount(int v) { foodCount = v; }

    /**
     * Возвращает длину для победы.
     *
     * @return длина для победы
     */
    public int getWinLength() { return winLength; }

    /**
     * Устанавливает длину для победы.
     *
     * @param v длина для победы
     */
    public void setWinLength(int v) { winLength = v; }

    /**
     * Возвращает начальную скорость.
     *
     * @return скорость в миллисекундах
     */
    public int getInitialSpeedMs() { return initialSpeedMs; }

    /**
     * Устанавливает начальную скорость.
     *
     * @param v скорость в миллисекундах
     */
    public void setInitialSpeedMs(int v) { initialSpeedMs = v; }

    /**
     * Возвращает список препятствий.
     *
     * @return список препятствий
     */
    public List<ObstacleConfig> getObstacles() { return obstacles; }

    /**
     * Устанавливает список препятствий.
     *
     * @param v список препятствий
     */
    public void setObstacles(List<ObstacleConfig> v) { obstacles = v; }

    /**
     * Возвращает список ботов.
     *
     * @return список ботов
     */
    public List<BotConfig> getBots() { return bots; }

    /**
     * Устанавливает список ботов.
     *
     * @param v список ботов
     */
    public void setBots(List<BotConfig> v) { bots = v; }

    /**
     * Конфигурация одного препятствия.
     */
    public static class ObstacleConfig {
        private int x;
        private int y;

        /**
         * Конструктор.
         */
        public ObstacleConfig() {}

        /**
         * Возвращает координату X.
         *
         * @return координата X
         */
        public int getX() { return x; }

        /**
         * Устанавливает координату X.
         *
         * @param x координата X
         */
        public void setX(int x) { this.x = x; }

        /**
         * Возвращает координату Y.
         *
         * @return координата Y
         */
        public int getY() { return y; }

        /**
         * Устанавливает координату Y.
         *
         * @param y координата Y
         */
        public void setY(int y) { this.y = y; }
    }

    /**
     * Конфигурация одного бота.
     */
    public static class BotConfig {
        private int startX;
        private int startY;
        private String strategy = "RANDOM";
        private String colorHex = "#3498db";
        private int speedMs = 200;

        /**
         * Конструктор.
         */
        public BotConfig() {}

        /**
         * Возвращает стартовую X.
         *
         * @return стартовая X
         */
        public int getStartX() { return startX; }

        /**
         * Устанавливает стартовую X.
         *
         * @param v стартовая X
         */
        public void setStartX(int v) { startX = v; }

        /**
         * Возвращает стартовую Y.
         *
         * @return стартовая Y
         */
        public int getStartY() { return startY; }

        /**
         * Устанавливает стартовую Y.
         *
         * @param v стартовая Y
         */
        public void setStartY(int v) { startY = v; }

        /**
         * Возвращает тип стратегии.
         *
         * @return тип стратегии
         */
        public String getStrategy() { return strategy; }

        /**
         * Устанавливает тип стратегии.
         *
         * @param v тип стратегии
         */
        public void setStrategy(String v) { strategy = v; }

        /**
         * Возвращает цвет бота.
         *
         * @return цвет в формате HEX
         */
        public String getColorHex() { return colorHex; }

        /**
         * Устанавливает цвет бота.
         *
         * @param v цвет в формате HEX
         */
        public void setColorHex(String v) { colorHex = v; }

        /**
         * Возвращает скорость бота.
         *
         * @return скорость в миллисекундах
         */
        public int getSpeedMs() { return speedMs; }

        /**
         * Устанавливает скорость бота.
         *
         * @param v скорость в миллисекундах
         */
        public void setSpeedMs(int v) { speedMs = v; }
    }
}
