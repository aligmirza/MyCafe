package com.progmeleon.mycafe.model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private static int lastOrderId = 0;
    private int orderId;
    private User customer;
    private List<OrderItem> items;

    public Order(User customer) {
        this.orderId = ++lastOrderId;
        this.customer = customer;
        this.items = new ArrayList<>();
    }

    public int getOrderId() {
        return orderId;
    }

    public User getCustomer() {
        return customer;
    }

    public List<OrderItem> getItems() {
        return new ArrayList<>(items);
    }

    public void addItem(Item item, int quantity) {
        OrderItem orderItem = new OrderItem(item, quantity);
        items.add(orderItem);
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", customer=" + customer +
                ", items=" + items +
                '}';
    }
}

