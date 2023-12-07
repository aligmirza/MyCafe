package com.progmeleon.mycafe.ui;

import com.progmeleon.mycafe.config.ConfigureExistingData;
import com.progmeleon.mycafe.config.DBConnector;
import com.progmeleon.mycafe.controller.FileHandler;
import com.progmeleon.mycafe.controller.ItemController;
import com.progmeleon.mycafe.controller.ItemController.*;
import com.progmeleon.mycafe.model.Category;
import com.progmeleon.mycafe.model.Item;
import com.progmeleon.mycafe.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.progmeleon.mycafe.controller.CategoryController.categories;
//import static com.progmeleon.mycafe.controller.InventorySystem.currentUser;
//import static com.progmeleon.mycafe.controller.InventorySystem.currentUser;
import static com.progmeleon.mycafe.controller.ItemController.*;
import static com.progmeleon.mycafe.ui.Components.primaryStage;
import static com.progmeleon.mycafe.ui.Components.showAlert;
import static com.progmeleon.mycafe.ui.SideBar.showNextScene;

public class UserRoleInterface {
    static User currentUser;

    public static void showManageCategories() {
        // Create VBox for "Manage Categories" buttons
        VBox manageCategoriesMenu = new VBox(10);
        manageCategoriesMenu.setAlignment(Pos.CENTER);

        Button addCategoryButton = new Button("Add Category");
        Button deleteCategoryButton = new Button("Delete Category");
        Button updateCategoryButton = new Button(" Update Category");
        Button displayCategoriesButton = new Button("Display Categories");
        Button backButton = new Button("Back to Admin Menu");

        // Set a fixed size for each button
        double buttonWidth = 180;
        double buttonHeight = 30;

        addCategoryButton.setMinSize(buttonWidth, buttonHeight);
        deleteCategoryButton.setMinSize(buttonWidth, buttonHeight);
        updateCategoryButton.setMinSize(buttonWidth, buttonHeight);
        displayCategoriesButton.setMinSize(buttonWidth, buttonHeight);
        backButton.setMinSize(buttonWidth, buttonHeight);


        addCategoryButton.setOnAction(e -> addCategory());
        deleteCategoryButton.setOnAction(e -> deleteCategory());
        updateCategoryButton.setOnAction(e -> updateCategory());
        displayCategoriesButton.setOnAction(e -> displayCategories());
        backButton.setOnAction(e -> showNextScene()); // Go back to the main Admin Menu

        manageCategoriesMenu.getChildren().addAll(
                addCategoryButton,
                deleteCategoryButton,
                updateCategoryButton,
                displayCategoriesButton,
                backButton
        );

        ((BorderPane) primaryStage.getScene().getRoot()).setCenter(manageCategoriesMenu);
    }

    private static void addCategory() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Category");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter category name:");

        // Traditional way to get the response value.
        String categoryName = dialog.showAndWait().orElse(null);

        if (categoryName != null && !categoryName.isEmpty()) {
            Category newCategory = new Category(categoryName);
            categories.add(newCategory);
            // Save categories to file or perform other necessary actions
            System.out.println("Category added successfully: " + categoryName);
        } else {
            System.out.println("Invalid category name.");
        }
    }

    private static void deleteCategory() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Delete Category");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter category name to delete:");

        String categoryName = dialog.showAndWait().orElse(null);

        if (categoryName != null && !categoryName.isEmpty()) {
            categories.removeIf(category -> category.getCategoryName().equalsIgnoreCase(categoryName));

            // Save categories to file or perform other necessary actions
            System.out.println("Category deleted successfully: " + categoryName);
        } else {
            System.out.println("Invalid category name.");
        }
    }

    // Update category
    private static void updateCategory() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Update Category");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter category name to update:");

        String oldCategoryName = dialog.showAndWait().orElse(null);

        if (oldCategoryName != null && !oldCategoryName.isEmpty()) {
            // Find the category to update
            for (Category category : categories) {
                if (category.getCategoryName().equalsIgnoreCase(oldCategoryName)) {
                    // Show another dialog to get the new category name
                    TextInputDialog updateDialog = new TextInputDialog();
                    updateDialog.setTitle("Update Category");
                    updateDialog.setHeaderText(null);
                    updateDialog.setContentText("Enter new category name:");

                    String newCategoryName = updateDialog.showAndWait().orElse(null);

                    if (newCategoryName != null && !newCategoryName.isEmpty()) {
                        // Update the category
                        category.setCategoryName(newCategoryName);
                        // Save categories to file or perform other necessary actions
                        System.out.println("Category updated successfully: " + newCategoryName);
                        return;
                    } else {
                        System.out.println("Invalid new category name.");
                        return;
                    }
                }
            }

            System.out.println("Category not found.");
        } else {
            System.out.println("Invalid category name.");
        }
    }

    // Display categories
    private static void displayCategories() {
        // Create a TextArea to display categories
        TextArea categoriesTextArea = new TextArea();
        categoriesTextArea.setEditable(false);
        categoriesTextArea.setFont(Font.font("Arial", 14));

        String selectQuery = "SELECT * FROM category";

        try (Connection connection = DBConnector.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectQuery)) {

            System.out.println("\n====== Categories ======");
            while (resultSet.next()) {
                System.out.println("Category ID: " + resultSet.getInt("id"));
                System.out.println("Category Name: " + resultSet.getString("categoryName"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Create a new Stage to display the TextArea
        Stage categoriesStage = new Stage();
        categoriesStage.setTitle("Categories");
        Scene categoriesScene = new Scene(new BorderPane(categoriesTextArea), 300, 400);
        categoriesStage.setScene(categoriesScene);
        categoriesStage.show();
    }


    public static void showManageItems() {
        VBox manageItemsMenu = new VBox(10);

        ObservableList<Item> itemList = FXCollections.observableArrayList();

        String selectQuery = "SELECT * FROM item";

        try (Connection connection = DBConnector.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectQuery)) {

            while (resultSet.next()) {
                Item item = new Item();
                item.setItemId(resultSet.getInt("id"));
                item.setItemName(resultSet.getString("itemName"));
                item.setItemPrice(resultSet.getDouble("itemPrice"));
                item.setCategoryId(resultSet.getInt("categoryId"));
                itemList.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        TableView<Item> tableView = new TableView<>();

        TableColumn<Item, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("itemId"));

        TableColumn<Item, String> nameColumn = new TableColumn<>("Item Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));

        TableColumn<Item, Double> priceColumn = new TableColumn<>("Item Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("itemPrice"));

        TableColumn<Item, Integer> categoryIdColumn = new TableColumn<>("Category ID");
        categoryIdColumn.setCellValueFactory(new PropertyValueFactory<>("categoryId"));

        // Actions column with Edit and Delete buttons
        TableColumn<Item, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                editButton.setOnAction(event -> {
                    Item item = getTableView().getItems().get(getIndex());
                    // Add your edit action here
                    System.out.println("Edit item: " + item.getItemId());
                });

                deleteButton.setOnAction(event -> {
                    Item item = getTableView().getItems().get(getIndex());
                    // Add your delete action here
                    System.out.println("Delete item: " + item.getItemId());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(5, editButton, deleteButton);
                    setGraphic(buttons);
                }
            }
        });

        tableView.getColumns().addAll(idColumn, nameColumn, priceColumn, categoryIdColumn, actionsColumn);

        // Use an ObservableList for automatic updates
        ObservableList<Item> observableList = FXCollections.observableArrayList(itemList);
        tableView.setItems(observableList);

        // Form for adding items
        TextField itemNameField = new TextField();
        Label itemNameLabel = new Label("Item Name:");
        itemNameField.setMaxWidth(200);

        TextField itemPriceField = new TextField();
        Label itemPriceLabel = new Label("Item Price:");
        itemPriceField.setMaxWidth(200);

        ComboBox categoryComboBox = new ComboBox();
        Label categoryIdLabel = new Label("Chose Category:");
        categoryComboBox.setMaxWidth(200);

        ObservableList<Category> categories = FXCollections.observableArrayList(
                new Category(1, "Food"),
                new Category(2, "Drinks"),
                new Category(3, "Snacks")
        );


        // Add the categories to the ComboBox
        categoryComboBox.getItems().addAll(categories);


        Button addItemButton = new Button("Add Item");

        VBox addItemForm = new VBox();
        addItemForm.setPadding(new Insets(10));
        addItemForm.setAlignment(Pos.CENTER);
        addItemForm.setStyle("-fx-background-color: #f0f0f0;");
        addItemForm.setPrefWidth(300);
        addItemForm.setPrefHeight(200);

        addItemForm.getChildren().addAll(
                itemNameLabel, itemNameField,
                itemPriceLabel, itemPriceField,
                categoryIdLabel, categoryComboBox,
                addItemButton
        );

        addItemButton.setOnAction(event -> {
            // Call your addItems method with the form values
            ItemController.addItem(itemNameField.getText(), Double.parseDouble(itemPriceField.getText()), categoryComboBox.getSelectionModel().getSelectedItem().toString());

            // Update the ObservableList to reflect the changes
            itemList.add(new Item(itemNameField.getText(), Double.parseDouble(itemPriceField.getText()), Integer.parseInt(categoryComboBox.getSelectionModel().getSelectedItem().toString())));

            // Clear the form fields after adding the item
            itemNameField.clear();
            itemPriceField.clear();
//            categoryComboBox.clearSelection();
            showManageItems();
        });

        VBox root = new VBox(10, tableView, addItemForm);
        manageItemsMenu.getChildren().add(root);

        ((BorderPane) primaryStage.getScene().getRoot()).setCenter(manageItemsMenu);
    }


    // Delete item
    private static void deleteItem() {
        TextInputDialog itemNameDialog = new TextInputDialog();
        itemNameDialog.setTitle("Delete Item");
        itemNameDialog.setHeaderText(null);
        itemNameDialog.setContentText("Enter item name to delete:");

        itemNameDialog.showAndWait().ifPresent(itemName -> {
            items.removeIf(item -> item.getItemName().equalsIgnoreCase(itemName));
            FileHandler.saveDataToFile(items, "items.ser");
            showAlert("Item deleted successfully.", Alert.AlertType.INFORMATION);
        });
    }

    // Update item
    private static void updateItem() {
        TextInputDialog oldItemNameDialog = new TextInputDialog();
        oldItemNameDialog.setTitle("Update Item");
        oldItemNameDialog.setHeaderText(null);
        oldItemNameDialog.setContentText("Enter item name to update:");

        oldItemNameDialog.showAndWait().ifPresent(oldItemName -> {
            TextInputDialog newItemNameDialog = new TextInputDialog();
            newItemNameDialog.setTitle("Update Item");
            newItemNameDialog.setHeaderText(null);
            newItemNameDialog.setContentText("Enter new item name:");

            String newItemName = newItemNameDialog.showAndWait().orElse("");

            TextInputDialog newItemPriceDialog = new TextInputDialog();
            newItemPriceDialog.setTitle("Update Item");
            newItemPriceDialog.setHeaderText(null);
            newItemPriceDialog.setContentText("Enter new item price:");

            double newItemPrice = Double.parseDouble(newItemPriceDialog.showAndWait().orElse("0.0"));

            TextInputDialog newItemCategoryDialog = new TextInputDialog();
            newItemCategoryDialog.setTitle("Update Item");
            newItemCategoryDialog.setHeaderText(null);
            newItemCategoryDialog.setContentText("Enter new item category:");

            String newItemCategoryName = newItemCategoryDialog.showAndWait().orElse("");

            int newCategoryId = getCategoryIDByName(newItemCategoryName);

            for (Item item : items) {
                if (item.getItemName().equalsIgnoreCase(oldItemName)) {
                    item.setItemName(newItemName);
                    item.setItemPrice(newItemPrice);
                    item.setCategoryId(newCategoryId);
                    FileHandler.saveDataToFile(items, "items.ser");
                    showAlert("Item updated successfully.", Alert.AlertType.INFORMATION);
                    return;
                }
            }

            showAlert("Item not found.", Alert.AlertType.ERROR);
        });
    }

    // Display items
    private static void displayItems() {

        TextArea itemsTextArea = new TextArea();
        itemsTextArea.setEditable(false);
        itemsTextArea.setFont(Font.font("Arial", 14));
        System.out.println("\n====== Categories ======");
        for (Category category : categories) {
            itemsTextArea.appendText(category.getCategoryName() + "\n");
        }

        // Create a new Stage to display the TextArea
        Stage itemsStage = new Stage();
        itemsStage.setTitle("Categories");
        Scene categoriesScene = new Scene(new BorderPane(itemsTextArea), 300, 400);
        itemsStage.setScene(categoriesScene);
        itemsStage.show();
    }

//    static void handleChangePassword() {
//        TextInputDialog oldPasswordDialog = new TextInputDialog();
////        Password passwordDialog = new PasswordDialog();
//        oldPasswordDialog.setHeaderText(null);
//        oldPasswordDialog.setTitle("Change Password");
//        oldPasswordDialog.setContentText("Enter your old password:");
//
//        Optional<String> oldPasswordResult = oldPasswordDialog.showAndWait();
//
//        oldPasswordResult.ifPresent(oldPassword -> {
//            if (checkOldPassword(oldPassword)) {
//                oldPasswordDialog.close();
//                TextInputDialog newPasswordDialog = new TextInputDialog();
//                newPasswordDialog.setHeaderText(null);
//                newPasswordDialog.setTitle("Change Password");
//                newPasswordDialog.setContentText("Enter your new password:");
//
//                newPasswordDialog.showAndWait().ifPresent(newPassword -> {
//                    showAlert("Password changed successfully.", Alert.AlertType.INFORMATION);
//                });
//            } else {
//                showAlert("Incorrect old password. Password not changed.", Alert.AlertType.ERROR);
//            }
//        });
//    }
//
//    private static boolean checkOldPassword(String oldPassword) {
//        // Assuming currentUser is the currently logged-in user.
//        User currentUser = InventorySystem.getCurrentUser();
//
//        if (currentUser != null && currentUser.getPassword().equals(oldPassword)) {
//            return true;
//        } else {
//            return false;
//        }
//    }
}