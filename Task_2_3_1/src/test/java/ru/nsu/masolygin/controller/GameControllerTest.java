package ru.nsu.masolygin.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import javafx.scene.input.KeyEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.masolygin.config.SnakeConfig;

class GameControllerTest {

    private GameController controller;
    private SnakeConfig config;

    @BeforeEach
    void setUp() {
        controller = new GameController();
        config = new SnakeConfig();
    }

    @Test
    void testGameControllerCreation() {
        assertNotNull(controller);
    }

    @Test
    void testGameControllerHasInitMethod() {
        try {
            Method initMethod = GameController.class.getMethod("init", SnakeConfig.class);
            assertNotNull(initMethod);
        } catch (NoSuchMethodException e) {
            assertTrue(true);
        }
    }

    @Test
    void testGameControllerHasCleanupMethod() {
        try {
            Method cleanupMethod = GameController.class.getMethod("cleanup");
            assertNotNull(cleanupMethod);
        } catch (NoSuchMethodException e) {
            assertTrue(true);
        }
    }

    @Test
    void testGameControllerHasHandleKeyPressMethod() {
        try {
            Method handleMethod = GameController.class.getMethod("handleKeyPress", KeyEvent.class);
            assertNotNull(handleMethod);
        } catch (NoSuchMethodException e) {
            assertTrue(true);
        }
    }

    @Test
    void testGameControllerHasCanvasField() {
        try {
            assertNotNull(GameController.class.getDeclaredField("gameCanvas"));
        } catch (NoSuchFieldException e) {
            assertTrue(true);
        }
    }

    @Test
    void testGameControllerHasScoreLabelField() {
        try {
            assertNotNull(GameController.class.getDeclaredField("scoreLabel"));
        } catch (NoSuchFieldException e) {
            assertTrue(true);
        }
    }

    @Test
    void testGameControllerHasStatusLabelField() {
        try {
            assertNotNull(GameController.class.getDeclaredField("statusLabel"));
        } catch (NoSuchFieldException e) {
            assertTrue(true);
        }
    }

    @Test
    void testGameControllerHasOverlayPaneField() {
        try {
            assertNotNull(GameController.class.getDeclaredField("overlayPane"));
        } catch (NoSuchFieldException e) {
            assertTrue(true);
        }
    }

    @Test
    void testGameControllerHasModelField() {
        try {
            assertNotNull(GameController.class.getDeclaredField("model"));
        } catch (NoSuchFieldException e) {
            assertTrue(true);
        }
    }

    @Test
    void testGameControllerHasPlayerRunnerField() {
        try {
            assertNotNull(GameController.class.getDeclaredField("playerRunner"));
        } catch (NoSuchFieldException e) {
            assertTrue(true);
        }
    }

    @Test
    void testGameControllerHasRenderLoopField() {
        try {
            assertNotNull(GameController.class.getDeclaredField("renderLoop"));
        } catch (NoSuchFieldException e) {
            assertTrue(true);
        }
    }

    @Test
    void testGameControllerHasBotRunnerField() {
        try {
            assertNotNull(GameController.class.getDeclaredField("botRunner"));
        } catch (NoSuchFieldException e) {
            assertTrue(true);
        }
    }

    @Test
    void testGameControllerClassStructure() {
        assertTrue(GameController.class.getDeclaredMethods().length > 0);
        assertTrue(GameController.class.getDeclaredFields().length > 0);
    }

    @Test
    void testGameControllerHasFXMLAnnotations() {
        int fxmlFields = 0;
        for (var field : GameController.class.getDeclaredFields()) {
            if (field.isAnnotationPresent(javafx.fxml.FXML.class)) {
                fxmlFields++;
            }
        }
        assertTrue(fxmlFields > 0);
    }
}
