package com.progmeleon.mycafe.model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private static int lastOrderId = 0;
    private int orderId;
    private User customer;
    private List<Item> items;

    public Order(User customer, List<Item> items) {
        this.orderId = ++lastOrderId;
        this.customer = customer;
        this.items = new ArrayList<>(items);
    }

    public int getOrderId() {
        return orderId;
    }

    public User getCustomer() {
        return customer;
    }

    public List<Item> getItems() {
        return new ArrayList<>(items);
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
