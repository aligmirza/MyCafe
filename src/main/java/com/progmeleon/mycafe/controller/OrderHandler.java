package com.progmeleon.mycafe.controller;

import com.progmeleon.mycafe.config.ConfigureExistingData;
import com.progmeleon.mycafe.model.Item;
import com.progmeleon.mycafe.model.Order;
import com.progmeleon.mycafe.model.OrderItem;
import com.progmeleon.mycafe.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OrderHandler {
    private static List<Order> orders = new ArrayList<>();

    public void placeOrder() {
        Scanner scanner = new Scanner(System.in);

        // Get customer details
        System.out.print("Enter customer name: ");
        String customerName = scanner.nextLine();
        User customer = new User(customerName);

        // Display available items
        System.out.println("Available items:");
        displayItems(ConfigureExistingData.items);

        // Get items for the order
        List<OrderItem> orderItems = new ArrayList<>();
        char addMoreItems = 'n';
        do {
            System.out.print("Enter item name for the order: ");
            String itemName = scanner.nextLine();

            // Find the existing item by name
            Item existingItem = findItemByName(itemName, ConfigureExistingData.items);

            if (existingItem != null) {
                System.out.print("Enter quantity: ");
                int quantity = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character
                orderItems.add(new OrderItem(existingItem, quantity));
                System.out.print("Add more items? (y/n): ");
                addMoreItems = scanner.nextLine().charAt(0);
            } else {
                System.out.println("Item not found. Please enter a valid item name.");
            }
        } while (addMoreItems == 'y' || addMoreItems == 'Y');

        // Create a new order and add it to the orders list
        Order newOrder = new Order(customer);
        for (OrderItem orderItem : orderItems) {
            newOrder.addItem(orderItem.getItem(), orderItem.getQuantity());
        }
        orders.add(newOrder);
        generateBill(newOrder);
        System.out.println("Order taken successfully.");
    }

    public void displayOrders() {
        if (orders.isEmpty()) {
            System.out.println("No orders available.");
        } else {
            System.out.println("\n====== Orders ======");
            for (Order order : orders) {
                System.out.println(order);
            }
        }
    }

    private void displayItems(List<Item> itemList) {
        for (Item item : itemList) {
            System.out.println(item.getName());
        }
    }

    private Item findItemByName(String itemName, List<Item> itemList) {
        for (Item item : itemList) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return null;
    }

    private void generateBill(Order order) {
        System.out.println("\n====== Bill ======");
        System.out.println("Order ID: " + order.getOrderId());
        System.out.println("Customer: " + order.getCustomer().getName());
        System.out.println("\nItems:");

        for (OrderItem orderItem : order.getItems()) {
            Item item = orderItem.getItem();
            System.out.println("- " + item.getName() + ": $" + item.getPrice() +
                    " x " + orderItem.getQuantity() + " = $" + (item.getPrice() * orderItem.getQuantity()));
        }

        double totalAmount = order.getItems().stream()
                .mapToDouble(orderItem -> orderItem.getItem().getPrice() * orderItem.getQuantity())
                .sum();

        System.out.println("\nTotal Amount: $" + totalAmount);
    }
}
