package com.progmeleon.mycafe.controller;

import com.progmeleon.mycafe.model.Category;

import java.util.List;
import java.util.Scanner;

public class CategoryController {
    private static List<Category> categories;

    public CategoryController(List<Category> categories) {
        this.categories = categories;
    }

    public void manageCategories() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n====== Category Management ======");
            System.out.println("1. Add Category");
            System.out.println("2. Delete Category");
            System.out.println("3. Update Category");
            System.out.println("4. Display Categories");
            System.out.println("5. Back to Admin Menu");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addCategory();
                    break;
                case 2:
                    deleteCategory();
                    break;
                case 3:
                    updateCategory();
                    break;
                case 4:
                    displayCategories();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (true);
    }

    // Add category
    private void addCategory() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter category name: ");
        String categoryName = scanner.nextLine();

        Category newCategory = new Category(categoryName);
        categories.add(newCategory);

        FileHandler.saveDataToFile(categories, "categories.ser");
        System.out.println("Category added successfully.");
    }

    // Delete category
    private void deleteCategory() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter category name to delete: ");
        String categoryName = scanner.nextLine();

        categories.removeIf(category -> category.getCategoryName().equalsIgnoreCase(categoryName));

        FileHandler.saveDataToFile(categories, "categories.ser");
        System.out.println("Category deleted successfully.");
    }

    // Update category
    private void updateCategory() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter category name to update: ");
        String oldCategoryName = scanner.nextLine();
        System.out.print("Enter new category name: ");
        String newCategoryName = scanner.nextLine();

        for (Category category : categories) {
            if (category.getCategoryName().equalsIgnoreCase(oldCategoryName)) {
                category.setCategoryName(newCategoryName);
                FileHandler.saveDataToFile(categories, "categories.ser");
                System.out.println("Category updated successfully.");
                return;
            }
        }

        System.out.println("Category not found.");
    }

    // Display categories
    static void displayCategories() {
        System.out.println("\n====== Categories ======");
        for (Category category : categories) {
            System.out.println(category.getCategoryName());
        }
    }
}
