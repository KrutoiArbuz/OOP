package ru.nsu.masolygin.view;

import java.util.Map;
import java.util.function.BiConsumer;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import ru.nsu.masolygin.model.Point;

/**
 * Рендерер сетки редактора карты в меню.
 */
public class MenuRenderer {

    private final GridPane gridPane;
    private final BiConsumer<Point, Rectangle> cellInteractedAction;

    /**
     * Конструктор.
     *
     * @param gridPane             панель сетки для отрисовки
     * @param cellInteractedAction действие при взаимодействии с клеткой
     */
    public MenuRenderer(GridPane gridPane, BiConsumer<Point, Rectangle> cellInteractedAction) {
        this.gridPane = gridPane;
        this.cellInteractedAction = cellInteractedAction;
    }

    /**
     * Отрисовывает сетку редактора.
     *
     * @param width   ширина поля
     * @param height  высота поля
     * @param cellMap карта клеток с их типами
     */
    public void renderGrid(int width, int height, Map<Point, CellType> cellMap) {
        gridPane.getChildren().clear();

        double maxAvailableScreen = 500.0;
        int cellSize = (int) Math.min((maxAvailableScreen / width), (maxAvailableScreen / height));
        if (cellSize < 3) {
            cellSize = 3;
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Rectangle cell = new Rectangle(cellSize, cellSize, Color.BLACK);

                Point p = new Point(x, y);
                CellType existingType = cellMap.getOrDefault(p, CellType.EMPTY);
                cell.setFill(existingType.getColor());

                cell.setOnMouseClicked(event -> cellInteractedAction.accept(p, cell));
                cell.setOnMouseEntered(event -> {
                    if (event.isPrimaryButtonDown()) {
                        cellInteractedAction.accept(p, cell);
                    }
                });

                gridPane.add(cell, x, y);
            }
        }
    }
}
