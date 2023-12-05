package com.progmeleon.mycafe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileHandler {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> List<T> loadData(Class<T> valueType, String filePath) {
        File file = new File(filePath);

        if (file.exists() && file.length() > 0) {
            try {
                return objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, valueType));
            } catch (IOException e) {
                e.printStackTrace();  // Consider logging the exception
            }
        } else {
            System.err.println("Warning: File not found or empty: " + filePath);
            UserController.createDefaultAdminUser();
        }

        return List.of();  // Return an empty list by default
    }



    public static <T> void saveDataToFile(List<T> data,String filePath) {
        try {
            objectMapper.writeValue(new File(filePath), data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
