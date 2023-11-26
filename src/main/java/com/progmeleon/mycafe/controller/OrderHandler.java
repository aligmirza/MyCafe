package com.progmeleon.mycafe.controller;

import com.progmeleon.mycafe.model.Item;
import com.progmeleon.mycafe.model.Order;
import com.progmeleon.mycafe.model.OrderStatus;
import com.progmeleon.mycafe.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OrderHandler {
    private static List<Order> orders;

    public OrderHandler() {
        orders = new ArrayList<>();
        loadOrders();
    }

    private void loadOrders() {
        try (ObjectInputStream orderInputStream = new ObjectInputStream(new FileInputStream("orders.ser"))) {
            orders = (List<Order>) orderInputStream.readObject();
            System.out.println("Orders loaded successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("Orders file not found. Creating new order data.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveOrders() {
        try (ObjectOutputStream orderOutputStream = new ObjectOutputStream(new FileOutputStream("orders.ser"))) {
            orderOutputStream.writeObject(orders);
            System.out.println("Orders saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void placeOrder(User user, List<Item> items) {
        int orderId = generateOrderId();
        Order order = new Order(orderId, user, items, OrderStatus.PENDING);
        orders.add(order);
        saveOrders();
        System.out.println("Order placed successfully. Order ID: " + orderId);
    }

    private int generateOrderId() {
        // Implement your logic to generate a unique order ID
        // This can be based on the current time, a counter, etc.
        // For simplicity, a placeholder implementation is used here.
        return orders.size() + 1;
    }

    public void displayOrders() {
        if (orders.isEmpty()) {
            System.out.println("No orders available.");
        } else {
            for (Order order : orders) {
                System.out.println(order);
            }
        }
    }

    public void updateOrderStatus(int orderId, OrderStatus newStatus) {
        for (Order order : orders) {
            if (order.getOrderId() == orderId) {
                order.setOrderStatus(newStatus);
                saveOrders();
                System.out.println("Order status updated successfully.");
                return;
            }
        }
        System.out.println("Order not found.");
    }
}
