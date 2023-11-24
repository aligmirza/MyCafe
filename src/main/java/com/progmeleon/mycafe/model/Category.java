package com.progmeleon.mycafe.model;

import java.io.Serializable;

public class Category implements Serializable {
    private static int nextCategoryId = 1;
    private int categoryId;
    private String categoryName;

    public Category(String categoryName) {
        this.categoryId = nextCategoryId++;
        this.categoryName = categoryName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    @Override
    public String toString() {
        return "Category [ID: " + categoryId + ", Name: " + categoryName + "]";
    }

    // Check if the category already exists
    public boolean equalsIgnoreCase(String categoryName) {
        return this.categoryName.equalsIgnoreCase(categoryName);
    }
}
