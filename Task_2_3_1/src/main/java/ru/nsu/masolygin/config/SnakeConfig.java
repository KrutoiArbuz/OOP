package ru.nsu.masolygin.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Конфигурация игры.
 */
public record SnakeConfig(
    @JsonProperty("fieldWidth") int fieldWidth,
    @JsonProperty("fieldHeight") int fieldHeight,
    @JsonProperty("cellSize") int cellSize,
    @JsonProperty("foodCount") int foodCount,
    @JsonProperty("winLength") int winLength,
    @JsonProperty("initialSpeedMs") int initialSpeedMs,
    @JsonProperty("playerEnabled") Boolean playerEnabled,
    @JsonProperty("obstacles") List<ObstacleConfig> obstacles,
    @JsonProperty("bots") List<BotConfig> bots,
    @JsonProperty("playerStartX") Integer playerStartX,
    @JsonProperty("playerStartY") Integer playerStartY
) {

    /**
     * Конструктор.
     */
    public SnakeConfig {
        if (playerEnabled == null) {
            playerEnabled = true;
        }
        if (obstacles == null) {
            obstacles = List.of();
        }
        if (bots == null) {
            bots = List.of();
        }
    }

    /**
     * Возвращает конфигурацию со стандартными параметрами.
     *
     * @return конфигурация
     */
    public static SnakeConfig defaults() {
        return new SnakeConfig(25, 25, 26, 3, 20, 200, true, List.of(), List.of(), null, null);
    }

    /**
     * Конфигурация препятствия.
     */
    public record ObstacleConfig(
        @JsonProperty("x") int x,
        @JsonProperty("y") int y
    ) {

    }

    /**
     * Конфигурация бота.
     */
    public record BotConfig(
        @JsonProperty("startX") int startX,
        @JsonProperty("startY") int startY,
        @JsonProperty("strategy") String strategy,
        @JsonProperty("colorHex") String colorHex,
        @JsonProperty("speedMs") int speedMs
    ) {

    }
}
