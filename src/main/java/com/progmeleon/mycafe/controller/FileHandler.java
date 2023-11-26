package com.progmeleon.mycafe.controller;

import java.io.*;
import java.util.List;

public class FileHandler {
    // Load data from file
    public static <T> List<T> loadDataFromFile(String filePath) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath))) {
            return (List<T>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Save data to file
    public static <T> void saveDataToFile(List<T> data, String filePath) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            outputStream.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
