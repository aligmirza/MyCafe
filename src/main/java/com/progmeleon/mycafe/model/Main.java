package com.progmeleon.mycafe.model;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        InventorySystem inventorySystem = new InventorySystem();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Fast-Food Inventory System =====");
            System.out.println("1. Add Category");
            System.out.println("2. Add Item");
            System.out.println("3. Display Categories and Items");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    System.out.print("Enter Category Name: ");
                    String categoryName = scanner.nextLine();
                    inventorySystem.addCategory(categoryName);
                    break;

                case 2:
                    System.out.print("Enter Item Name: ");
                    String itemName = scanner.nextLine();
                    System.out.print("Enter Item Price: ");
                    double itemPrice = scanner.nextDouble();
                    scanner.nextLine(); // Consume the newline character
                    System.out.print("Enter Category ID: ");
                    int categoryId = scanner.nextInt();
                    inventorySystem.addItem(itemName, itemPrice, categoryId);
                    break;

                case 3:
                    System.out.println("\nCategories:");
                    for (Category category : inventorySystem.getCategories()) {
                        System.out.println(category);
                    }

                    System.out.println("\nItems:");
                    for (Item item : inventorySystem.getItems()) {
                        System.out.println(item);
                    }
                    break;

                case 4:
                    System.out.println("Exiting the program. Goodbye!");
                    System.exit(0);

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}

