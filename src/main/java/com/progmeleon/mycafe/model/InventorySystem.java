package com.progmeleon.mycafe.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class InventorySystem {
    private List<Category> categories;
    private List<Item> items;

    public InventorySystem() {
        this.categories = new ArrayList<>();
        this.items = new ArrayList<>();
        // Load existing data from files
        loadExistingData();
    }

    // Load existing data from files
    private void loadExistingData() {
        try (ObjectInputStream categoryInputStream = new ObjectInputStream(new FileInputStream("categories.ser"));
             ObjectInputStream itemInputStream = new ObjectInputStream(new FileInputStream("items.ser"))) {

            categories = (List<Category>) categoryInputStream.readObject();
            items = (List<Item>) itemInputStream.readObject();

            System.out.println("Data loaded successfully.");

        } catch (FileNotFoundException e) {
            System.out.println("No existing data found. Starting with an empty inventory.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Add category to the system
    public void addCategory(String categoryName) {
        // Check if the category already exists
        for (Category existingCategory : categories) {
            if (existingCategory.equalsIgnoreCase(categoryName)) {
                System.out.println("Category already exists.");
                return;
            }
        }

        Category category = new Category(categoryName);
        categories.add(category);
        System.out.println("Category added successfully.");
        saveData(); // Save modified data to files
    }

    // Add item to the system (avoiding duplicates)
    public void addItem(String itemName, double itemPrice, int categoryId) {
        // Check if the item already exists
        for (Item existingItem : items) {
            if (existingItem.equalsIgnoreCase(itemName, categoryId)) {
                // Update the existing item
                existingItem.setItemPrice(itemPrice);
                System.out.println("Item updated successfully.");
                saveData(); // Save modified data to files
                return;
            }
        }

        // If the item doesn't exist, add a new one
        Item newItem = new Item(itemName, itemPrice, categoryId);
        items.add(newItem);
        System.out.println("Item added successfully.");
        saveData(); // Save modified data to files
    }

    // Save data to files
    private void saveData() {
        try (ObjectOutputStream categoryOutputStream = new ObjectOutputStream(new FileOutputStream("categories.ser"));
             ObjectOutputStream itemOutputStream = new ObjectOutputStream(new FileOutputStream("items.ser"))) {

            categoryOutputStream.writeObject(categories);
            itemOutputStream.writeObject(items);

            System.out.println("Data saved successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Getters for categories and items
    public List<Category> getCategories() {
        return categories;
    }

    public List<Item> getItems() {
        return items;
    }
}
