package com.progmeleon.mycafe.model;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        InventorySystem inventorySystem = new InventorySystem();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n====== Menu ======");
            System.out.println("1. Add Category");
            System.out.println("2. Add Item");
            System.out.println("3. Display Items");
            System.out.println("4. Change Username");
            System.out.println("5. Change Password");
            System.out.println("6. Save Data and Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character



            switch (choice) {
                case 1:
                    System.out.print("Enter category name: ");
                    String categoryName = scanner.nextLine();
                    inventorySystem.addCategory(categoryName);
                    break;
                case 2:
                    System.out.print("Enter item name: ");
                    String itemName = scanner.nextLine();
                    System.out.print("Enter item price: ");
                    double itemPrice = scanner.nextDouble();
                    System.out.print("Enter category ID: ");
                    int categoryId = scanner.nextInt();
                    inventorySystem.addItem(itemName, itemPrice, categoryId);
                    break;
                case 3:
                    InventorySystem.displayItems(inventorySystem.getItems());
                    break;
                case 4:
                    System.out.print("Enter new username: ");
                    String newUsername = scanner.nextLine();
                    inventorySystem.changeUsername(newUsername);
                    break;
                case 5:
                    System.out.print("Enter new password: ");
                    String newPassword = scanner.nextLine();
                    inventorySystem.changePassword(newPassword);
                    break;
                case 6:
                    inventorySystem.saveData();
                    System.out.println("Exiting the program. Thank you!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }


}
