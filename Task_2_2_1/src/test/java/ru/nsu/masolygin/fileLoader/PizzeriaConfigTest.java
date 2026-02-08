package ru.nsu.masolygin.fileLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class PizzeriaConfigTest {

    @Test
    void testPizzeriaConfigCreation() {
        PizzeriaConfig config = new PizzeriaConfig();
        assertNotNull(config);
    }

    @Test
    void testSetAndGetWorkTime() {
        PizzeriaConfig config = new PizzeriaConfig();
        config.setWorkTime(10000);
        assertEquals(10000, config.getWorkTime());
    }

    @Test
    void testSetAndGetWarehouseCapacity() {
        PizzeriaConfig config = new PizzeriaConfig();
        config.setWarehouseCapacity(20);
        assertEquals(20, config.getWarehouseCapacity());
    }

    @Test
    void testSetAndGetBakers() {
        PizzeriaConfig config = new PizzeriaConfig();
        List<PizzeriaConfig.BakerConfig> bakers = new ArrayList<>();
        config.setBakers(bakers);
        assertNotNull(config.getBakers());
    }

    @Test
    void testSetAndGetCouriers() {
        PizzeriaConfig config = new PizzeriaConfig();
        List<PizzeriaConfig.CourierConfig> couriers = new ArrayList<>();
        config.setCouriers(couriers);
        assertNotNull(config.getCouriers());
    }

    @Test
    void testWorkTimeWithDifferentValues() {
        PizzeriaConfig config = new PizzeriaConfig();
        config.setWorkTime(5000);
        assertEquals(5000, config.getWorkTime());
        config.setWorkTime(15000);
        assertEquals(15000, config.getWorkTime());
    }

    @Test
    void testWarehouseCapacityWithDifferentValues() {
        PizzeriaConfig config = new PizzeriaConfig();
        config.setWarehouseCapacity(5);
        assertEquals(5, config.getWarehouseCapacity());
        config.setWarehouseCapacity(50);
        assertEquals(50, config.getWarehouseCapacity());
    }

    @Test
    void testBakerConfigCreation() {
        PizzeriaConfig.BakerConfig bakerConfig = new PizzeriaConfig.BakerConfig();
        assertNotNull(bakerConfig);
    }

    @Test
    void testBakerConfigSetAndGetId() {
        PizzeriaConfig.BakerConfig bakerConfig = new PizzeriaConfig.BakerConfig();
        bakerConfig.setId(1);
        assertEquals(1, bakerConfig.getId());
    }

    @Test
    void testBakerConfigSetAndGetCookingTime() {
        PizzeriaConfig.BakerConfig bakerConfig = new PizzeriaConfig.BakerConfig();
        bakerConfig.setCookingTime(1000);
        assertEquals(1000, bakerConfig.getCookingTime());
    }

    @Test
    void testBakerConfigWithDifferentValues() {
        PizzeriaConfig.BakerConfig bakerConfig = new PizzeriaConfig.BakerConfig();
        bakerConfig.setId(5);
        bakerConfig.setCookingTime(2000);
        assertEquals(5, bakerConfig.getId());
        assertEquals(2000, bakerConfig.getCookingTime());
    }

    @Test
    void testCourierConfigCreation() {
        PizzeriaConfig.CourierConfig courierConfig = new PizzeriaConfig.CourierConfig();
        assertNotNull(courierConfig);
    }

    @Test
    void testCourierConfigSetAndGetId() {
        PizzeriaConfig.CourierConfig courierConfig = new PizzeriaConfig.CourierConfig();
        courierConfig.setId(1);
        assertEquals(1, courierConfig.getId());
    }

    @Test
    void testCourierConfigSetAndGetDeliveryTime() {
        PizzeriaConfig.CourierConfig courierConfig = new PizzeriaConfig.CourierConfig();
        courierConfig.setDeliveryTime(1500);
        assertEquals(1500, courierConfig.getDeliveryTime());
    }

    @Test
    void testCourierConfigSetAndGetBackpackCapacity() {
        PizzeriaConfig.CourierConfig courierConfig = new PizzeriaConfig.CourierConfig();
        courierConfig.setBackpackCapacity(5);
        assertEquals(5, courierConfig.getBackpackCapacity());
    }

    @Test
    void testCourierConfigWithDifferentValues() {
        PizzeriaConfig.CourierConfig courierConfig = new PizzeriaConfig.CourierConfig();
        courierConfig.setId(3);
        courierConfig.setDeliveryTime(2500);
        courierConfig.setBackpackCapacity(10);
        assertEquals(3, courierConfig.getId());
        assertEquals(2500, courierConfig.getDeliveryTime());
        assertEquals(10, courierConfig.getBackpackCapacity());
    }

    @Test
    void testMultipleBakerConfigs() {
        List<PizzeriaConfig.BakerConfig> bakers = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            PizzeriaConfig.BakerConfig baker = new PizzeriaConfig.BakerConfig();
            baker.setId(i);
            baker.setCookingTime(1000 * i);
            bakers.add(baker);
        }
        assertEquals(3, bakers.size());
    }

    @Test
    void testMultipleCourierConfigs() {
        List<PizzeriaConfig.CourierConfig> couriers = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            PizzeriaConfig.CourierConfig courier = new PizzeriaConfig.CourierConfig();
            courier.setId(i);
            courier.setDeliveryTime(1000 * i);
            courier.setBackpackCapacity(5 * i);
            couriers.add(courier);
        }
        assertEquals(3, couriers.size());
    }

    @Test
    void testCompleteConfig() {
        PizzeriaConfig config = new PizzeriaConfig();
        config.setWorkTime(10000);
        config.setWarehouseCapacity(15);

        List<PizzeriaConfig.BakerConfig> bakers = new ArrayList<>();
        PizzeriaConfig.BakerConfig baker = new PizzeriaConfig.BakerConfig();
        baker.setId(1);
        baker.setCookingTime(1000);
        bakers.add(baker);
        config.setBakers(bakers);

        List<PizzeriaConfig.CourierConfig> couriers = new ArrayList<>();
        PizzeriaConfig.CourierConfig courier = new PizzeriaConfig.CourierConfig();
        courier.setId(1);
        courier.setDeliveryTime(1500);
        courier.setBackpackCapacity(5);
        couriers.add(courier);
        config.setCouriers(couriers);

        assertEquals(10000, config.getWorkTime());
        assertEquals(15, config.getWarehouseCapacity());
        assertEquals(1, config.getBakers().size());
        assertEquals(1, config.getCouriers().size());
    }
}

