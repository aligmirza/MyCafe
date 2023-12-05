package com.progmeleon.mycafe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.progmeleon.mycafe.config.ConfigureExistingData;
import com.progmeleon.mycafe.config.DBConnector;
import com.progmeleon.mycafe.model.Deal;
import com.progmeleon.mycafe.model.Item;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static com.progmeleon.mycafe.config.ConfigureApp.DEALS_FILE_PATH;
import static com.progmeleon.mycafe.controller.FileHandler.saveDataToFile;
import static com.progmeleon.mycafe.controller.InventorySystem.displayItems;
import static com.progmeleon.mycafe.controller.ItemController.findItemByName;

public class DealHandler {


    private static List<Deal> deals = new ArrayList<>();


    static {
        deals = ConfigureExistingData.deals;
    }

    public static void saveDealsToFile() {
        saveDataToFile(deals, DEALS_FILE_PATH);
    }

    public static void manageDeals() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n====== Deal Management ======");
            System.out.println("1. Add Deal");
            System.out.println("2. Update Deal");
            System.out.println("3. Delete Deal");
            System.out.println("4. Display Deals");
            System.out.println("5. Back to Admin Menu");
            System.out.print("Enter your choice: ");
            try {
                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        makeDeal();
                        break;
                    case 2:
                        updateDeal();
                        break;
                    case 3:
                        deleteDeal();
                        break;
                    case 4:
                        displayDeals();
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume the newline character
            }

        } while (true);
    }

    public static void makeDeal() {
        Scanner scanner = new Scanner(System.in);

        // Get deal details from the user
        System.out.print("Enter deal name: ");
        String dealName = scanner.nextLine();
        System.out.print("Enter deal price: ");
        double dealPrice = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline character
        System.out.print("Enter deal description: ");
        String dealDescription = scanner.nextLine();

        // Display existing items for user selection
        displayItems();

        // Get items for the deal
        List<Item> dealItems = new ArrayList<>();
        System.out.print("Enter the number of items in the deal: ");
        int itemCount = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        for (int i = 0; i < itemCount; i++) {
            System.out.print("Enter item name for deal: ");
            String itemName = scanner.nextLine();

            // Find the existing item by name
            Item existingItem = findItemByName(itemName);

            if (existingItem != null) {
                dealItems.add(existingItem);
            } else {
                System.out.println("Item not found. Please enter a valid item name.");
                i--; // Decrement i to re-enter the item name
            }
        }

        // Use SQL query to add the deal
        String insertDealQuery = "INSERT INTO deal (dealName, dealPrice, dealDescription) VALUES (?, ?, ?)";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertDealQuery, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, dealName);
            preparedStatement.setDouble(2, dealPrice);
            preparedStatement.setString(3, dealDescription);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                // Retrieve the generated deal ID
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int dealId = generatedKeys.getInt(1);

                    // Use SQL query to add deal items
                    String insertDealItemQuery = "INSERT INTO deal_item (dealId, itemId) VALUES (?, ?)";
                    try (PreparedStatement dealItemStatement = connection.prepareStatement(insertDealItemQuery)) {
                        for (Item item : dealItems) {
                            dealItemStatement.setInt(1, dealId);
                            dealItemStatement.setInt(2, item.getItemId());
                            dealItemStatement.addBatch();
                        }
                        dealItemStatement.executeBatch();
                    }

                    System.out.println("Deal added successfully.");
                } else {
                    System.out.println("Failed to retrieve deal ID.");
                }
            } else {
                System.out.println("Failed to add deal.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update deal
    public static void updateDeal() {
        Scanner scanner = new Scanner(System.in);

        // Get the deal ID to update
        System.out.print("Enter the ID of the deal to update: ");
        int dealId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        // Use SQL query to update the deal
        String updateDealQuery = "UPDATE deal SET dealName = ?, dealPrice = ?, dealDescription = ? WHERE id = ?";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateDealQuery)) {

            // Get updated details from the user
            System.out.print("Enter updated deal name: ");
            String updatedDealName = scanner.nextLine();
            System.out.print("Enter updated deal price: ");
            double updatedDealPrice = scanner.nextDouble();
            scanner.nextLine(); // Consume the newline character
            System.out.print("Enter updated deal description: ");
            String updatedDealDescription = scanner.nextLine();

            preparedStatement.setString(1, updatedDealName);
            preparedStatement.setDouble(2, updatedDealPrice);
            preparedStatement.setString(3, updatedDealDescription);
            preparedStatement.setInt(4, dealId);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Deal updated successfully.");
            } else {
                System.out.println("Failed to update deal.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete deal
    public static void deleteDeal() {
        Scanner scanner = new Scanner(System.in);

        // Get the deal ID to delete
        System.out.print("Enter the ID of the deal to delete: ");
        int dealId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        // Use SQL query to delete the deal items
        String deleteDealItemsQuery = "DELETE FROM deal_item WHERE dealId = ?";

        // Use SQL query to delete the deal
        String deleteDealQuery = "DELETE FROM deal WHERE id = ?";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement deleteDealItemsStatement = connection.prepareStatement(deleteDealItemsQuery);
             PreparedStatement deleteDealStatement = connection.prepareStatement(deleteDealQuery)) {

            deleteDealItemsStatement.setInt(1, dealId);
            deleteDealItemsStatement.executeUpdate();

            deleteDealStatement.setInt(1, dealId);
            int rowsAffected = deleteDealStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Deal deleted successfully.");
            } else {
                System.out.println("Deal not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Display deals
    public static void displayDeals() {
        // Use SQL query to select and display all deals with their items
        String selectDealsQuery = "SELECT * FROM deal";
        String selectDealItemsQuery = "SELECT item.id, item.itemName, item.itemPrice FROM deal_item " +
                "JOIN item ON deal_item.itemId = item.id WHERE dealId = ?";

        try (Connection connection = DBConnector.getConnection();
             Statement selectDealsStatement = connection.createStatement();
             ResultSet dealsResultSet = selectDealsStatement.executeQuery(selectDealsQuery)) {

            System.out.println("\n====== Deals ======");
            while (dealsResultSet.next()) {
                int dealId = dealsResultSet.getInt("id");
                String dealName = dealsResultSet.getString("dealName");
                double dealPrice = dealsResultSet.getDouble("dealPrice");
                String dealDescription = dealsResultSet.getString("dealDescription");

                System.out.println("Deal ID: " + dealId);
                System.out.println("Deal Name: " + dealName);
                System.out.println("Deal Price: " + dealPrice);
                System.out.println("Deal Description: " + dealDescription);
                System.out.println("Deal Items:");

                // Retrieve and display items for the current deal
                try (PreparedStatement selectDealItemsStatement = connection.prepareStatement(selectDealItemsQuery)) {
                    selectDealItemsStatement.setInt(1, dealId);
                    ResultSet dealItemsResultSet = selectDealItemsStatement.executeQuery();

                    while (dealItemsResultSet.next()) {
                        int itemId = dealItemsResultSet.getInt("id");
                        String itemName = dealItemsResultSet.getString("itemName");
                        double itemPrice = dealItemsResultSet.getDouble("itemPrice");

                        System.out.println("- Item ID: " + itemId);
                        System.out.println("  Item Name: " + itemName);
                        System.out.println("  Item Price: " + itemPrice);
                    }
                }

                System.out.println("==========");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private static int findDealIndexById(int dealId) {
        for (int i = 0; i < deals.size(); i++) {
            if (deals.get(i).getId() == dealId) {
                return i;
            }
        }
        return -1;
    }
}