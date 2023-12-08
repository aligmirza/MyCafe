package com.progmeleon.mycafe.ui;

import com.progmeleon.mycafe.config.ConfigureExistingData;
import com.progmeleon.mycafe.config.DBConnector;
import com.progmeleon.mycafe.controller.FileHandler;
import com.progmeleon.mycafe.controller.ItemController;
import com.progmeleon.mycafe.controller.ItemController.*;
import com.progmeleon.mycafe.model.Category;
import com.progmeleon.mycafe.model.Item;
import com.progmeleon.mycafe.model.User;
import javafx.application.Platform;
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

import static com.progmeleon.mycafe.controller.CategoryController.categories;
import static com.progmeleon.mycafe.controller.ItemController.*;
import static com.progmeleon.mycafe.ui.Components.primaryStage;
import static com.progmeleon.mycafe.ui.Components.showAlert;
import static com.progmeleon.mycafe.ui.SideBar.showNextScene;

public class UserRoleInterface {


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
                    System.out.println("Edit item: " + item.getItemId());
                });

                deleteButton.setOnAction(event -> {
                    Item item = getTableView().getItems().get(getIndex());
                    ItemController.deleteItem(item.getItemId());
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

        ComboBox<Category> categoryComboBox = new ComboBox<>();
        Label categoryIdLabel = new Label("Choose Category:");
        categoryComboBox.setMaxWidth(200);
        categoryComboBox.getItems().clear();
        ConfigureExistingData.loadCategories();
        ObservableList<Category> cat = ConfigureExistingData.categories;
        categoryComboBox.getItems().addAll(cat);

        for (Category category : cat) {
            if (categoryComboBox.getItems().size() == cat.size()) {
                categoryComboBox.getItems().add(category);
            }
        }



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
            // Validate input fields
            if (validateInput(itemNameField.getText(), itemPriceField.getText(), categoryComboBox.getValue())) {
                // Retrieve the selected Category from the ComboBox
                Category selectedCategory = categoryComboBox.getValue();

                // Use the ID of the selected category when adding the item
                ItemController.addItem(itemNameField.getText(), Double.parseDouble(itemPriceField.getText()), selectedCategory.getCategoryName());
                itemList.add(new Item(itemNameField.getText(), Double.parseDouble(itemPriceField.getText()), selectedCategory.getCategoryId()));

                itemNameField.clear();
                itemPriceField.clear();
                // No need to call showManageItems() here, as it was causing recursion
            }
        });

        VBox root = new VBox(10, tableView, addItemForm);
        manageItemsMenu.getChildren().add(root);

        ((BorderPane) primaryStage.getScene().getRoot()).setCenter(manageItemsMenu);
    }

    // Validation method for input fields
    private static boolean validateInput(String itemName, String itemPrice, Category selectedCategory) {
        if (itemName.isEmpty() || itemPrice.isEmpty() || selectedCategory == null) {
            showAlert("Fill all fields correctly", Alert.AlertType.INFORMATION);
            System.out.println("Please fill in all fields and select a category.");
            return false;
        }

        try {
            Double.parseDouble(itemPrice);
        } catch (NumberFormatException e) {
            System.out.println("Invalid item price. Please enter a valid number.");
            return false;
        }

        return true;
    }
}