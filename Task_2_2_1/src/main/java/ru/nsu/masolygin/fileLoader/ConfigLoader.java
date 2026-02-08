package ru.nsu.masolygin.fileLoader;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class ConfigLoader {

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