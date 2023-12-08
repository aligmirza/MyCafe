package com.progmeleon.mycafe.ui;
import com.progmeleon.mycafe.controller.FileHandler;
import com.progmeleon.mycafe.controller.InventorySystem;
import com.progmeleon.mycafe.config.DBConnector;
import com.progmeleon.mycafe.model.Category;
import com.progmeleon.mycafe.model.Item;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.*;
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
    public static Stage primaryStage;
    private static List<Category> categories = new ArrayList<>();

    private static List<Item> items;


    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        BorderPane borderPane = createBorderPane();
        Scene scene = new Scene(borderPane, 900, 600, Color.WHITE);

        primaryStage.setTitle("My Cafe");
        primaryStage.getIcons().add(new Image("file:E:\\2nd Semester\\Java opp\\Finalproject\\src\\img.png"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private BorderPane createBorderPane() {
        BorderPane borderPane = new BorderPane();

//        Label anchorpaneLabel = new Label("My Cafe");
//        anchorpaneLabel.setAlignment(Pos.CENTER);
       Image image = new Image("file:E:\\2nd Semester\\Java opp\\Finalproject\\src\\img_5.png");
       ImageView imageView = new ImageView(image);

//        anchorpaneLabel.setStyle("-fx-text-fill: grey; -fx-font-weight: Bold; -fx-font-size: 30px; ");
//        anchorpaneLabel.setFont(Font.font("Ariel", 20));

        GridPane layout = loginGridPane();

        borderPane.setCenter(layout);

        AnchorPane anchorPane = new AnchorPane();
//        AnchorPane.setTopAnchor(anchorpaneLabel, 0.0);
//        AnchorPane.setRightAnchor(anchorpaneLabel, 0.0);
//        AnchorPane.setBottomAnchor(anchorpaneLabel, 0.0);
//        AnchorPane.setLeftAnchor(anchorpaneLabel, 0.0);
//        anchorPane.getChildren().add(anchorpaneLabel);
        anchorPane.getChildren().add(imageView);


        anchorPane.setStyle("-fx-background-color: black");
        anchorPane.setPrefWidth(200);
        borderPane.setLeft(anchorPane);

        return borderPane;
    }

    private GridPane loginGridPane() {
        GridPane layout = new GridPane();
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setHgap(10);
        layout.setVgap(10);

        TextField username = new TextField();
        username.setPromptText("Enter Username");
        username.setPrefWidth(200);
        username.setPrefHeight(30);

        PasswordField password = new PasswordField();
        password.setPromptText("Enter password");
        password.setPrefWidth(200);
        password.setPrefHeight(30);

        VBox signUpBox = new VBox();
        signUpBox.setAlignment(Pos.CENTER);
        ImageView signUpImage = new ImageView(new Image("file:E:\\2nd Semester\\Java opp\\Finalproject\\src\\img_3.png"));
        signUpImage.setFitHeight(60);
        signUpImage.setFitWidth(80);
        Label signUpLabel = new Label("", signUpImage);
        signUpBox.getChildren().addAll(signUpLabel);

        ImageView UserImage = new ImageView(new Image("file:E:\\2nd Semester\\Java opp\\Finalproject\\src\\img_1.png"));
        UserImage.setFitHeight(40);
        UserImage.setFitWidth(40);
        Label label = new Label("",UserImage);

        ImageView keyIcon = new ImageView(new Image("file:E:\\2nd Semester\\Java opp\\Finalproject\\src\\img_2.png"));
        keyIcon.setFitHeight(40);
        keyIcon.setFitWidth(40);
        Label passwordlabel = new Label("",keyIcon);

        layout.add(signUpBox,2,0);
        layout.add(label, 0, 1);
        layout.add(passwordlabel, 0, 2);
        layout.add(username, 2, 1);
//        HBox passwordBox = new HBox(5);
//        passwordBox.getChildren().addAll(passwordlabel, password);
//        layout.add(passwordBox, 2, 1);
        layout.add(password, 2, 2);
        layout.setAlignment(Pos.CENTER);

//        Button register = createButton("Forgot Password");
        Button register = new Button("Forget Password ?");
        register.setFont(new Font("Arial",15));
        register.setStyle("-fx-text-fill: black; -fx-border-color: transparent; -fx-cursor: hand; -fx-background-color: transparent; ");
        register.setUnderline(true);

        Button loginbutton = new Button("Login");
        loginbutton.setStyle("-fx-cursor: hand");
        loginbutton.setOnAction(e -> InventorySystem.authenticateUser(username.getText(), password.getText()));

        layout.add(register, 2, 3);
        layout.add(loginbutton, 2, 4);

        return layout;
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Times New Roman", 16));
        label.setStyle("-fx-font-weight: Bold");
        return label;
    }

//    private Button createButton(String text) {
//        Button button = new Button(text);
//        button.setFont(new Font("Arial", 15));
//        button.setStyle("-fx-text-fill: black; -fx-border-color: rgba(116,55,55,0); -fx-cursor: hand; -fx-background-color: transparent");
//        button.setUnderline(true);
//        return button;
//    }


    public static void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Inventory System");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public static void main(String[] args) {
        launch();
    }


}
