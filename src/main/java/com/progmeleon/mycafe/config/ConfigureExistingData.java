package com.progmeleon.mycafe.config;

import com.progmeleon.mycafe.config.DBConnector;
import com.progmeleon.mycafe.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConfigureExistingData {
    public static List<Category> categories;
    public static List<Item> items;
    public static List<User> users;
    public static List<Deal> deals;

    public ConfigureExistingData() {
        loadExistingData();
    }

    public static void loadExistingData() {
        // Initialize lists
        categories = new ArrayList<>();
        items = new ArrayList<>();
        users = new ArrayList<>();
        deals = new ArrayList<>();
        loadCategories();



        // Use ExecutorService to run tasks concurrently
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // Create tasks for loading data from different tables
        List<Callable<Void>> tasks = new ArrayList<>();
//        tasks.add(() -> loadCategories());
//        tasks.add(() -> loadItems());
//        tasks.add(() -> loadUsers());
//        tasks.add(() -> loadDeals());

        try {
            // Invoke all tasks and wait for completion
            executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Shutdown the executor service
            executorService.shutdown();
        }
    }

    private static void loadCategories() {
        // Implement loading categories from the database
        // You can modify the SQL query based on your database schema
        String selectCategoriesQuery = "SELECT * FROM category";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectCategoriesQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String categoryName = resultSet.getString("categoryName");

                Category category = new Category(id, categoryName);
                categories.add(category);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void loadItems() {
        // Implement loading items from the database
        // You can modify the SQL query based on your database schema
        String selectItemsQuery = "SELECT * FROM item";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectItemsQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String itemName = resultSet.getString("itemName");
                double itemPrice = resultSet.getDouble("itemPrice");
                int categoryId = resultSet.getInt("categoryId");

                // Assuming you have a constructor in Item class that takes these parameters
                Item item = new Item(id, itemName, itemPrice, categoryId);
                items.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void loadUsers() {
        // Implement loading users from the database
        // You can modify the SQL query based on your database schema
        String selectUsersQuery = "SELECT * FROM users";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectUsersQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int userId = resultSet.getInt("userId");
                String name = resultSet.getString("name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");

                // Assuming you have a constructor in User class that takes these parameters
                User user = new User(userId, name, username, password, UserRole.valueOf(role.toUpperCase()));
                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void loadDeals() {
        // Implement loading deals from the database
        // You can modify the SQL query based on your database schema
        String selectDealsQuery = "SELECT * FROM deal";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectDealsQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int dealId = resultSet.getInt("dealId");
                String dealName = resultSet.getString("dealName");
                double dealPrice = resultSet.getDouble("dealPrice");
                String dealDescription = resultSet.getString("dealDescription");

                // Assuming you have a constructor in Deal class that takes these parameters
                Deal deal = new Deal(dealId, dealName, dealPrice, dealDescription);
                deals.add(deal);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
