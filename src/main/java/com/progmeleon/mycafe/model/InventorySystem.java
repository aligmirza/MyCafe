package com.progmeleon.mycafe.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InventorySystem {
    private static List<Category> categories;
    private static List<Item> items;
    private List<User> users;
    private User currentUser;

    public InventorySystem() {
        this.categories = new ArrayList<>();
        this.items = new ArrayList<>();
        this.users = new ArrayList<>();
        // Load existing data from files
        loadExistingData();
        // Authenticate user before entering the system
        authenticateUser();
    }

    // Load existing data from files
// Load existing data from files
// Load existing data from files
// Load existing data from files
    private void loadExistingData() {
        try (ObjectInputStream categoryInputStream = new ObjectInputStream(new FileInputStream("categories.ser"));
             ObjectInputStream itemInputStream = new ObjectInputStream(new FileInputStream("items.ser"))) {

            categories = (List<Category>) categoryInputStream.readObject();
            items = (List<Item>) itemInputStream.readObject();

            File usersFile = new File("users.ser");

            if (usersFile.exists()) {
                try (ObjectInputStream userInputStream = new ObjectInputStream(new FileInputStream(usersFile))) {
                    users = (List<User>) userInputStream.readObject();
                }
            } else {
                // Create an initial admin user with the password "admin"
                User adminUser = new User("admin", "admin");
                users.add(adminUser);

                // Save the initial user to users.ser
                try (ObjectOutputStream userOutputStream = new ObjectOutputStream(new FileOutputStream("users.ser"))) {
                    userOutputStream.writeObject(users);
                }
            }

            System.out.println("Data loaded successfully.");

        } catch (FileNotFoundException e) {
            System.out.println("No existing data found. Starting with an empty inventory.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }




    // User authentication method
    private void authenticateUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        User user = new User(username, password);
        if (isValidUser(user)) {
            currentUser = user;
            System.out.println("Login successful. Welcome, " + currentUser.getUsername() + "!");
        } else {
            System.out.println("Invalid credentials. Exiting the program.");
            System.exit(0);
        }
    }

    // Check if the provided user credentials are valid
    private boolean isValidUser(User user) {
        return users.stream().anyMatch(u -> u.authenticate(user.getUsername(), user.getPassword()));
    }


    // Check if the provided username is unique
    private boolean isUniqueUsername(String username) {
        return users.stream().noneMatch(u -> u.hasSameUsername(username));
    }

    // Change username for the current user
// Change username for the current user
    public void changeUsername(String newUsername) {
        if (currentUser != null) {
            // Check if the new username is unique
            if (isUniqueUsername(newUsername)) {
                currentUser.changeUsername(newUsername);
                System.out.println("Username changed successfully. New username: " + currentUser.getUsername());
                saveUserData(); // Save modified user data to users.ser
            } else {
                System.out.println("Username already exists. Please choose a different username.");
            }
        } else {
            System.out.println("User not authenticated. Please log in first.");
        }
    }

    // Change password for the current user
    public void changePassword(String newPassword) {
        if (currentUser != null) {
            currentUser.changePassword(newPassword);
            System.out.println("Password changed successfully.");
            saveUserData(); // Save modified user data to users.ser
        } else {
            System.out.println("User not authenticated. Please log in first.");
        }
    }

    // Save modified user data to users.ser
    private void saveUserData() {
        try (ObjectOutputStream userOutputStream = new ObjectOutputStream(new FileOutputStream("users.ser"))) {
            userOutputStream.writeObject(users);
            System.out.println("User data saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    // Add category to the system
    public void addCategory(String categoryName) {
        // Check if the category already exists
        if (categories.stream().anyMatch(c -> c instanceof Category && c.getCategoryName().equalsIgnoreCase(categoryName))) {
            System.out.println("Category already exists.");
            return;
        }

        Category category = new Category(categoryName);
        categories.add(category);
        System.out.println("Category added successfully.");
        saveData(); // Save modified data to files
    }

    // Add item to the system (avoiding duplicates)
    public void addItem(String itemName, double itemPrice, int categoryId) {
        // Check if the item already exists
        if (items.stream().anyMatch(i -> i.equalsIgnoreCase(itemName, categoryId))) {
            // Update the existing item
            items.stream()
                    .filter(i -> i.equalsIgnoreCase(itemName, categoryId))
                    .findFirst()
                    .ifPresent(existingItem -> {
                        existingItem.setItemPrice(itemPrice);
                        System.out.println("Item updated successfully.");
                        saveData(); // Save modified data to files
                    });
        } else {
            // If the item doesn't exist, add a new one
            Item newItem = new Item(itemName, itemPrice, categoryId);
            items.add(newItem);
            System.out.println("Item added successfully.");
            saveData(); // Save modified data to files
        }
    }

    // Save data to files
    private void saveData() {
        try (ObjectOutputStream categoryOutputStream = new ObjectOutputStream(new FileOutputStream("categories.ser"));
             ObjectOutputStream itemOutputStream = new ObjectOutputStream(new FileOutputStream("items.ser"));
             ObjectOutputStream userOutputStream = new ObjectOutputStream(new FileOutputStream("users.ser"))) {

            categoryOutputStream.writeObject(categories);
            itemOutputStream.writeObject(items);
            userOutputStream.writeObject(users);

            System.out.println("Data saved successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Display users
    public void displayUsers() {
        System.out.println("\n====== Users ======");
        for (User user : users) {
            System.out.println(user);
        }
    }

    // Getters for categories, items, and users
    public List<Category> getCategories() {
        return categories;
    }

    public List<Item> getItems() {
        return items;
    }

    public List<User> getUsers() {
        return users;
    }

    // Display items with an option to filter by category
    public static void displayItems(List<Item> items) {
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

    // Display all items
    private static void displayAllItems() {
        if (items.isEmpty()) {
            System.out.println("No items available.");
        } else {
            for (Item item : items) {
                System.out.println(item);
            }
        }
    }

    // Display items by category
    private static void displayItemsByCategory(String categoryName) {
        if (items.isEmpty()) {
            System.out.println("No items available.");
        } else {
            for (Item item : items) {
                if (item.getCategoryId() == getCategoryIDByName(categoryName)) {
                    System.out.println(item);
                }
            }
        }
    }

    // Get category ID by name
    private static int getCategoryIDByName(String categoryName) {
        for (Category category : categories) {
            if (category instanceof Category && category.getCategoryName().equalsIgnoreCase(categoryName)) {
                return category.getCategoryId();
            }
        }
        return -1; // Return -1 if the category is not found
    }

}
