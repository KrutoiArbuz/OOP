package ru.nsu.masolygin.model;

import java.util.List;
import ru.nsu.masolygin.model.food.Food;
import ru.nsu.masolygin.model.obstacle.Obstacle;

/**
 * Снимок состояния игры.
 */
public record GameSnapshot(
        List<Point> playerBody,
        List<BotSnapshot> bots,
        List<Food> foods,
        List<Obstacle> obstacles,
        GameState state,
        int playerLength,
        int winLength,
        int speedMs,
        int speedEffectTicks
) {

    /**
     * Снимок одного бота.
     */
    public record BotSnapshot(List<Point> body, String colorHex, boolean alive) {}
}
