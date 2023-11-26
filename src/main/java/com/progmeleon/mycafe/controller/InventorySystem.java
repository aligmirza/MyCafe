package com.progmeleon.mycafe.controller;

import com.progmeleon.mycafe.model.Category;
import com.progmeleon.mycafe.model.Item;
import com.progmeleon.mycafe.model.User;
import com.progmeleon.mycafe.model.UserRole;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.progmeleon.mycafe.controller.ItemController.displayAllItems;
import static com.progmeleon.mycafe.controller.ItemController.displayItemsByCategory;

public class InventorySystem {
    private List<Category> categories;
    private List<Item> items;
    private List<User> users;
    private User currentUser;
    private UserRole currentUserRole = UserRole.ADMIN;
    private int flag = 1;

    private CategoryController categoryController;
    private ItemController itemController;
    private UserController userController;

    public InventorySystem() {
        categories = new ArrayList<>();
        items = new ArrayList<>();
        users = new ArrayList<>();
        currentUser = null;
        currentUserRole = null;
        loadExistingData();

        categoryController = new CategoryController(categories);
        itemController = new ItemController(items, categories);
        userController = new UserController(users);
    }

    public void start() {
        authenticateUser();
        displayMenu();
    }

    // Load existing data from files
// Load existing data from files
    private void loadExistingData() {
        try {
            loadCategories();
            loadItems();
            loadUsers();

            System.out.println("Data loaded successfully.");

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadCategories() throws IOException, ClassNotFoundException {
        try (ObjectInputStream categoryInputStream = new ObjectInputStream(new FileInputStream("categories.ser"))) {
            categories = (List<Category>) categoryInputStream.readObject();
            System.out.println("Categories loaded successfully.");
        } catch (EOFException | FileNotFoundException e) {
            System.out.println("No existing category data found.");
            createDefaultAdminUser();
        }
    }

    private void loadItems() throws IOException, ClassNotFoundException {
        try (ObjectInputStream itemInputStream = new ObjectInputStream(new FileInputStream("items.ser"))) {
            items = (List<Item>) itemInputStream.readObject();
            System.out.println("Items loaded successfully.");
        } catch (EOFException | FileNotFoundException e) {
            System.out.println("No existing item data found.");
            createDefaultAdminUser();
        }
    }

    private void loadUsers() throws IOException, ClassNotFoundException {
        try (ObjectInputStream userInputStream = new ObjectInputStream(new FileInputStream("users.ser"))) {
            users = (List<User>) userInputStream.readObject();
            System.out.println("User data loaded successfully.");
        } catch (EOFException | FileNotFoundException e) {
            System.out.println("No existing user data found.");
            createDefaultAdminUser();
        }
    }

    private void createDefaultAdminUser() {
        User adminUser = new User("admin", "admin", "admin");
        adminUser.setRole(UserRole.ADMIN);
        users = new ArrayList<>();  // Initialize the users list
        users.add(adminUser);
        saveUserData();
    }

    private void saveUserData() {
        try (ObjectOutputStream userOutputStream = new ObjectOutputStream(new FileOutputStream("users.ser"))) {
            userOutputStream.writeObject(users);
            System.out.println("User data saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void authenticateUser() {
        Scanner scanner = new Scanner(System.in);

        boolean isAuthenticated = false;

        do {
            System.out.print("Enter your username: ");
            String username = scanner.nextLine();
            System.out.print("Enter your password: ");
            String password = scanner.nextLine();

            for (User user : users) {
                if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                    currentUser = user;
                    currentUserRole = user.getRole();
                    System.out.println("Login successful. Welcome, " + currentUser.getName() + "!");
                    isAuthenticated = true;
                    break;
                }
            }

            if (!isAuthenticated) {
                System.out.println("Invalid credentials. Please try again.");
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

    private void displayAdminMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n====== Admin Menu ======");
            System.out.println("1. Manage Categories");
            System.out.println("2. Manage Items");
            System.out.println("3. Manage Users");
            System.out.println("4. Change Username");
            System.out.println("5. Change Password");
            System.out.println("6. Logout");
            System.out.print("Enter your choice: ");
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
        } while (true);
    }

    private void displaySalesmanMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n====== Salesman Menu ======");
            System.out.println("1. Display Categories");
            System.out.println("2. Display Items");
            System.out.println("3. Change Username");
            System.out.println("4. Change Password");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    CategoryController.displayCategories();
                    break;
                case 2:
                    displayItems();
                    break;
                case 3:
                    changeUsername();
                    break;
                case 4:
                    changePassword();
                    break;
                case 5:
                    System.out.println("Logout successful. Exiting the program.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (true);
    }


    private void changeUsername() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your current password: ");
        String currentPassword = scanner.nextLine();

        if (currentUser.authenticate(currentUser.getUsername(), currentPassword, currentUserRole)) {
            System.out.print("Enter your new username: ");
            String newUsername = scanner.nextLine();
            currentUser.setUsername(newUsername);

            saveUserData();
            System.out.println("Username changed successfully.");
        } else {
            System.out.println("Invalid password. Username not changed.");
        }
    }


    private void changePassword() {
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
