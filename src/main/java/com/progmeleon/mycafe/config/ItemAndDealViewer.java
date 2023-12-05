package com.progmeleon.mycafe.config;


import com.progmeleon.mycafe.config.DBConnector;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemAndDealViewer extends Application {

    private ComboBox<String> itemComboBox;
    private ComboBox<String> dealComboBox;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Item and Deal Viewer");

        itemComboBox = new ComboBox<>();
        dealComboBox = new ComboBox<>();

        loadItems();
        loadDeals();

        VBox vBox = new VBox(itemComboBox, dealComboBox);
        Scene scene = new Scene(vBox, 300, 200);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadItems() {
        try (Connection connection = DBConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT itemName FROM item");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            ObservableList<String> items = FXCollections.observableArrayList();

            while (resultSet.next()) {
                items.add(resultSet.getString("itemName"));
            }

            itemComboBox.setItems(items);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadDeals() {
        try (Connection connection = DBConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT dealName FROM deal");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            ObservableList<String> deals = FXCollections.observableArrayList();

            while (resultSet.next()) {
                deals.add(resultSet.getString("dealName"));
            }

            dealComboBox.setItems(deals);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
