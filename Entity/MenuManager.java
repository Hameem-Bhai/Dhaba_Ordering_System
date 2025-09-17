package Entity;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * MenuManager class for managing menu items
 * Demonstrates ENCAPSULATION, FILE I/O, and EXCEPTION HANDLING
 */
public class MenuManager {
    private List<MenuItem> menuItems;
    private String dataFilePath;
    
    // Constructor with ENCAPSULATION
     public MenuManager() {
        this.menuItems = new ArrayList<>();
        this.dataFilePath = "Data/data.txt";
        ensureDataDirectory();
        loadMenuItems();
    }
    
    public MenuManager(String dataFilePath) {
        this.menuItems = new ArrayList<>();
        this.dataFilePath = dataFilePath;
        ensureDataDirectory();
    
        loadMenuItems();
    }
    
    // Getter methods (ENCAPSULATION)
    public List<MenuItem> getMenuItems() {
        return new ArrayList<>(menuItems); // Return copy to prevent external modification
    }
    
    public List<MenuItem> getAvailableItems() {
        return menuItems.stream()
                .filter(MenuItem::isAvailable)
                .collect(Collectors.toList());
    }
    private void ensureDataDirectory()
    {
        File dataDir = new File("Data");
        if(!dataDir.exists())
        {
            dataDir.mkdir();
        }
    }

    public List<MenuItem> getItemsByCategory(String category) {
        return menuItems.stream()
                .filter(item -> item.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }
    
    // Business logic methods
    public void addMenuItem(MenuItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Menu item cannot be null");
        }
        menuItems.add(item);
        saveMenuItems(); // Auto-save after changes
    }
    
    public void removeMenuItem(String name) {
        menuItems.removeIf(item -> item.getName().equalsIgnoreCase(name));
        saveMenuItems();
    }
    
    public MenuItem findMenuItem(String name) {
        return menuItems.stream()
                .filter(item -> item.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
    
    public void updateMenuItemPrice(String name, double newPrice) {
        MenuItem item = findMenuItem(name);
        if (item != null) {
            item.setPrice(newPrice);
            saveMenuItems();
        } else {
            throw new NoSuchElementException("Menu item not found: " + name);
        }
    }
    
    // FILE I/O methods with EXCEPTION HANDLING
    public void saveMenuItems() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(dataFilePath))) {
            for (MenuItem item : menuItems) {
                writer.println(item.getClass().getSimpleName() + "|" + 
                              item.getName() + "|" + 
                              item.getPrice() + "|" + 
                              item.getDescription() + "|" + 
                              item.getImagePath() + "|" + 
                              item.isAvailable());
            }
        } catch (IOException e) {
            System.err.println("Error saving menu items: " + e.getMessage());
            throw new RuntimeException("Failed to save menu data", e);
        }
    }
    
    public void loadMenuItems() {
        File dataFile = new File(dataFilePath);
        if (!dataFile.exists()) {
            initializeDefaultMenu();
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 6) {
                    String type = parts[0];
                    String name = parts[1];
                    double price = Double.parseDouble(parts[2]);
                    String description = parts[3];
                    String imagePath = parts[4];
                    boolean available = Boolean.parseBoolean(parts[5]);
                    
                    MenuItem item;
                    if ("Drinks".equals(type)) {
                        item = new Drinks(name, price, description);
                    } else if ("Food".equals(type)) {
                        item = new Food(name, price, description);
                    } else {
                        continue; 
                    }
                    
                    item.setImagePath(imagePath);
                    item.setAvailable(available);
                    menuItems.add(item);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading menu items: " + e.getMessage());
            initializeDefaultMenu(); // Fallback to default menu
        } catch (NumberFormatException e) {
            System.err.println("Error parsing menu data: " + e.getMessage());
            initializeDefaultMenu();
        }
    }
    
    private void initializeDefaultMenu() {
        
        menuItems.clear();
        
        // Drinks
        menuItems.add(new Drinks("Coffee", 50.0, "Freshly brewed coffee"));
        menuItems.add(new Drinks("Water", 20.0, "Pure drinking water"));
        menuItems.add(new Drinks("Dudh Cha", 15.0, "Traditional milk tea"));
        menuItems.add(new Drinks("Red Tea", 10.0, "Classic red tea"));
        menuItems.add(new Drinks("Mojo", 25.0, "Refreshing mojo drink"));
        menuItems.add(new Drinks("Coke", 25.0, "Classic Coca Cola"));
        menuItems.add(new Drinks("Pran Milk", 50.0, "Fresh milk"));
        menuItems.add(new Drinks("Sprite", 25.0, "Lemon-lime soda"));
        menuItems.add(new Drinks("Boba Tea", 150.0, "Classic boba tea"));
        menuItems.add(new Drinks("Caramel Boba Tea", 350.0, "Caramel flavored boba"));
        menuItems.add(new Drinks("Strawberry Boba Tea", 210.0, "Strawberry flavored boba"));
        menuItems.add(new Drinks("Lychee Boba Tea", 180.0, "Lychee flavored boba"));
        menuItems.add(new Drinks("Mango Boba Tea", 280.0, "Mango flavored boba"));
        menuItems.add(new Drinks("Oreo Boba Tea", 380.0, "Oreo flavored boba"));
        menuItems.add(new Drinks("Matcha Boba Tea", 450.0, "Matcha flavored boba"));
        menuItems.add(new Drinks("Taro Boba Tea", 500.0, "Taro flavored boba"));
        
        // Food
        menuItems.add(new Food("Sandwich", 60.0, "Fresh sandwich"));
        menuItems.add(new Food("Shawarma", 120.0, "Delicious shawarma"));
        menuItems.add(new Food("Burger", 200.0, "Juicy burger"));
        menuItems.add(new Food("Chicken Roll", 60.0, "Crispy chicken roll"));
        menuItems.add(new Food("Meat Box", 140.0, "Meat combo box"));
        menuItems.add(new Food("Paratha", 20.0, "Traditional paratha"));
        menuItems.add(new Food("Grilled Chicken", 150.0, "Grilled chicken breast"));
        menuItems.add(new Food("Pizza", 300.0, "Delicious pizza"));
        menuItems.add(new Food("Waffle", 150.0, "Sweet waffle"));
        
        saveMenuItems();
    }
    
    public int getTotalItems() {
        return menuItems.size();
    }
    
    public int getAvailableItemsCount() {
        return (int) menuItems.stream().filter(MenuItem::isAvailable).count();
    }
}
