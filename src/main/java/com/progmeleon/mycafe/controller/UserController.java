package com.progmeleon.mycafe.controller;

import com.progmeleon.mycafe.config.ConfigureExistingData;
import com.progmeleon.mycafe.config.DBConnector;
import com.progmeleon.mycafe.model.User;
import com.progmeleon.mycafe.model.UserRole;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import static com.progmeleon.mycafe.config.ConfigureExistingData.users;
import static com.progmeleon.mycafe.controller.FileHandler.saveDataToFile;

public class UserController {
    private static List<User> users;
//
//    public UserController(List<User> users) {
//        this.users = users;
//    }
    static {
//        users = FileHandler.loadData(User.class,InventorySystem.USERS_FILE_PATH);
    }

    public UserController(List<User> users) {
        this.users = ConfigureExistingData.users;
    }



    public void manageUsers() {
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

    // Add user
    private void addUser() {
        Scanner scanner = new Scanner(System.in);

        // Get a unique username without spaces
        String username;
        boolean usernameValid;
        do {
            System.out.print("Enter username (without spaces): ");
            username = scanner.nextLine();
            usernameValid = isUsernameValid(username);
            if (!usernameValid) {
                System.out.println("Invalid username. Please enter a unique username without spaces.");
            }
        } while (!usernameValid);

        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        UserRole userRole = null;
        do {
            System.out.print("Enter new role (1 for ADMIN, 2 for SALESMAN): ");
            String role = scanner.nextLine();

            if (role.equals("1")) {
                userRole = UserRole.ADMIN;
            } else if (role.equals("2")) {
                userRole = UserRole.SALESMAN;
            } else {
                System.out.println("Invalid role. Please enter 1 for ADMIN or 2 for SALESMAN.");
            }
        } while (userRole == null);

        String insertQuery = "INSERT INTO user (name, username, password, role) VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, userRole.name());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("User added successfully.");
            } else {
                System.out.println("Failed to add user.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Object idGenerator() {
        return users.size() + 1;
    }

    // Update user
    public void updateUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username to update: ");
        String oldUsername = scanner.nextLine();

        String updateQuery = "UPDATE user SET name = ?, username = ?, password = ?, role = ? WHERE username = ?";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            for (User user : users) {
                if (user.getUsername().equalsIgnoreCase(oldUsername)) {
                    System.out.print("Enter new name: ");
                    String newName = scanner.nextLine();
                    System.out.print("Enter new username: ");
                    String newUsername = scanner.nextLine();
                    System.out.print("Enter new password: ");
                    String newPassword = scanner.nextLine();
                    System.out.println("Press 1 for Admin or 2 for SALESMAN");
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

                    preparedStatement.setString(1, newName);
                    preparedStatement.setString(2, newUsername);
                    preparedStatement.setString(3, newPassword);
                    preparedStatement.setString(4, user.getRole().name());
                    preparedStatement.setString(5, oldUsername);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("User updated successfully.");
                    } else {
                        System.out.println("Failed to update user.");
                    }

                    return;
                }
            }

            System.out.println("User not found.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Delete user
    private void deleteUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username to delete: ");
        String username = scanner.nextLine();

        // Use SQL query to delete the user
        String deleteQuery = "DELETE FROM user WHERE username = ?";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {

            preparedStatement.setString(1, username);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("User deleted successfully.");
            } else {
                System.out.println("User not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Display users
    private void displayUsers() {
        System.out.println("\n====== Users ======");
        for (User user : users) {
            System.out.println(", Name: " + user.getName() +", "+ "Username: " + user.getUsername() +", "+ "Password: " + user.getPassword() +" " + ", Role: " + user.getRole());
        }
    }

    // Check if the username is unique
    public boolean isUsernameUnique(String username) {
        for (User existingUser : users) {
            if (existingUser.getUsername().equals(username)) {
                return false; // Username is not unique
            }
        }
        return true; // Username is unique
    }

    // Check if the username is valid
    private boolean isUsernameValid(String username) {
        return !username.contains(" ");
    }

    public static void createDefaultAdminUser() {
        User adminUser = new User("admin", "admin", "admin", UserRole.ADMIN );
        adminUser.setRole(UserRole.ADMIN);
        users = new ArrayList<>();  // Initialize the users list
        users.add(adminUser);
        saveDataToFile(users, InventorySystem.USERS_FILE_PATH);
    }
}
