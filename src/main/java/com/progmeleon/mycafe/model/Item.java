package com.progmeleon.mycafe.model;

import java.io.Serializable;

public class Item implements Serializable {
    private static int nextItemId = 1;
    private int itemId;
    private String itemName;
    private double itemPrice;
    private int categoryId;

    public Item(String itemName, double itemPrice, int categoryId) {
        this.itemId = nextItemId++;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.categoryId = categoryId;
    }

    public int getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public static void setNextItemId(int nextItemId) {
        Item.nextItemId = nextItemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "Item [ID: " + itemId + ", Name: " + itemName + ", Price: PKR" + itemPrice + ", Category ID: " + categoryId + "]";
    }

    // Check if the item already exists
    public boolean equalsIgnoreCase(String itemNameToCompare, int categoryIdToCompare) {
        return this.itemName.equalsIgnoreCase(itemNameToCompare) && this.categoryId == categoryIdToCompare;
    }
}

