package com.progmeleon.mycafe.controller;

import com.progmeleon.mycafe.model.Item;
import com.progmeleon.mycafe.model.Category;

import java.util.List;
import java.util.Scanner;

public class ItemController {
    private static List<Item> items;
    private static List<Category> categories;

    public ItemController(List<Item> items, List<Category> categories) {
        this.items = items;
        this.categories = categories;
    }

    public void manageItems() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n====== Item Management ======");
            System.out.println("1. Add Item");
            System.out.println("2. Delete Item");
            System.out.println("3. Update Item");
            System.out.println("4. Display Items");
            System.out.println("5. Back to Admin Menu");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addItem();
                    break;
                case 2:
                    deleteItem();
                    break;
                case 3:
                    updateItem();
                    break;
                case 4:
                    displayItems();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (true);
    }

    // Add item
    private void addItem() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter item name: ");
        String itemName = scanner.nextLine();
        System.out.print("Enter item price: ");
        double itemPrice = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline character
        System.out.print("Enter item category: ");
        String itemCategoryName = scanner.nextLine();

        int categoryId = getCategoryIDByName(itemCategoryName);

        if (categoryId != -1) {
            Item newItem = new Item(itemName, itemPrice, categoryId);
            items.add(newItem);
            FileHandler.saveDataToFile(items, "items.ser");
            System.out.println("Item added successfully.");
        } else {
            System.out.println("Category not found. Item not added.");
        }
    }

    // Delete item
    private void deleteItem() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter item name to delete: ");
        String itemName = scanner.nextLine();

        items.removeIf(item -> item.getItemName().equalsIgnoreCase(itemName));

        FileHandler.saveDataToFile(items, "items.ser");
        System.out.println("Item deleted successfully.");
    }

    // Update item
    private void updateItem() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter item name to update: ");
        String oldItemName = scanner.nextLine();
        System.out.print("Enter new item name: ");
        String newItemName = scanner.nextLine();
        System.out.print("Enter new item price: ");
        double newItemPrice = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline character
        System.out.print("Enter new item category: ");
        String newItemCategoryName = scanner.nextLine();

        int newCategoryId = getCategoryIDByName(newItemCategoryName);

        for (Item item : items) {
            if (item.getItemName().equalsIgnoreCase(oldItemName)) {
                item.setItemName(newItemName);
                item.setItemPrice(newItemPrice);
                item.setCategoryId(newCategoryId);
                FileHandler.saveDataToFile(items, "items.ser");
                System.out.println("Item updated successfully.");
                return;
            }
        }

        System.out.println("Item not found.");
    }

    // Display items
    private void displayItems() {
        System.out.println("\n====== Items ======");
        for (Item item : items) {
            System.out.println(item);
        }
    }

    public static void displayAllItems() {
        if (items.isEmpty()) {
            System.out.println("No items available.");
        } else {
            for (Item item : items) {
                System.out.println(item);
            }
        }
    }

    // Display items by category
    public static void displayItemsByCategory(String categoryName) {
        int categoryId = getCategoryIDByName(categoryName);

        if (categoryId != -1) {
            boolean found = false;

            for (Item item : items) {
                if (item.getCategoryId() == categoryId) {
                    System.out.println(item);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No items found in category: " + categoryName);
            }
        } else {
            System.out.println("Category not found: " + categoryName);
        }
    }

    // Get category ID by name
    private static int getCategoryIDByName(String categoryName) {
        for (Category category : categories) {
            if (category instanceof Category && category.getCategoryName().equalsIgnoreCase(categoryName)) {
                return category.getCategoryId();
            }
        }
        return -1; // Return -1 if the category is not found
    }
}
