package com.progmeleon.mycafe.config;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertDataExample extends Application {

    // JDBC URL, username, and password of MySQL server
    private static final String JDBC_URL = "jdbc:mysql://localhost:3308/mycafe";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Insert Data Example");

        // UI components
        Label categoryNameLabel = new Label("Category Name:");
        TextField categoryNameField = new TextField();

        Button insertButton = new Button("Insert Data");
        insertButton.setOnAction(e -> {
            // Insert data into the database
            String categoryName = categoryNameField.getText();
            insertData(categoryName);
        });

        // Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(categoryNameLabel, categoryNameField, insertButton);

        // Scene
        Scene scene = new Scene(layout, 300, 150);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    // Insert data into the database
    private void insertData(String categoryName) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String query = "INSERT INTO category (categoryName) VALUES (?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, categoryName);
                preparedStatement.executeUpdate();
                System.out.println("Data inserted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

