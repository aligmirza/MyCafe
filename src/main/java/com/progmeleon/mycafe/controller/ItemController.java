package com.progmeleon.mycafe.controller;

import com.progmeleon.mycafe.config.DBConnector;
import com.progmeleon.mycafe.model.Item;
import com.progmeleon.mycafe.model.Category;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ItemController {

    public static List<Item> items;
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
            try {
                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
//                        addItem();
                        break;
                    case 2:
//                        deleteItem();
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
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                manageItems();
            }

        } while (true);
    }

    // Add item
    public static void addItem(String itemName, double itemPrice, String itemCategoryName) {


        int categoryId = getCategoryIDByName(itemCategoryName);

        if (categoryId != -1) {
            // Use SQL query to insert the new item
            String insertQuery = "INSERT INTO item (itemName, itemPrice, categoryId) VALUES (?, ?, ?)";

            try (Connection connection = DBConnector.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

                preparedStatement.setString(1, itemName);
                preparedStatement.setDouble(2, itemPrice);
                preparedStatement.setInt(3, categoryId);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Item added successfully.");
                } else {
                    System.out.println("Failed to add item.");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Category not found. Item not added.");
        }
    }

    public static int getCategoryIDByName(String categoryName) {
        // Use SQL query to select the category ID by name
        String selectQuery = "SELECT id FROM category WHERE categoryName = ?";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setString(1, categoryName);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id");
            } else {
                return -1; // Category not found
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }


    // Delete item
    public static void deleteItem(int id) {
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Enter item name to delete: ");
//        String itemName = scanner.nextLine();

        // Use SQL query to delete the item
        String deleteQuery = "DELETE FROM item WHERE id = ?";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {

            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Item deleted successfully.");
            } else {
                System.out.println("Item not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
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

        // Use SQL query to update the item
        String updateQuery = "UPDATE item SET itemName = ?, itemPrice = ?, categoryId = ? WHERE itemName = ?";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            preparedStatement.setString(1, newItemName);
            preparedStatement.setDouble(2, newItemPrice);
            preparedStatement.setInt(3, newCategoryId);
            preparedStatement.setString(4, oldItemName);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Item updated successfully.");
            } else {
                System.out.println("Item not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Display items
    private void displayItems() {
        // Use SQL query to select and display all items
        String selectQuery = "SELECT * FROM item";

        try (Connection connection = DBConnector.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectQuery)) {

            System.out.println("\n====== Items ======");
            while (resultSet.next()) {
                System.out.println("Item ID: " + resultSet.getInt("id"));
                System.out.println("Item Name: " + resultSet.getString("itemName"));
                System.out.println("Item Price: " + resultSet.getDouble("itemPrice"));
                // Add other item details as needed
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Display all items
    public static void displayAllItems() {
        // Use SQL query to select and display all items
        String selectQuery = "SELECT * FROM item";

        try (Connection connection = DBConnector.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectQuery)) {

            if (resultSet.next()) {
                System.out.println("\n====== Items ======");
                do {
                    System.out.println("Item ID: " + resultSet.getInt("id")+" "+"Item Name: " + resultSet.getString("itemName")+" "+"Item Price: " + resultSet.getDouble("itemPrice"));
                } while (resultSet.next());
            } else {
                System.out.println("No items available.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
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

    public static Item findItemByName(String itemName) {
        // Use SQL query to select item by name
        String selectItemQuery = "SELECT * FROM item WHERE itemName = ?";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectItemQuery)) {

            preparedStatement.setString(1, itemName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
//                    int itemId = resultSet.getInt("itemId");
                    double itemPrice = resultSet.getDouble("itemPrice");
                    int categoryId = resultSet.getInt("categoryId");

                    // Create and return the Item object
                    return new Item( itemName, itemPrice, categoryId);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}