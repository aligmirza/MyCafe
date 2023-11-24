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
    private UserRole currentUserRole = UserRole.ADMIN;
     int flag = 1;

    public InventorySystem() {
        categories = new ArrayList<>();
        items = new ArrayList<>();
        users = new ArrayList<>();
        currentUser = null;
        currentUserRole = null;
        loadExistingData();
    }

    public void start() {
        authenticateUser();
        displayMenu();
    }

    // Load existing data from files
    private void loadExistingData() {
        try {
            System.out.println("Start loading data");

            // Load categories
            try (ObjectInputStream categoryInputStream = new ObjectInputStream(new FileInputStream("categories.ser"))) {
//                System.out.println("Loading categories...");
                categories = (List<Category>) categoryInputStream.readObject();
                System.out.println("Categories loaded successfully.");
            } catch (EOFException e) {
                System.out.println("No existing category data found.");
                createDefaultAdminUser();
            } catch (FileNotFoundException e) {
                System.out.println("Categories file not found. Creating default data.");
                createDefaultAdminUser();
                saveData(); // Save the default data
            }

            // Load items
            try (ObjectInputStream itemInputStream = new ObjectInputStream(new FileInputStream("items.ser"))) {
//                System.out.println("Loading items...");
                items = (List<Item>) itemInputStream.readObject();
                System.out.println("Items loaded successfully.");
            } catch (EOFException e) {
                System.out.println("No existing item data found.");
                createDefaultAdminUser();
            } catch (FileNotFoundException e) {
                System.out.println("Items file not found. Creating default data.");
                createDefaultAdminUser();
                saveData(); // Save the default data
            }

            // Load users
            try (ObjectInputStream userInputStream = new ObjectInputStream(new FileInputStream("users.ser"))) {
//                System.out.println("Loading user data...");
                users = (List<User>) userInputStream.readObject();
                System.out.println("User data loaded successfully.");
            } catch (EOFException e) {
                System.out.println("No existing user data found.");
                createDefaultAdminUser();
            } catch (FileNotFoundException e) {
                System.out.println("Users file not found. Creating default data.");
                createDefaultAdminUser();
                saveData(); // Save the default data
            }

            System.out.println("Data loaded successfully.");

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    private void createDefaultAdminUser() {
        User adminUser = new User("admin", "admin", "admin");
        adminUser.setRole(UserRole.ADMIN);
        users = new ArrayList<>();  // Initialize the users list
        users.add(adminUser);
        saveUserData();
    }

//    private void saveUserData() {
//        try (ObjectOutputStream userOutputStream = new ObjectOutputStream(new FileOutputStream("users.ser"))) {
//            userOutputStream.writeObject(users);
//            System.out.println("User data saved successfully.");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }



    public boolean authenticateUser() {
        Scanner scanner = new Scanner(System.in);

        // Move this outside the loop to load data only once
//        loadExistingData();

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

        return true;
    }





    // Check if the provided user credentials are valid
    private boolean isValidUser(User user) {
        return users.stream().anyMatch(u -> u.authenticate(user.getUsername(), user.getPassword(), null));
    }


    // Get the role of the current user
    public UserRole getCurrentUserRole() {
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


    // Display the admin menu
    // Display the admin menu
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
                    manageCategories();
                    break;
                case 2:
                    manageItems();
                    break;
                case 3:
                    manageUsers();
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

    // Manage users
    private void manageUsers() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n====== User Management ======");
            System.out.println("1. Add User");
            System.out.println("2. Update User");
            System.out.println("3. Delete User");
            System.out.println("4. Display Users");
            System.out.println("5. Back to Admin Menu");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addUser();
                    break;
                case 2:
                    updateUser();
                    break;
                case 3:
                    deleteUser();
                    break;
                case 4:
                    displayUsers();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (true);
    }

    // Display users



    // Display the salesman menu
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
                    displayCategories();
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

    private void manageCategories() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n====== Category Management ======");
            System.out.println("1. Add Category");
            System.out.println("2. Delete Category");
            System.out.println("3. Update Category");
            System.out.println("4. Display Categories");
            System.out.println("5. Back to Admin Menu");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addCategory();
                    break;
                case 2:
                    deleteCategory();
                    break;
                case 3:
                    updateCategory();
                    break;
                case 4:
                    displayCategories();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (true);
    }

    // Manage items (Admin)
    private void manageItems() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n====== Item Management ======");
            System.out.println("1. Add Item");
            System.out.println("2. Delete Item");
            System.out.println("3. Update Item");
            System.out.println("4. Display Items");
            System.out.println("5. Back to Admin Menu");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addItem();
                    break;
                case 2:
                    deleteItem();
                    break;
                case 3:
                    updateItem();
                    break;
                case 4:
                    displayItems();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (true);
    }

    // Display categories (Salesman)
    private void displayCategories() {
        System.out.println("\n====== Categories ======");
        for (Category category : categories) {
            System.out.println(category.getCategoryName());
        }
    }



    // Add category
    private void addCategory() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter category name: ");
        String categoryName = scanner.nextLine();

        Category newCategory = new Category(categoryName);
        categories.add(newCategory);

        saveData();
        System.out.println("Category added successfully.");
    }

    // Delete category
    private void deleteCategory() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter category name to delete: ");
        String categoryName = scanner.nextLine();

        categories.removeIf(category -> category.getCategoryName().equalsIgnoreCase(categoryName));

        saveData();
        System.out.println("Category deleted successfully.");
    }

    // Update category
    private void updateCategory() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter category name to update: ");
        String oldCategoryName = scanner.nextLine();
        System.out.print("Enter new category name: ");
        String newCategoryName = scanner.nextLine();

        for (Category category : categories) {
            if (category.getCategoryName().equalsIgnoreCase(oldCategoryName)) {
                category.setCategoryName(newCategoryName);
                saveData();
                System.out.println("Category updated successfully.");
                return;
            }
        }

        System.out.println("Category not found.");
    }

    // Add item
    private void addItem() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter item name: ");
        String itemName = scanner.nextLine();
        System.out.print("Enter item price: ");
        double itemPrice = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline character
        System.out.print("Enter item category: ");
        String itemCategoryName = scanner.nextLine();

        int categoryId = getCategoryIDByName(itemCategoryName);

        if (categoryId != -1) {
            Item newItem = new Item(itemName, itemPrice, categoryId);
            items.add(newItem);
            saveData();
            System.out.println("Item added successfully.");
        } else {
            System.out.println("Category not found. Item not added.");
        }
    }

    // Delete item
    private void deleteItem() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter item name to delete: ");
        String itemName = scanner.nextLine();

        items.removeIf(item -> item.getItemName().equalsIgnoreCase(itemName));

        saveData();
        System.out.println("Item deleted successfully.");
    }

    // Update item
    private void updateItem() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter item name to update: ");
        String oldItemName = scanner.nextLine();
        System.out.print("Enter new item name: ");
        String newItemName = scanner.nextLine();
        System.out.print("Enter new item price: ");
        double newItemPrice = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline character
        System.out.print("Enter new item category: ");
        String newItemCategoryName = scanner.nextLine();

        int newCategoryId = getCategoryIDByName(newItemCategoryName);

        for (Item item : items) {
            if (item.getItemName().equalsIgnoreCase(oldItemName)) {
                item.setItemName(newItemName);
                item.setItemPrice(newItemPrice);
                item.setCategoryId(newCategoryId);
                saveData();
                System.out.println("Item updated successfully.");
                return;
            }
        }

        System.out.println("Item not found.");
    }

    // Save data to files
    private void saveData() {
        try (ObjectOutputStream categoryOutputStream = new ObjectOutputStream(new FileOutputStream("categories.ser"));
             ObjectOutputStream itemOutputStream = new ObjectOutputStream(new FileOutputStream("items.ser"))) {

            categoryOutputStream.writeObject(categories);
            itemOutputStream.writeObject(items);

            System.out.println("Data saved successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
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

    // Change password
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
    // Display items by category
    private static void displayItemsByCategory(String categoryName) {
        int categoryId = getCategoryIDByName(categoryName);

        if (categoryId != -1) {
            boolean found = false;

            for (Item item : items) {
                if (item.getCategoryId() == categoryId) {
                    System.out.println(item);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No items found in category: " + categoryName);
            }
        } else {
            System.out.println("Category not found: " + categoryName);
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


    // Add user
    private void addUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        User newUser = new User(username, password, name);
        users.add(newUser);

        saveUserData();
        System.out.println("User added successfully.");
    }

    // Update user
    private void updateUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username to update: ");
        String oldUsername = scanner.nextLine();

        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(oldUsername)) {
                System.out.print("Enter new username: ");
                String newUsername = scanner.nextLine();
                System.out.print("Enter new password: ");
                String newPassword = scanner.nextLine();
                System.out.print("Enter new name: ");
                String newName = scanner.nextLine();
                System.out.println("Pres 1 for Admin or 2 for SALESMAN");
                String role;
                do {
                    System.out.print("Enter new role (1 for ADMIN, 2 for SALESMAN): ");
                    role = scanner.nextLine();

                    if (role.equals("1")) {
                        user.setRole(UserRole.ADMIN);
                    } else if (role.equals("2")) {
                        user.setRole(UserRole.SALESMAN);
                    } else {
                        System.out.println("Invalid role. Please enter 1 for ADMIN or 2 for SALESMAN.");
                    }
                } while (!role.equals("1") && !role.equals("2"));


                user.setUsername(newUsername);
                user.setPassword(newPassword);
                user.setName(newName);

                saveUserData();
                System.out.println("User updated successfully.");
                return;
            }
        }

        System.out.println("User not found.");
    }

    // Delete user
    private void deleteUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username to delete: ");
        String username = scanner.nextLine();

        users.removeIf(user -> user.getUsername().equalsIgnoreCase(username));

        saveUserData();
        System.out.println("User deleted successfully.");
    }

    // Create default admin user
//    private void createDefaultAdminUser() {
//        if (users.isEmpty()) {
//            Scanner scanner = new Scanner(System.in);
//            System.out.print("Enter admin username: ");
//            String adminUsername = scanner.nextLine();
//            System.out.print("Enter admin password: ");
//            String adminPassword = scanner.nextLine();
//
//            User adminUser = new User(adminUsername, adminPassword, "admin");
//            adminUser.setRole(UserRole.ADMIN);
//            users.add(adminUser);
//
//            saveUserData();
//            System.out.println("Default admin user created successfully.");
//        }
//    }

    // Save user data to files
    private void saveUserData() {
        try (ObjectOutputStream userOutputStream = new ObjectOutputStream(new FileOutputStream("users.ser"))) {
            userOutputStream.writeObject(users);
            System.out.println("User data saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayUsers() {
        System.out.println("\n====== Users ======");
        for (User user : users) {
            System.out.println("Username: " + user.getUsername() + ", Name: " + user.getName() + ", Role: " + user.getRole());
        }
    }

}
