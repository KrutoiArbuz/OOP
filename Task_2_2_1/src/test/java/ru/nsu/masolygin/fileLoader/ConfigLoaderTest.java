package ru.nsu.masolygin.fileLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ConfigLoaderTest {

    @Test
    void testConfigLoaderCreation() {
        ConfigLoader loader = new ConfigLoader();
        assertNotNull(loader);
    }

    @Test
    void testLoadValidConfig() {
        ConfigLoader loader = new ConfigLoader();
        PizzeriaConfig config = loader.load("src/main/resources/config.json");

        assertNotNull(config);
        assertNotNull(config.getBakers());
        assertNotNull(config.getCouriers());
    }

    @Test
    void testLoadConfigWorkTime() {
        ConfigLoader loader = new ConfigLoader();
        PizzeriaConfig config = loader.load("src/main/resources/config.json");

        assertEquals(10000, config.getWorkTime());
    }

    @Test
    void testLoadConfigWarehouseCapacity() {
        ConfigLoader loader = new ConfigLoader();
        PizzeriaConfig config = loader.load("src/main/resources/config.json");

        assertEquals(10, config.getWarehouseCapacity());
    }

    @Test
    void testLoadConfigBakers() {
        ConfigLoader loader = new ConfigLoader();
        PizzeriaConfig config = loader.load("src/main/resources/config.json");

        assertNotNull(config.getBakers());
    }

    @Test
    void testLoadConfigCouriers() {
        ConfigLoader loader = new ConfigLoader();
        PizzeriaConfig config = loader.load("src/main/resources/config.json");

        assertNotNull(config.getCouriers());
    }

    @Test
    void testLoadNonExistentFile() {
        ConfigLoader loader = new ConfigLoader();
        assertThrows(RuntimeException.class, () -> loader.load("non_existent_file.json"));
    }

    @Test
    void testLoadInvalidPath() {
        ConfigLoader loader = new ConfigLoader();
        assertThrows(RuntimeException.class, () -> loader.load(""));
    }

    @Test
    void testLoadConfigMultipleTimes() {
        ConfigLoader loader = new ConfigLoader();
        PizzeriaConfig config1 = loader.load("src/main/resources/config.json");
        PizzeriaConfig config2 = loader.load("src/main/resources/config.json");

        assertNotNull(config1);
        assertNotNull(config2);
    }

    @Test
    void testMultipleLoaderInstances() {
        ConfigLoader loader1 = new ConfigLoader();
        ConfigLoader loader2 = new ConfigLoader();

        PizzeriaConfig config1 = loader1.load("src/main/resources/config.json");
        PizzeriaConfig config2 = loader2.load("src/main/resources/config.json");

        assertNotNull(config1);
        assertNotNull(config2);
    }
}

