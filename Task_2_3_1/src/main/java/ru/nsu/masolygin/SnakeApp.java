package ru.nsu.masolygin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.nsu.masolygin.config.ConfigLoader;
import ru.nsu.masolygin.config.SnakeConfig;
import ru.nsu.masolygin.controller.GameController;

/**
 * Основное JavaFX-приложение игры.
 */
public class SnakeApp extends Application {

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

        ConfigLoader configLoader = new ConfigLoader();
        SnakeConfig config = configLoader.load("/config.json");

        FXMLLoader fxmlLoader = new FXMLLoader(
            SnakeApp.class.getResource("/game.fxml")
        );

        Parent root = fxmlLoader.load();

        GameController controller = fxmlLoader.getController();
        controller.init(config);

        this.controller = controller;

        int sceneWidth = config.getFieldWidth() * config.getCellSize();
        int sceneHeight = config.getFieldHeight() * config.getCellSize() + 60;

        Scene scene = new Scene(root, sceneWidth, sceneHeight);

        scene.setOnKeyPressed(controller::handleKeyPress);

        primaryStage.setTitle("Змейка");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        root.requestFocus();
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
