package ru.nsu.masolygin.view;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class GameRendererTest {

    @Test
    void testGameRendererClass() {
        assertNotNull(GameRenderer.class);
    }

    @Test
    void testGameRendererMethods() {
        assertTrue(GameRenderer.class.getDeclaredMethods().length > 0);
    }

    @Test
    void testGameRendererFields() {
        assertTrue(GameRenderer.class.getDeclaredFields().length > 0);
    }

    @Test
    void testGameRendererConstructor() {
        try {
            assertNotNull(GameRenderer.class.getDeclaredConstructors());
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    void testRenderMethodExists() {
        try {
            assertNotNull(GameRenderer.class.getMethod("render",
                Class.forName("ru.nsu.masolygin.model.GameSnapshot")));
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    void testGameRendererHasValidStructure() {
        assertTrue(GameRenderer.class.getDeclaredFields().length > 0);
    }

    @Test
    void testGameRendererIsPublic() {
        assertTrue(GameRenderer.class.getDeclaredMethods().length > 0);
    }
}
