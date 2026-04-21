package ru.nsu.masolygin.view;

import java.util.Map;
import java.util.function.BiConsumer;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import ru.nsu.masolygin.model.Point;

/**
 * Рендер меню игры. Управляет отображением поля и модалок.
 */
public class MenuRenderer {

    private final GridPane gridPane;
    private final StackPane errorOverlay;
    private final Label errorTitle;
    private final Label errorMessage;
    private final BiConsumer<Point, Rectangle> cellInteractedAction;

    /**
     * Конструктор.
     *
     * @param gridPane             панель сетки для отрисовки
     * @param errorOverlay         оверлей для ошибки
     * @param errorTitle           заголовок ошибки
     * @param errorMessage         текст ошибки
     * @param errorOkBtn           кнопка закрытия ошибки
     * @param cellInteractedAction действие при взаимодействии с клеткой
     */
    public MenuRenderer(GridPane gridPane, StackPane errorOverlay, Label errorTitle,
        Label errorMessage, Button errorOkBtn,
        BiConsumer<Point, Rectangle> cellInteractedAction) {
        this.gridPane = gridPane;
        this.errorOverlay = errorOverlay;
        this.errorTitle = errorTitle;
        this.errorMessage = errorMessage;
        this.cellInteractedAction = cellInteractedAction;

        if (errorOkBtn != null) {
            errorOkBtn.setOnAction(e -> hideErrorModal());
        }
    }

    /**
     * Отрисовывает сетку редактора с визуализацией доступной зоны спауна игрока.
     *
     * @param width         ширина поля
     * @param height        высота поля
     * @param cellMap       карта клеток с их типами
     * @param playerEnabled включен ли игрок
     * @param playerPos     позиция игрока (null если не расставлен)
     */
    public void renderGrid(int width, int height, Map<Point, CellType> cellMap,
        boolean playerEnabled, Point playerPos) {
        gridPane.getChildren().clear();

        double maxAvailableScreen = 500.0;
        int cellSize = (int) Math.min((maxAvailableScreen / width), (maxAvailableScreen / height));
        if (cellSize < 3) {
            cellSize = 3;
        }

        int zoneX = (playerPos != null) ? playerPos.getX() : width / 2;
        int zoneY = (playerPos != null) ? playerPos.getY() : height / 2;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Rectangle cell = new Rectangle(cellSize, cellSize, Color.BLACK);

                Point p = new Point(x, y);
                CellType existingType = cellMap.getOrDefault(p, CellType.EMPTY);
                cell.setFill(existingType.getColor());

                gridPane.add(cell, x, y);

                if (playerEnabled
                    && x >= zoneX - 1
                    && x <= zoneX + 1
                    && y >= zoneY - 1
                    && y <= zoneY + 1) {
                    Rectangle spawnOverlay = new Rectangle(cellSize, cellSize,
                        Color.color(0.18, 0.80, 0.44, 0.3));
                    gridPane.add(spawnOverlay, x, y);
                }

                cell.setOnMouseClicked(event -> cellInteractedAction.accept(p, cell));
                cell.setOnMouseEntered(event -> {
                    if (event.isPrimaryButtonDown()) {
                        cellInteractedAction.accept(p, cell);
                    }
                });
            }
        }
    }


    /**
     * Показывает ошибку нехватки пространства.
     */
    public void showNotEnoughSpaceError() {
        showErrorModal(
            "Ошибка конфигурации",
            "Слишком много объектов на поле или нет места 3x3!\n"
                + "Игроку требуется пространство 3x3 клетки вокруг себя.\n"
                + "Убедитесь, что вокруг случайного или выбранного старта свободно, "
                + "либо увеличьте поле."
        );
    }

    /**
     * Показывает ошибку избытка еды.
     *
     * @param freeCells количество свободных клеток (вне зоны спауна)
     */
    public void showTooMuchFoodError(int freeCells) {
        showErrorModal(
            "Ошибка конфигурации",
            "Слишком много еды!\nКоличество еды превышает свободное место (" + freeCells
                + " кл.).\nУменьшите количество еды или очистите поле."
        );
    }

    /**
     * Показывает модальное окно с ошибкой.
     *
     * @param title   заголовок
     * @param message сообщение
     */
    public void showErrorModal(String title, String message) {
        if (errorOverlay != null && errorTitle != null && errorMessage != null) {
            errorTitle.setText(title);
            errorMessage.setText(message);
            errorOverlay.setVisible(true);
        }
    }

    /**
     * Скрывает модальное окно.
     */
    public void hideErrorModal() {
        if (errorOverlay != null) {
            errorOverlay.setVisible(false);
        }
    }
}