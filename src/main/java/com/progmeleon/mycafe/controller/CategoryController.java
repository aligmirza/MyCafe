package com.progmeleon.mycafe.controller;

import com.progmeleon.mycafe.config.ConfigureExistingData;
import com.progmeleon.mycafe.config.DBConnector;
import com.progmeleon.mycafe.model.Category;
import com.progmeleon.mycafe.model.Item;
import com.progmeleon.mycafe.model.User;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.sql.*;
import java.util.List;
import java.util.Scanner;

import static com.progmeleon.mycafe.ui.Components.primaryStage;
import static com.progmeleon.mycafe.ui.SideBar.showNextScene;

public class CategoryController {
    public static List<Category> categories;


    public CategoryController() {
        this.categories = ConfigureExistingData.categories;
    }

    public CategoryController(List<Category> categories) {
        this.categories = ConfigureExistingData.categories;
    }

    public void manageCategories() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n====== Category Management ======");
            System.out.println("1. Add Category");
            System.out.println("2. Delete Category");
            System.out.println("3. Update Category");
            System.out.println("4. Display Categories");
            System.out.println("5. Back to Admin Menu");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
//                    addCategory();
                    break;
                case 2:
//                    deleteCategory();
                    break;
                case 3:
                    updateCategory();
                    break;
                case 4:
                    displayCategories();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (true);
    }

    // Add category
    private static void addCategory(String categoryName) {
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Enter category name: ");
//        String categoryName = scanner.nextLine();

        // Use SQL query to add the category
        String insertQuery = "INSERT INTO category (categoryName) VALUES (?)";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            preparedStatement.setString(1, categoryName);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Category added successfully.");
            } else {
                System.out.println("Failed to add category.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete category
    private static void deleteCategory(String categoryName) {
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Enter category name to delete: ");
//        String categoryName = scanner.nextLine();

        // Use SQL query to delete the category
        String deleteQuery = "DELETE FROM category WHERE categoryName = ?";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {

            preparedStatement.setString(1, categoryName);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Category deleted successfully.");
            } else {
                System.out.println("Category not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update category
    private static void updateCategory() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter category name to update: ");
        String oldCategoryName = scanner.nextLine();
        System.out.print("Enter new category name: ");
        String newCategoryName = scanner.nextLine();

        // Use SQL query to update the category
        String updateQuery = "UPDATE category SET categoryName = ? WHERE categoryName = ?";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            preparedStatement.setString(1, newCategoryName);
            preparedStatement.setString(2, oldCategoryName);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Category updated successfully.");
            } else {
                System.out.println("Failed to update category.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Display categories
    public static void displayCategories() {
        VBox manageItemsMenu = new VBox(10);
        List<Category> categoryList = new ArrayList<>();

        String selectQuery = "SELECT * FROM category";

        try (Connection connection = DBConnector.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectQuery)) {

            while (resultSet.next()) {
                Category category = new Category();
                category.setCategoryId(resultSet.getInt("id"));
                category.setCategoryName(resultSet.getString("categoryName"));
                categoryList.add(category);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        TableView<Category> tableView = new TableView<>();

        TableColumn<Category, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("categoryId"));

        TableColumn<Category, String> nameColumn = new TableColumn<>("Category Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));

        TableColumn<Category, String> actionColumn = new TableColumn<>("Action");
//        actionColumn.setCellValueFactory(new PropertyValueFactory<>("action"));


        actionColumn.setCellFactory(param -> new TableCell<Category, String>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
        });



        tableView.getColumns().addAll(idColumn, nameColumn,actionColumn);

        ObservableList<Category> observableList = FXCollections.observableArrayList(categoryList);
        tableView.setItems(observableList);

        TextField itemNameField = new TextField();
        Label itemNameLabel = new Label("Category Name:");
        itemNameField.setMaxWidth(200);



        Button addItemButton = new Button("Add Category");

        VBox addItemForm = new VBox();
        addItemForm.setPadding(new Insets(10));
        addItemForm.setAlignment(Pos.CENTER);
        addItemForm.setStyle("-fx-background-color: #f0f0f0;");
        addItemForm.setPrefWidth(300);
        addItemForm.setPrefHeight(200);

        addItemForm.getChildren().addAll(
                itemNameLabel, itemNameField,
                addItemButton
        );

        addItemButton.setOnAction(event -> {
            // Call your addItems method with the form values
            addCategory(itemNameField.getText());

            // Update the ObservableList to reflect the changes
            categoryList.add(new Category(itemNameField.getText()));

            // Clear the form fields after adding the item
            itemNameField.clear();

            displayCategories();
        });
        VBox root = new VBox(10, tableView, addItemForm);
        manageItemsMenu.getChildren().add(root);
        ((BorderPane) primaryStage.getScene().getRoot()).setCenter(manageItemsMenu);
    }


    // Helper method to create buttons with a given label
    private static Button createButton(String label) {
        Button button = new Button(label);
        button.setStyle("-fx-cursor: hand;");
        return button;
    }




// Uncomment this line if you want to display the primary stage
// primaryStage.show();





    }

