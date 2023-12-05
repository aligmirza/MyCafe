package com.progmeleon.mycafe.controller;

import com.progmeleon.mycafe.config.ConfigureExistingData;
import com.progmeleon.mycafe.config.DBConnector;
import com.progmeleon.mycafe.model.Category;
import com.progmeleon.mycafe.model.User;

import java.util.ArrayList;
import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class CategoryController {
    public static List<Category> categories;


    public CategoryController() {
        this.categories = ConfigureExistingData.categories;
    }

    public CategoryController(List<Category> categories) {
        this.categories = ConfigureExistingData.categories;
    }

    public void manageCategories() {
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

    // Add category
    private void addCategory() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter category name: ");
        String categoryName = scanner.nextLine();

        // Use SQL query to add the category
        String insertQuery = "INSERT INTO category (categoryName) VALUES (?)";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            preparedStatement.setString(1, categoryName);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Category added successfully.");
            } else {
                System.out.println("Failed to add category.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete category
    private void deleteCategory() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter category name to delete: ");
        String categoryName = scanner.nextLine();

        // Use SQL query to delete the category
        String deleteQuery = "DELETE FROM category WHERE categoryName = ?";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {

            preparedStatement.setString(1, categoryName);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Category deleted successfully.");
            } else {
                System.out.println("Category not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update category
    private void updateCategory() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter category name to update: ");
        String oldCategoryName = scanner.nextLine();
        System.out.print("Enter new category name: ");
        String newCategoryName = scanner.nextLine();

        // Use SQL query to update the category
        String updateQuery = "UPDATE category SET categoryName = ? WHERE categoryName = ?";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            preparedStatement.setString(1, newCategoryName);
            preparedStatement.setString(2, oldCategoryName);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Category updated successfully.");
            } else {
                System.out.println("Failed to update category.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Display categories
    static void displayCategories() {
        // Use SQL query to select and display all categories
        String selectQuery = "SELECT * FROM category";

        try (Connection connection = DBConnector.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectQuery)) {

            System.out.println("\n====== Categories ======");
            while (resultSet.next()) {
                System.out.println("Category ID: " + resultSet.getInt("id"));
                System.out.println("Category Name: " + resultSet.getString("categoryName"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
