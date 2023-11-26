package com.progmeleon.mycafe.model;

import java.io.Serializable;

public class OrderItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private int itemId;
    private int quantity;
    private double totalPrice;

    public OrderItem(int itemId, int quantity, double totalPrice) {
        this.itemId = itemId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    // Add a method to calculate and return the total price of the order item
    public static double getTotalPrice() {
        return 0.0;
    }
}
