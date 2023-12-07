package com.progmeleon.mycafe.ui;

import com.progmeleon.mycafe.controller.CategoryController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import static com.progmeleon.mycafe.ui.Components.primaryStage;
import static com.progmeleon.mycafe.ui.Menu.showMenuItems;
import static com.progmeleon.mycafe.ui.UserRoleInterface.showManageItems;

public class SideBar {

    public static void showNextScene() {

        BorderPane bpane = new BorderPane();
        bpane.setStyle("-fx-background-color: grey;");

//        AnchorPane anchorPane = new AnchorPane();
//        anchorPane.setStyle("-fx-background-color: black;");

        Label label = new Label("Welcome to the main page");
        label.setTextFill(Color.WHITE);
        label.setAlignment(Pos.CENTER);
        label.setFont(Font.font("Times New Roman", 15));


        VBox adminMenu = new VBox(10);
        adminMenu.setAlignment(Pos.CENTER);
        adminMenu.setPadding(new Insets(20));
        adminMenu.setMinWidth(200);
        adminMenu.setStyle("-fx-background-color: black;");

        double buttonWidth = 180;
        double buttonHeight = 30;

        Button showItemButton = new Button("1. Inventory");
        showItemButton.setMinSize(buttonWidth,buttonHeight);
        showItemButton.setStyle("-fx-cursor: hand;");

        Button manageCategoriesButton = new Button("2. Manage Categories");
        manageCategoriesButton.setMinSize(buttonWidth,buttonHeight);
        manageCategoriesButton.setStyle("-fx-cursor: hand;");

        Button manageItemsButton = new Button("3. Manage Items");
        manageItemsButton.setMinSize(buttonWidth,buttonHeight);
        manageItemsButton.setStyle("-fx-cursor: hand;");

        Button manageUsersButton = new Button("4. Manage Users");
        manageUsersButton.setMinSize(buttonWidth,buttonHeight);
        manageUsersButton.setStyle("-fx-cursor: hand;");

        Button changeUsernameButton = new Button("5. Change Username");
        changeUsernameButton.setMinSize(buttonWidth,buttonHeight);
        changeUsernameButton.setStyle("-fx-cursor: hand;");

        Button changePasswordButton = new Button("6. Change Password");
        changePasswordButton.setMinSize(buttonWidth,buttonHeight);
        changePasswordButton.setStyle("-fx-cursor: hand;");

        Button logoutButton = new Button("7. Logout");
        logoutButton.setMinSize(buttonWidth,buttonHeight);
        logoutButton.setStyle("-fx-cursor: hand;");

        adminMenu.getChildren().addAll(
                showItemButton,
                manageCategoriesButton,
                manageItemsButton,
                manageUsersButton,
                changeUsernameButton,
                changePasswordButton,
                logoutButton
        );

        manageCategoriesButton.setOnAction(e -> CategoryController.displayCategories());
        manageItemsButton.setOnAction(e -> showManageItems());
        showItemButton.setOnAction(e -> Menu.showMenuItems());


        bpane.setLeft(adminMenu);

        Scene nextScene = new Scene(bpane, 900, 600);
        primaryStage.setScene(nextScene);
    }
}
