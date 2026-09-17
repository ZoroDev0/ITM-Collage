import java.util.HashSet;
import java.util.TreeSet;
import java.util.TreeMap;
import java.util.NavigableMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ProductInventoryManagementSystem {

    // HashSet for unique product categories
    private static HashSet<String> categories = new HashSet<>();

    // TreeSet for unique and automatically sorted product IDs
    private static TreeSet<Integer> productIds = new TreeSet<>();

    // TreeMap for product ID -> product details
    private static TreeMap<Integer, String> productCatalog = new TreeMap<>();

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int choice;

        System.out.println("===== Product Inventory Management System =====");

        do {
            System.out.println();
            System.out.println("1. Add Product Category");
            System.out.println("2. Add Product ID");
            System.out.println("3. Add Product to Catalog");
            System.out.println("4. Display All Products");
            System.out.println("5. Find Nearest Product ID");
            System.out.println("6. Display Products in ID Range");
            System.out.println("7. Remove Product");
            System.out.println("8. Exit");

            System.out.print("Enter your choice: ");

            try {
                choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {

                    case 1:
                        addCategory(scanner);
                        break;

                    case 2:
                        addProductId(scanner);
                        break;

                    case 3:
                        addProductToCatalog(scanner);
                        break;

                    case 4:
                        displayAllProducts();
                        break;

                    case 5:
                        findNearestProductId(scanner);
                        break;

                    case 6:
                        displayProductsInRange(scanner);
                        break;

                    case 7:
                        removeProduct(scanner);
                        break;

                    case 8:
                        System.out.println("Exiting Product Inventory Management System.");
                        break;

                    default:
                        System.out.println("Invalid choice. Please select 1 to 8.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number.");
                choice = 0;

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                choice = 0;
            }

        } while (choice != 8);

        scanner.close();
    }

    // ---------------------------------------------------------
    // 1. Add Product Category
    // ---------------------------------------------------------
    private static void addCategory(Scanner scanner) {

        System.out.print("Enter category name: ");
        String category = scanner.nextLine().trim();

        if (category.isEmpty()) {
            System.out.println("Error: Category name cannot be empty.");
            return;
        }

        boolean added = categories.add(category);

        if (added) {
            System.out.println("Category added: " + category);
        } else {
            System.out.println("Category already exists. Duplicate ignored.");
        }
    }

    // ---------------------------------------------------------
    // 2. Add Product ID
    // ---------------------------------------------------------
    private static void addProductId(Scanner scanner) {

        try {
            System.out.print("Enter product ID: ");
            int productId = Integer.parseInt(scanner.nextLine());

            boolean added = productIds.add(productId);

            if (added) {
                System.out.println("Product ID added successfully.");
            } else {
                System.out.println("Product ID already exists. Duplicate ignored.");
            }

            System.out.println();
            System.out.println("===== Product IDs (Sorted) =====");

            if (productIds.isEmpty()) {
                System.out.println("No product IDs available.");
            } else {

                Iterator<Integer> iterator = productIds.iterator();

                while (iterator.hasNext()) {
                    System.out.println(iterator.next());
                }
            }

        } catch (NumberFormatException e) {
            System.out.println("Error: Product ID must be a valid integer.");
        }
    }

    // ---------------------------------------------------------
    // 3. Add Product to Catalog
    // ---------------------------------------------------------
    private static void addProductToCatalog(Scanner scanner) {

        try {
            System.out.print("Enter product ID: ");
            int productId = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter product name and price: ");
            String details = scanner.nextLine().trim();

            if (details.isEmpty()) {
                System.out.println("Error: Product details cannot be empty.");
                return;
            }

            productCatalog.put(productId, details);

            // Keep TreeSet synchronized with TreeMap
            productIds.add(productId);

            System.out.println("Product added to catalog.");

        } catch (NumberFormatException e) {
            System.out.println("Error: Product ID must be a valid integer.");
        }
    }

    // ---------------------------------------------------------
    // 4. Display All Products
    // ---------------------------------------------------------
    private static void displayAllProducts() {

        System.out.println();
        System.out.println("===== All Products (Sorted by ID) =====");

        if (productCatalog.isEmpty()) {
            System.out.println("Product catalog is empty.");
            return;
        }

        for (var entry : productCatalog.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }

    // ---------------------------------------------------------
    // 5. Find Nearest Product ID
    // ---------------------------------------------------------
    private static void findNearestProductId(Scanner scanner) {

        try {

            if (productCatalog.isEmpty()) {
                throw new NoSuchElementException(
                        "Product catalog is empty. Add products first."
                );
            }

            System.out.print("Enter ID to search nearest: ");
            int searchId = Integer.parseInt(scanner.nextLine());

            NavigableMap<Integer, String> catalog = productCatalog;

            Integer floorId = catalog.floorKey(searchId);
            Integer ceilingId = catalog.ceilingKey(searchId);

            System.out.println();

            if (floorId != null) {
                System.out.println("Floor ID  : " + floorId);
            } else {
                System.out.println("Floor ID  : No lower product ID found.");
            }

            if (ceilingId != null) {
                System.out.println("Ceiling ID: " + ceilingId);
            } else {
                System.out.println("Ceiling ID: No higher product ID found.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Error: Product ID must be a valid integer.");

        } catch (NoSuchElementException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // 6. Display Products in ID Range
    // ---------------------------------------------------------
    private static void displayProductsInRange(Scanner scanner) {

        try {

            if (productCatalog.isEmpty()) {
                throw new NoSuchElementException(
                        "Product catalog is empty. Add products first."
                );
            }

            System.out.print("Enter starting ID: ");
            int fromId = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter ending ID: ");
            int toId = Integer.parseInt(scanner.nextLine());

            if (fromId > toId) {
                System.out.println("Error: Starting ID cannot be greater than ending ID.");
                return;
            }

            NavigableMap<Integer, String> catalog = productCatalog;

            // subMap() returns products within the specified range.
            NavigableMap<Integer, String> range =
                    catalog.subMap(fromId, true, toId, true);

            System.out.println();
            System.out.println("===== Products in Range =====");

            if (range.isEmpty()) {
                System.out.println("No products found in the specified range.");
            } else {

                for (var entry : range.entrySet()) {
                    System.out.println(
                            entry.getKey() + " -> " + entry.getValue()
                    );
                }
            }

            // Demonstrating headMap() and tailMap()
            System.out.println();
            System.out.println("HeadMap size: " + catalog.headMap(toId, true).size());
            System.out.println("TailMap size: " + catalog.tailMap(fromId, true).size());

        } catch (NumberFormatException e) {
            System.out.println("Error: Product IDs must be valid integers.");

        } catch (NoSuchElementException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // 7. Remove Product
    // ---------------------------------------------------------
    private static void removeProduct(Scanner scanner) {

        try {

            if (productCatalog.isEmpty()) {
                throw new NoSuchElementException(
                        "Product catalog is empty. Nothing to remove."
                );
            }

            System.out.print("Enter product ID to remove: ");
            int productId = Integer.parseInt(scanner.nextLine());

            if (productCatalog.containsKey(productId)) {

                productCatalog.remove(productId);
                productIds.remove(productId);

                System.out.println("Product removed successfully.");

            } else {

                System.out.println(
                        "Product ID not found in the catalog."
                );
            }

        } catch (NumberFormatException e) {
            System.out.println("Error: Product ID must be a valid integer.");

        } catch (NoSuchElementException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}