package Entity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DataManager class for handling file I/O operations
 * Demonstrates FILE I/O, EXCEPTION HANDLING, and ENCAPSULATION
 */
public class DataManager {
    private static final String ORDERS_FILE = "Data/orders.dat";
    private static final String CUSTOMERS_FILE = "Data/customers.dat";
    private static final String REPORTS_FILE = "Data/reports.txt";
    
    // Private constructor to prevent instantiation (utility class)
    private DataManager() {}
    
    // Save orders to file with EXCEPTION HANDLING
    public static void saveOrders(List<Order> orders) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ORDERS_FILE))) {
            oos.writeObject(orders);
            System.out.println("Orders saved successfully to " + ORDERS_FILE);
        } catch (IOException e) {
            System.err.println("Error saving orders: " + e.getMessage());
            throw new RuntimeException("Failed to save orders", e);
        }
    }
    
    // Load orders from file with EXCEPTION HANDLING
    @SuppressWarnings("unchecked")
    public static List<Order> loadOrders() {
        File file = new File(ORDERS_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Order>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading orders: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    // Save customers to file with EXCEPTION HANDLING
    public static void saveCustomers(List<Customer> customers) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CUSTOMERS_FILE))) {
            oos.writeObject(customers);
            System.out.println("Customers saved successfully to " + CUSTOMERS_FILE);
        } catch (IOException e) {
            System.err.println("Error saving customers: " + e.getMessage());
            throw new RuntimeException("Failed to save customers", e);
        }
    }
    
    // Load customers from file with EXCEPTION HANDLING
    @SuppressWarnings("unchecked")
    public static List<Customer> loadCustomers() {
        File file = new File(CUSTOMERS_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Customer>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading customers: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    // Generate and save reports with EXCEPTION HANDLING
    public static void generateReport(List<Order> orders, List<Customer> customers) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(REPORTS_FILE))) {
            writer.println("=== HAMEEM DHABA SALES REPORT ===");
            writer.println("Generated on: " + java.time.LocalDateTime.now());
            writer.println();
            
            // Order statistics
            writer.println("ORDER STATISTICS:");
            writer.println("Total Orders: " + orders.size());
            writer.println("Total Revenue: " + String.format("%.2f", 
                orders.stream().mapToDouble(Order::getTotalPrice).sum()) + "৳");
            writer.println("Average Order Value: " + String.format("%.2f", 
                orders.stream().mapToDouble(Order::getTotalPrice).average().orElse(0.0)) + "৳");
            writer.println();
            
            // Customer statistics
            writer.println("CUSTOMER STATISTICS:");
            writer.println("Total Customers: " + customers.size());
            writer.println("Gold Customers: " + customers.stream().mapToInt(c -> 
                "Gold".equals(c.getCustomerTier()) ? 1 : 0).sum());
            writer.println("Silver Customers: " + customers.stream().mapToInt(c -> 
                "Silver".equals(c.getCustomerTier()) ? 1 : 0).sum());
            writer.println("Bronze Customers: " + customers.stream().mapToInt(c -> 
                "Bronze".equals(c.getCustomerTier()) ? 1 : 0).sum());
            writer.println();
            
            // Top customers
            writer.println("TOP CUSTOMERS BY SPENDING:");
            customers.stream()
                .sorted((c1, c2) -> Double.compare(c2.getTotalSpent(), c1.getTotalSpent()))
                .limit(5)
                .forEach(c -> writer.println(c.getName() + ": " + 
                    String.format("%.2f", c.getTotalSpent()) + "৳ (" + c.getCustomerTier() + ")"));
            
        } catch (IOException e) {
            System.err.println("Error generating report: " + e.getMessage());
            throw new RuntimeException("Failed to generate report", e);
        }
    }
    
    // Backup data with EXCEPTION HANDLING
    public static void backupData() {
        String timestamp = java.time.LocalDateTime.now().toString().replace(":", "-");
        String backupDir = "cafe/Data/backup_" + timestamp;
        
        try {
            new File(backupDir).mkdirs();
            
            // Backup orders
            if (new File(ORDERS_FILE).exists()) {
                copyFile(ORDERS_FILE, backupDir + "/orders.dat");
            }
            
            // Backup customers
            if (new File(CUSTOMERS_FILE).exists()) {
                copyFile(CUSTOMERS_FILE, backupDir + "/customers.dat");
            }
            
            // Backup reports
            if (new File(REPORTS_FILE).exists()) {
                copyFile(REPORTS_FILE, backupDir + "/reports.txt");
            }
            
            System.out.println("Data backed up to: " + backupDir);
            
        } catch (IOException e) {
            System.err.println("Error creating backup: " + e.getMessage());
            throw new RuntimeException("Failed to create backup", e);
        }
    }
    
    // Helper method for file copying
    private static void copyFile(String source, String destination) throws IOException {
        try (FileInputStream fis = new FileInputStream(source);
             FileOutputStream fos = new FileOutputStream(destination)) {
            
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
        }
    }
    
    // Create data directory if it doesn't exist
    public static void ensureDataDirectory() {
        File dataDir = new File("Data"); // Remove "cafe/"
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }
}
