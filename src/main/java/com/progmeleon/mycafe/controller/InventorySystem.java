package com.progmeleon.mycafe.controller;

import com.progmeleon.mycafe.config.ConfigureExistingData;
import com.progmeleon.mycafe.config.DBConnector;
import com.progmeleon.mycafe.model.*;
import com.progmeleon.mycafe.ui.Components;
import com.progmeleon.mycafe.ui.SideBar;


import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static com.progmeleon.mycafe.controller.FileHandler.saveDataToFile;
import static com.progmeleon.mycafe.controller.ItemController.displayAllItems;
//import static com.progmeleon.mycafe.controller.ItemController.displayItemsByCategory;
import static com.progmeleon.mycafe.config.ConfigureExistingData.*;
import static com.progmeleon.mycafe.controller.ItemController.displayItemsByCategory;


public class InventorySystem {

    public static final String CATEGORIES_FILE_PATH = "categories.json";
    public static final String ITEMS_FILE_PATH = "items.json";
    public static final String USERS_FILE_PATH = "users.json";

//    private List<Category> categories = ConfigureExistingData.categories;
//    private List<Item> items = ConfigureExistingData.items;
//    private List<User> users = ConfigureExistingData.users;
    private static User currentUser;
    private static UserRole currentUserRole = UserRole.ADMIN;
    private int flag = 1;

    private CategoryController categoryController;
    private ItemController itemController;
    private UserController userController;

    public InventorySystem() {
//        categories = new ArrayList<>(ConfigureExistingData.categories);
//        items = new ArrayList<>(ConfigureExistingData.items);
//        users = new ArrayList<>(ConfigureExistingData.users);
        currentUser = null;
        currentUserRole = null;

        // Move the initialization of controllers after loading existing data

        categoryController = new CategoryController(categories);
        itemController = new ItemController(items, categories);
        userController = new UserController(users);
    }


    public void start() {
//        authenticateUser();
        displayMenu();
    }



    public void saveData() {
        FileHandler.saveDataToFile( categories,CATEGORIES_FILE_PATH);
        FileHandler.saveDataToFile( items,ITEMS_FILE_PATH);
        FileHandler.saveDataToFile( users,USERS_FILE_PATH);

        System.out.println("Data saved successfully.");
    }

    // Display the main menu based on user role
    private void displayMenu() {
        if (currentUserRole != null) {
            switch (currentUserRole) {
                case ADMIN:
                    displayAdminMenu();
                    break;
                case SALESMAN:
                    displaySalesmanMenu();
                    break;
                default:
                    System.out.println("Invalid user role. Exiting the program.");
                    System.exit(0);
            }
        } else {
            System.out.println("User role is null. Exiting the program.");
            System.exit(0);
        }
    }

    // The rest of your code remains unchanged...





    private void saveUserData() {
        try (ObjectOutputStream userOutputStream = new ObjectOutputStream(new FileOutputStream("users.ser"))) {
            userOutputStream.writeObject(users);
            System.out.println("User data saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void authenticateUser(String username, String password) {
        Scanner scanner = new Scanner(System.in);

        boolean isAuthenticated = false;

        do {
//            System.out.print("Enter your username: ");
//            String username = scanner.nextLine();
//            System.out.print("Enter your password: ");
//            String password = scanner.nextLine();

            String selectQuery = "SELECT users.*, role.roleName " +
                    "FROM users " +
                    "JOIN role ON users.roleId = role.id " +
                    "WHERE username = ? AND password = ?";

            try {
                Connection connection = DBConnector.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);  // Note: Replace this with hashed password in real-world scenarios

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    currentUser = new User(
                            resultSet.getString("name"),
                            resultSet.getString("username"),
                            resultSet.getString("password"),  // Note: Replace this with hashed password in real-world scenarios
                            UserRole.valueOf(resultSet.getString("roleName").toUpperCase())
                    );

                    currentUserRole = currentUser.getRole();
                    System.out.println("Login successful. Welcome, " + currentUser.getName() + "!");
                    SideBar.showNextScene();
                    isAuthenticated = true;

                } else {
                    System.out.println("Invalid credentials. Please try again.");
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }



        } while (!isAuthenticated);
    }


    // Check if the provided user credentials are valid
    private boolean isValidUser(User user) {
        return users.stream().anyMatch(u -> u.authenticate(user.getUsername(), user.getPassword(), null));
    }

    // Get the role of the current user
    private UserRole getCurrentUserRole() {
        return currentUserRole;
    }

    // Get the role of a user by username
    private UserRole getUserRoleByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user.getRole();
            }
        }
        return null;
    }

    private void displayAdminMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n====== Admin Menu ======");
            System.out.println("1. Manage Categories");
            System.out.println("2. Manage Items");
            System.out.println("3. Manage Users");
            System.out.println("4. Manage Deals");
            System.out.println("5. Change Username");
            System.out.println("6. Change Password");
            System.out.println("7. Logout");
            System.out.print("Enter your choice: ");
            try {
                choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        categoryController.manageCategories();
                        break;
                    case 2:
                        itemController.manageItems();
                        break;
                    case 3:
                        userController.manageUsers();
                        break;
                    case 4:
                        DealHandler.manageDeals();
                        break;
                    case 5:
                        changeUsername();
                        break;
                    case 6:
                        changePassword();
                        break;
                    case 7:
                        System.out.println("Logout successful. Exiting the program.");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Enter valid input");
                displayAdminMenu();
            }

        } while (true);
    }

    private void displaySalesmanMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n====== Salesman Menu ======");
            System.out.println("1. Display Categories");
            System.out.println("2. Display Items");
            System.out.println("3. Display Deals");
            System.out.println("4. Change Username");
            System.out.println("5. Change Password");
            System.out.println("6. Logout");
            System.out.print("Enter your choice: ");
            try {
                choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        CategoryController.displayCategories();
                        break;
                    case 2:
                        displayItems();
                        break;
                    case 3:
                        DealHandler.displayDeals();
                        break;
                    case 4:
                        changeUsername();
                        break;
                    case 5:
                        changePassword();
                        break;
                    case 6:
                        System.out.println("Logout successful. Exiting the program.");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Enter valid input");
                displaySalesmanMenu();
            }
        } while (true);
    }



    public void changeUsername() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your current password: ");
        String currentPassword = scanner.nextLine();

        if (currentUser.authenticate(currentUser.getUsername(), currentPassword, currentUserRole)) {
            System.out.print("Enter your new username: ");
            String newUsername = scanner.nextLine();

            // Check if the new username is unique
            if (userController.isUsernameUnique(newUsername)) {
                currentUser.setUsername(newUsername);
                saveUserData();
                System.out.println("Username changed successfully.");
            } else {
                System.out.println("Username already exists. Please choose a different one.");
            }
        } else {
            System.out.println("Invalid password. Username not changed.");
        }
    }

    public void changePassword() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your current password: ");
        String currentPassword = scanner.nextLine();

        if (currentUser.authenticate(currentUser.getUsername(), currentPassword, currentUserRole)) {
            System.out.print("Enter your new password: ");
            String newPassword = scanner.nextLine();
            currentUser.setPassword(newPassword);

            saveUserData();
            System.out.println("Password changed successfully.");
        } else {
            System.out.println("Invalid password. Password not changed.");
        }
    }



    public static void displayItems() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n====== Items ======");
        System.out.println("1. Display All Items");
        System.out.println("2. Display Items by Category");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        switch (choice) {
            case 1:
                displayAllItems();
                break;
            case 2:
                System.out.print("Enter category name: ");
                String categoryName = scanner.nextLine();
                displayItemsByCategory(categoryName);
                break;
            default:
                System.out.println("Invalid choice. Displaying all items.");
                displayAllItems();
        }
    }

}
