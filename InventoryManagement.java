package org.example;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.Scanner;

public class InventoryManagement {
    private static MongoCollection<Document> collection;

    public static void main(String[] args) {
        // Connect to MongoDB
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("inventory_db");
        collection = database.getCollection("items");

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Inventory Management System:");
            System.out.println("1. Add Item");
            System.out.println("2. Update Item");
            System.out.println("3. Remove Item");
            System.out.println("4. View Inventory");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addItem(scanner);
                    break;
                case 2:
                    updateItem(scanner);
                    break;
                case 3:
                    removeItem(scanner);
                    break;
                case 4:
                    viewInventory();
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        mongoClient.close();
        scanner.close();
    }

    private static void addItem(Scanner scanner) {
        System.out.print("Enter item name: ");
        String name = scanner.nextLine();
        System.out.print("Enter item quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter item price: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter item category: ");
        String category = scanner.nextLine();

        Document item = new Document("name", name)
                .append("quantity", quantity)
                .append("price", price)
                .append("category", category);

        collection.insertOne(item);
        System.out.println("Item added successfully.");
    }

    private static void updateItem(Scanner scanner) {
        System.out.print("Enter item name to update: ");
        String name = scanner.nextLine();

        Document item = collection.find(new Document("name", name)).first();
        if (item == null) {
            System.out.println("Item not found.");
            return;
        }

        System.out.println("What do you want to update?");
        System.out.println("1. Name");
        System.out.println("2. Quantity");
        System.out.println("3. Price");
        System.out.println("4. Category");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                System.out.print("Enter new name: ");
                String newName = scanner.nextLine();
                collection.updateOne(Filters.eq("name", name), Updates.set("name", newName));
                System.out.println("Item name updated successfully.");
                break;
            case 2:
                System.out.print("Enter new quantity: ");
                int quantity = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                collection.updateOne(Filters.eq("name", name), Updates.set("quantity", quantity));
                System.out.println("Item quantity updated successfully.");
                break;
            case 3:
                System.out.print("Enter new price: ");
                double price = scanner.nextDouble();
                scanner.nextLine(); // Consume newline
                collection.updateOne(Filters.eq("name", name), Updates.set("price", price));
                System.out.println("Item price updated successfully.");
                break;
            case 4:
                System.out.print("Enter new category: ");
                String category = scanner.nextLine();
                collection.updateOne(Filters.eq("name", name), Updates.set("category", category));
                System.out.println("Item category updated successfully.");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void removeItem(Scanner scanner) {
        System.out.print("Enter item name: ");
        String name = scanner.nextLine();

        collection.deleteOne(new Document("name", name));
        System.out.println("Item removed successfully.");
    }

    private static void viewInventory() {
        for (Document doc : collection.find()) {
            System.out.println("Item: " + doc.getString("name") + ", Quantity: " + doc.getInteger("quantity") +
                    ", Price: " + doc.getDouble("price") + ", Category: " + doc.getString("category"));
        }
    }
}
