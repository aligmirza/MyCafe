package com.progmeleon.mycafe.controller;

import com.progmeleon.mycafe.config.ConfigureExistingData;
import com.progmeleon.mycafe.model.Item;
import com.progmeleon.mycafe.model.Order;
import com.progmeleon.mycafe.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OrderHandler {
    private static List<Order> orders = new ArrayList<>();

    public static void placeOrder() {
        Scanner scanner = new Scanner(System.in);

        // Get customer details
        System.out.print("Enter customer name: ");
        String customerName = scanner.nextLine();
        User customer = new User(customerName);

        // Display available items
        System.out.println("Available items:");
        displayItems(ConfigureExistingData.items);

        // Get items for the order
        List<Item> orderItems = new ArrayList<>();
        char addMoreItems = 'n';
        do {
            System.out.print("Enter item name for the order: ");
            String itemName = scanner.nextLine();

            // Find the existing item by name
            Item existingItem = findItemByName(itemName, ConfigureExistingData.items);

            if (existingItem != null) {
                orderItems.add(existingItem);
                System.out.print("Add more items? (y/n): ");
                addMoreItems = scanner.nextLine().charAt(0);
            } else {
                System.out.println("Item not found. Please enter a valid item name.");
            }
        } while (addMoreItems == 'y' || addMoreItems == 'Y');

        // Create a new order and add it to the orders list
        Order newOrder = new Order(customer, orderItems);
        orders.add(newOrder);
        generateBill(newOrder);
        System.out.println("Order taken successfully.");
    }

    private static Item findItemByName(String itemName, List<Item> itemList) {
        for (Item item : itemList) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return null;
    }

    private static void displayItems(List<Item> itemList) {
        for (Item item : itemList) {
            System.out.println(item.getName());
        }
    }

    private static void generateBill(Order order) {
        System.out.println("\n====== Bill ======");
        System.out.println("Order ID: " + order.getOrderId());
        System.out.println("Customer: " + order.getCustomer().getName());
        System.out.println("\nItems:");

        for (Item item : order.getItems()) {
            System.out.println("- " + item.getName() + ": $" + item.getPrice());
        }

        double totalAmount = order.getItems().stream().mapToDouble(Item::getPrice).sum();
        System.out.println("\nTotal Amount: $" + totalAmount);
    }

    public static void displayOrders() {
        if (orders.isEmpty()) {
            System.out.println("No orders available.");
        } else {
            System.out.println("\n====== Orders ======");
            for (Order order : orders) {
                System.out.println(order);
            }
        }
    }
}
