package com.progmeleon.mycafe.controller;

import com.progmeleon.mycafe.model.User;
import com.progmeleon.mycafe.model.UserRole;

import java.util.List;
import java.util.Scanner;

public class UserController {
    private List<User> users;

    public UserController(List<User> users) {
        this.users = users;
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

        User newUser = new User(username, password, name);

        // Check if the username is unique
        if (!isUsernameUnique(username)) {
            System.out.println("Username is not unique. User not added.");
            return;
        }

        newUser.setRole(userRole);
        users.add(newUser);

        FileHandler.saveDataToFile(users, "users.ser");
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

                user.setUsername(newUsername);
                user.setPassword(newPassword);
                user.setName(newName);

                FileHandler.saveDataToFile(users, "users.ser");
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

        FileHandler.saveDataToFile(users, "users.ser");
        System.out.println("User deleted successfully.");
    }

    // Display users
    private void displayUsers() {
        System.out.println("\n====== Users ======");
        for (User user : users) {
            System.out.println(", Name: " + user.getName() +", "+ "Username: " + user.getUsername() +", "+ "Password: " + user.getPassword() +" " + ", Role: " + user.getRole());
        }
    }

    // Check if the username is unique
    private boolean isUsernameUnique(String username) {
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
}
