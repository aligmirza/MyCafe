package com.progmeleon.mycafe.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Item implements Serializable {
    private static int nextItemId = 1;
    private int itemId;
    private String itemName;
    private double itemPrice;
    private int categoryId;

    public Item(@JsonProperty("itemname") String itemName,@JsonProperty("itemprice") double itemPrice, @JsonProperty("itemcategory") int categoryId) {
        this.itemId = nextItemId++;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.categoryId = categoryId;
    }

    public Item(int id, String itemName, double itemPrice, int categoryId) {
        this.itemId = id;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.categoryId = categoryId;
    }

    public Item(String itemName, double itemPrice) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    public Item() {

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

    public double getPrice() {
        return itemPrice;
    }

    public String getName() {
        return itemName;
    }

}

