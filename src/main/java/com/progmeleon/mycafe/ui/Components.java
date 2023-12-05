package com.progmeleon.mycafe.ui;
import com.progmeleon.mycafe.controller.FileHandler;
import com.progmeleon.mycafe.controller.InventorySystem;
import com.progmeleon.mycafe.model.Category;
import com.progmeleon.mycafe.model.Item;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.progmeleon.mycafe.model.User;
import com.progmeleon.mycafe.model.UserRole;
import com.progmeleon.mycafe.controller.CategoryController;
import com.progmeleon.mycafe.controller.ItemController;

import static com.progmeleon.mycafe.controller.ItemController.*;
import static com.progmeleon.mycafe.ui.SideBar.showNextScene;

public class Components extends Application {
    File data = new File("FileData.txt");
    private static List<User> users = new ArrayList<>();
    private User currentUser;
    private String currentUserRole;
    private InventorySystem inventorySystem;
    private static Stage primaryStage;
    private static List<Category> categories = new ArrayList<>();

    private static List<Item> items;


    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        BorderPane borderPane = createBorderPane();
        Scene scene = new Scene(borderPane, 900, 600, Color.WHITE);

        primaryStage.setTitle("My Cafe");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private BorderPane createBorderPane() {
        BorderPane borderPane = new BorderPane();

        Label anchorpaneLabel = new Label("My Cafe");
        anchorpaneLabel.setAlignment(Pos.CENTER);
        anchorpaneLabel.setStyle("-fx-text-fill: grey; -fx-font-weight: Bold;");
        anchorpaneLabel.setFont(Font.font("Ariel", 16));

        GridPane layout = userNameGridPane();

        borderPane.setCenter(layout);

        AnchorPane anchorPane = new AnchorPane();
        AnchorPane.setTopAnchor(anchorpaneLabel, 0.0);
        AnchorPane.setRightAnchor(anchorpaneLabel, 0.0);
        AnchorPane.setBottomAnchor(anchorpaneLabel, 0.0);
        AnchorPane.setLeftAnchor(anchorpaneLabel, 0.0);
        anchorPane.getChildren().add(anchorpaneLabel);
        anchorPane.setStyle("-fx-background-color: black;");
        anchorPane.setPrefWidth(200);
        borderPane.setLeft(anchorPane);

        return borderPane;
    }

    private GridPane userNameGridPane() {
        GridPane layout = new GridPane();
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setHgap(10);
        layout.setVgap(10);

        TextField username = new TextField();
        username.setPrefWidth(200);
        username.setPrefHeight(30);

        PasswordField password = new PasswordField();
        password.setPrefWidth(200);
        password.setPrefHeight(30);

        Label label = createLabel("Username");
        Label label1 = createLabel("Password");

        layout.add(label, 0, 0);
        layout.add(label1, 0, 1);
        layout.add(username, 2, 0);
        layout.add(password, 2, 1);
        layout.setAlignment(Pos.CENTER);

        Button register = createButton("Forgot Password");
        Button loginbutton = createButton("Login");
        loginbutton.setOnAction(e -> InventorySystem.authenticateUser(username.getText(), password.getText()));

        layout.add(register, 2, 2);
        layout.add(loginbutton, 2, 3);

        return layout;
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Times New Roman", 16));
        label.setStyle("-fx-font-weight: Bold");
        return label;
    }

    private Button createButton(String text) {
        Button button = new Button(text);
        button.setFont(new Font("Arial", 15));
        button.setStyle("-fx-text-fill: black; -fx-border-color: rgba(116,55,55,0); -fx-cursor: hand; -fx-background-color: transparent");
        button.setUnderline(true);
        return button;
    }


    private static void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Inventory System");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

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

        System.out.println("\n====== Categories ======");
        for (Category category : categories) {
            categoriesTextArea.appendText(category.getCategoryName() + "\n");
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
        manageItemsMenu.setAlignment(Pos.CENTER);

        Button addItemButton = new Button("Add Item");
        Button deleteItemButton = new Button("Delete Item");
        Button updateItemButton = new Button(" Update Item");
        Button displayItemButton = new Button("Display Item");
        Button backButton = new Button("Back to Admin Menu");

        // Set a fixed size for each button
        double buttonWidth = 180;
        double buttonHeight = 30;

        addItemButton.setOnAction(e -> addItem());
        deleteItemButton.setOnAction(e -> deleteItem());
        updateItemButton.setOnAction(e -> updateItem());
        displayItemButton.setOnAction(e -> displayItems());
        backButton.setOnAction(e -> showNextScene());

        addItemButton.setMinSize(buttonWidth, buttonHeight);
        deleteItemButton.setMinSize(buttonWidth, buttonHeight);
        updateItemButton.setMinSize(buttonWidth, buttonHeight);
        displayItemButton.setMinSize(buttonWidth, buttonHeight);
        backButton.setMinSize(buttonWidth, buttonHeight);

        manageItemsMenu.getChildren().addAll(
                addItemButton,
                deleteItemButton,
                updateItemButton,
                displayItemButton,
                backButton
        );
        ((BorderPane) primaryStage.getScene().getRoot()).setCenter(manageItemsMenu);

    }
    private static void addItem() {
        TextInputDialog itemNameDialog = new TextInputDialog();
        itemNameDialog.setTitle("Add Item");
        itemNameDialog.setHeaderText(null);
        itemNameDialog.setContentText("Enter item name:");

        itemNameDialog.showAndWait().ifPresent(itemName -> {
            TextInputDialog itemPriceDialog = new TextInputDialog();
            itemPriceDialog.setTitle("Add Item");
            itemPriceDialog.setHeaderText(null);
            itemPriceDialog.setContentText("Enter item price:");

            double itemPrice = Double.parseDouble(itemPriceDialog.showAndWait().orElse("0.0"));

            TextInputDialog itemCategoryDialog = new TextInputDialog();
            itemCategoryDialog.setTitle("Add Item");
            itemCategoryDialog.setHeaderText(null);
            itemCategoryDialog.setContentText("Enter item category:");

            String itemCategoryName = itemCategoryDialog.showAndWait().orElse("");

            int categoryId = getCategoryIDByName(itemCategoryName);

            if (categoryId != -1) {
                Item newItem = new Item(itemName, itemPrice, categoryId);
                items.add(newItem);
                FileHandler.saveDataToFile(items, "items.ser");
                showAlert("Item added successfully.", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Category not found. Item not added.", Alert.AlertType.ERROR);
            }
        });
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






    public static void main(String[] args) {
        launch();
    }
}
