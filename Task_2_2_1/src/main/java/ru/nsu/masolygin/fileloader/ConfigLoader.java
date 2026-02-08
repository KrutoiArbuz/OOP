package ru.nsu.masolygin.fileloader;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

/**
 * Класс загрузки конфигурации.
 */
public class ConfigLoader {

    /**
     * Загружает конфигурацию из файла.
     *
     * @param path путь к файлу конфигурации
     * @return конфигурация пиццерии
     */
    public PizzeriaConfig load(String path) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(new File(path), PizzeriaConfig.class);
        } catch (IOException e) {
            System.err.println("Failed to load config: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}