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
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                manageItems();
            }

        } while (true);
    }

    // Add item
    public static void addItem(String itemName, double itemPrice, String itemCategoryName) {
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Enter item name: ");
//        String itemName = scanner.nextLine();
//        System.out.print("Enter item price: ");
//        double itemPrice = scanner.nextDouble();
//        scanner.nextLine(); // Consume the newline character
//        System.out.print("Enter item category: ");
//        String itemCategoryName = scanner.nextLine();

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
    private void deleteItem() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter item name to delete: ");
        String itemName = scanner.nextLine();

        // Use SQL query to delete the item
        String deleteQuery = "DELETE FROM item WHERE itemName = ?";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {

            preparedStatement.setString(1, itemName);

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

        return null; // Item not found
    }


//    public static void main(String[] args) {
//        launch();
//    }
//
//    @Override
//    public void start(Stage primaryStage) {
//        AnchorPane root = new AnchorPane();
//        root.getStyleClass().addAll("card", "shadow");
//
//        VBox vBox = new VBox();
//        vBox.setPrefSize(230, 180);
//        vBox.setLayoutX(65);
//        vBox.setLayoutY(-10);
//
//        String[] productNames = {"Spaghetti", "Pizza", "Burger"};
//        double[] productPrices = {10.0, 15.0, 5.0};
//        String[] imagePaths = {"1.png", "1.png", "1.png"};
//
//        for (int i = 0; i < productNames.length; i++) {
//            showItems(vBox, productNames[i], productPrices[i], imagePaths[i]);
//        }
//
//        root.getChildren().add(vBox);
//
//        Scene scene = new Scene(root);
//        scene.getStylesheets().add("cardDesign.css");
//
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//    private void showItems(VBox parent, String productName, double productPrice, String imagePath) {
//        HBox headerHBox = new HBox();
//        headerHBox.setPrefSize(230, 29);
//
//        Label prodNameLabel = new Label(productName);
//        prodNameLabel.setPrefSize(151, 21);
//        prodNameLabel.setFont(new Font(15));
//        HBox.setMargin(prodNameLabel, new Insets(0, 0, 0, 25));
//
//        Label prodPriceLabel = new Label(String.format("$%.2f", productPrice));
//        prodPriceLabel.setPrefSize(86, 21);
//        prodPriceLabel.setFont(new Font(15));
//        HBox.setMargin(prodPriceLabel, new Insets(0, 25, 0, 0));
//
//        headerHBox.getChildren().addAll(prodNameLabel, prodPriceLabel);
//
//        AnchorPane imageAnchorPane = new AnchorPane();
//        imageAnchorPane.setPrefSize(230, 101);
//
//        ImageView prodImageView = new ImageView(new Image(imagePath));
//        prodImageView.setFitHeight(94);
//        prodImageView.setFitWidth(190);
//        prodImageView.setLayoutX(20);
//        prodImageView.setLayoutY(4);
//        imageAnchorPane.getChildren().add(prodImageView);
//
//        HBox buttonHBox = new HBox();
//        buttonHBox.setPrefSize(230, 48);
//
//        Spinner<Integer> prodSpinner = new Spinner<>();
//        prodSpinner.setPrefSize(100, 25);
//        HBox.setMargin(prodSpinner, new Insets(0, 10, 0, 0));
//
//        Button prodAddBtn = new Button("Add");
//        prodAddBtn.setPrefSize(81, 25);
//        prodAddBtn.getStyleClass().add("btn");
//
//        buttonHBox.getChildren().addAll(prodSpinner, prodAddBtn);
//
//        VBox productVBox = new VBox();
//        productVBox.getChildren().addAll(headerHBox, imageAnchorPane, buttonHBox);
//
//        parent.getChildren().add(productVBox);
//    }



}
