package com.progmeleon.mycafe.ui;

import com.progmeleon.mycafe.config.DBConnector;
import com.progmeleon.mycafe.model.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
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

    public static void showMenuItems() {
        HBox manageItemsMenu = new HBox(10);
        itemList.setAll(fetchItemsFromDatabase());

        for (Item item : itemList) {
            HBox menuItemHBox = new HBox();
            HBox menuItemBox = menuItems(item.getItemName(), item.getItemPrice());
            menuItemHBox.getChildren().add(menuItemBox);
            manageItemsMenu.getChildren().add(menuItemHBox);
        }

        ((BorderPane) primaryStage.getScene().getRoot()).setCenter(manageItemsMenu);
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

    public static HBox menuItems(String itemName, double itemPrice) {
        HBox menuItemBox = new HBox(10);
        menuItemBox.setMaxWidth(200);

        menuItemBox.setStyle("-fx-border-color: red");
        menuItemBox.setPadding(new Insets(20,10,10,20));
        menuItemBox.setBackground(Background.fill(Color.WHITE));
        menuItemBox.setPadding(new Insets(10));
        Label itemNameLabel = new Label(itemName);
        Label itemPriceLabel = new Label("Price: PKR" + itemPrice);
        Spinner<Integer> quantitySpinner = new Spinner<>(0,Integer.MAX_VALUE,0);
        Button addToCartButton = new Button("Add to Cart");
        menuItemBox.getChildren().addAll(itemNameLabel, itemPriceLabel, quantitySpinner, addToCartButton);
        return menuItemBox;
    }
}
