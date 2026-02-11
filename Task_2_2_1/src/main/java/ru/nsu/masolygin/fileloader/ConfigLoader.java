package ru.nsu.masolygin.fileloader;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import ru.nsu.masolygin.exception.ConfigLoadException;

/**
 * Загрузчик конфигурации из json файла.
 */
public class ConfigLoader {

    /**
     * Загружает конфигурацию из файла.
     *
     * @param path путь к файлу конфигурации
     * @return конфигурация пиццерии
     * @throws ConfigLoadException если не удалось загрузить конфигурацию
     */
    public PizzeriaConfig load(String path) throws ConfigLoadException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(new File(path), PizzeriaConfig.class);
        } catch (IOException e) {
            throw new ConfigLoadException("Failed to load config from: " + path, e);
        }
    }
}