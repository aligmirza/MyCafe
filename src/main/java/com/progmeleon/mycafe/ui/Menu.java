package com.progmeleon.mycafe.ui;

import com.progmeleon.mycafe.config.DBConnector;
import com.progmeleon.mycafe.model.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.progmeleon.mycafe.ui.Components.primaryStage;

public class Menu {
    private static ObservableList<Item> itemList = FXCollections.observableArrayList();
    private static ObservableList<CartItem> cartItemList = FXCollections.observableArrayList();

    // Create a TableView to display selected items with quantities
    private static TableView<CartItem> tableView = new TableView<>();

    // Create a label to display the total bill
    private static Label totalLabel = new Label("Total: PKR 0.0");

    public static void showMenuItems() {
        HBox mainLayout = new HBox(20);

        // Create an HBox for menu items
        VBox manageItemsMenu = new VBox(10);
        itemList.setAll(fetchItemsFromDatabase());

        for (Item item : itemList) {
            HBox menuItemHBox = new HBox();
            VBox menuItemBox = menuItems(item.getItemName(), item.getItemPrice(), item);
            menuItemHBox.getChildren().add(menuItemBox);
            manageItemsMenu.getChildren().add(menuItemHBox);
        }

        // Create VBox for TableView
        VBox tableViewVBox = new VBox();
        tableViewVBox.getChildren().add(new Label("Selected Items:"));

        // Define columns for TableView
        TableColumn<CartItem, String> itemNameColumn = new TableColumn<>("Item Name");
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));

        TableColumn<CartItem, Double> itemPriceColumn = new TableColumn<>("Item Price");
        itemPriceColumn.setCellValueFactory(new PropertyValueFactory<>("itemPrice"));

        TableColumn<CartItem, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        tableView.getColumns().addAll(itemNameColumn, itemPriceColumn, quantityColumn);
        Button checkoutButton = new Button("Generate Bill");
        checkoutButton.setOnAction(e -> printReceipt());

        tableViewVBox.getChildren().addAll(tableView, checkoutButton,totalLabel);


        mainLayout.getChildren().addAll(manageItemsMenu, tableViewVBox);
        ((BorderPane) primaryStage.getScene().getRoot()).setCenter(mainLayout);
    }

    public static List<Item> fetchItemsFromDatabase() {
        List<Item> items = new ArrayList<>();

        String selectItemsQuery = "SELECT * FROM item";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectItemsQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Item item = new Item();
                item.setItemName(resultSet.getString("itemName"));
                item.setItemPrice(resultSet.getInt("itemPrice"));
                items.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

    public static VBox menuItems(String itemName, double itemPrice, Item item) {
        VBox menuItemBox = new VBox(10);
        menuItemBox.setMaxWidth(200);

        menuItemBox.setStyle("-fx-border-color: red");
        menuItemBox.setPadding(new Insets(20, 10, 10, 20));
        menuItemBox.setBackground(Background.fill(Color.WHITE));
        menuItemBox.setPadding(new Insets(10));
        Label itemNameLabel = new Label(itemName);
        Label itemPriceLabel = new Label("Price: PKR" + itemPrice);
        Spinner<Integer> quantitySpinner = new Spinner<>(0, Integer.MAX_VALUE, 0);
        Button addToCartButton = new Button("Add to Cart");

        // Event handler to add the selected item to the TableView
        addToCartButton.setOnAction(event -> {
            int quantity = quantitySpinner.getValue();
            if (quantity > 0) {
                // Create a CartItem wrapper for the Item and its quantity
                CartItem cartItem = new CartItem(item, quantity);
                cartItemList.add(cartItem);
                tableView.getItems().setAll(cartItemList);

                // Update total label
                updateTotalLabel();
            }
        });

        menuItemBox.getChildren().addAll(itemNameLabel, itemPriceLabel, quantitySpinner, addToCartButton);
        return menuItemBox;
    }

    // Simple wrapper class to hold Item and its quantity
    public static class CartItem {
        private Item item;
        private int quantity;

        public CartItem(Item item, int quantity) {
            this.item = item;
            this.quantity = quantity;
        }

        public String getItemName() {
            return item.getItemName();
        }

        public double getItemPrice() {
            return item.getItemPrice();
        }

        public int getQuantity() {
            return quantity;
        }
    }

    // Update total label based on items in the cart
    private static void updateTotalLabel() {
        double total = cartItemList.stream()
                .mapToDouble(cartItem -> cartItem.getItemPrice() * cartItem.getQuantity())
                .sum();
        totalLabel.setText("Total: PKR " + total);
    }

    // Print the receipt
    public static void printReceipt() {
        StringBuilder receipt = new StringBuilder("Receipt:\n");
        for (CartItem cartItem : cartItemList) {
            receipt.append(cartItem.getItemName())
                    .append("\t")
                    .append(cartItem.getQuantity())
                    .append("\t")
                    .append("PKR ")
                    .append(cartItem.getItemPrice() * cartItem.getQuantity())
                    .append("\n");
        }
        receipt.append("\nTotal: PKR ").append(totalLabel.getText().substring(8));

        // You can modify this part to print or display the receipt as needed
        System.out.println(receipt.toString());
    }
}
