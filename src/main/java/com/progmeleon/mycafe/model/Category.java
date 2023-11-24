package com.progmeleon.mycafe.model;

import java.io.Serializable;

public class Category implements Serializable {
    private static final long serialVersionUID = 123456789L;

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

    public static void setNextCategoryId(int nextCategoryId) {
        Category.nextCategoryId = nextCategoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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
