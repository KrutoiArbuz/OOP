package ru.nsu.masolygin.view;

import javafx.scene.paint.Color;

/**
 * Типы ячеек в редакторе карты.
 */
public enum CellType {
    EMPTY(Color.BLACK),
    OBSTACLE(Color.GRAY),
    PLAYER(Color.web("#2ecc71")),
    BOT_GREEDY(Color.web("#3498db")),
    BOT_RANDOM(Color.web("#e73cdb")),
    BOT_WALL_HUGGER(Color.web("#e67e22"));

    private final Color color;

    CellType(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
