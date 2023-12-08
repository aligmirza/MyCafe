package com.progmeleon.mycafe.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Category implements Serializable {

    private static int nextCategoryId = 1;
    private int categoryId;
    private String categoryName;

    public Category(@JsonProperty("categoryName")String categoryName) {
        this.categoryId = nextCategoryId++;
        this.categoryName = categoryName;
    }

    public Category(String categoryName, int categoryId) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public Category(int id, String categoryName) {
        this.categoryId = id;
        this.categoryName = categoryName;
    }

    public Category() {

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
        return categoryName;
    }

    // Check if the category already exists
    public boolean equalsIgnoreCase(String categoryName) {
        return this.categoryName.equalsIgnoreCase(categoryName);
    }

    public int getItemId() {
        return categoryId;
    }
}