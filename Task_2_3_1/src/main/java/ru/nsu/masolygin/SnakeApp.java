package ru.nsu.masolygin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.nsu.masolygin.config.SnakeConfig;
import ru.nsu.masolygin.controller.GameController;
import ru.nsu.masolygin.controller.MenuController;

/**
 * Основное JavaFX-приложение игры.
 */
public class SnakeApp extends Application {

    private Stage primaryStage;
    private Scene menuScene;
    private GameController controller;

    /**
     * Точка входа JavaFX.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Запускает окно и инициализирует контроллер.
     *
     * @param primaryStage главное окно
     * @throws Exception ошибка загрузки интерфейса
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        FXMLLoader loader = new FXMLLoader(SnakeApp.class.getResource("/menu.fxml"));
        Parent menuRoot = loader.load();
        MenuController menuController = loader.getController();
        menuController.setApp(this);

        menuScene = new Scene(menuRoot, 900, 600);

        primaryStage.setTitle("Змейка");
        primaryStage.setScene(menuScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Запускает саму игру.
     */
    public void startGame(SnakeConfig config) {
        if (controller != null) {
            controller.cleanup();
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                SnakeApp.class.getResource("/game.fxml")
            );

            Parent root = fxmlLoader.load();

            controller = fxmlLoader.getController();
            controller.init(config, this);

            int sceneWidth = config.fieldWidth() * config.cellSize();
            int sceneHeight = config.fieldHeight() * config.cellSize() + 60;

            Scene gameScene = new Scene(root, sceneWidth, sceneHeight);
            gameScene.setOnKeyPressed(controller::handleKeyPress);

            primaryStage.setScene(gameScene);
            root.requestFocus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Возврат в меню.
     */
    public void returnToMenu() {
        if (controller != null) {
            controller.cleanup();
        }
        primaryStage.setScene(menuScene);
    }

    /**
     * Останавливает фоновые потоки при закрытии.
     */
    @Override
    public void stop() {
        if (controller != null) {
            controller.cleanup();
        }
    }
}
