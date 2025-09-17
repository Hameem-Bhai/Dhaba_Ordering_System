package Entity;
import java.io.Serializable;

/**
 * Abstract base class for all menu items
 * Demonstrates ABSTRACTION - defines common interface for all menu items
 */
public abstract class MenuItem implements Serializable {
    private static final long serialVersionUID = 1L;
    // VARIABLE LEGEND: name, price, description, imagePath, available
    private String name;
    private double price;
    private String description;
    private String imagePath;
    private boolean available;
    
    // SECTION: Constructors
    public MenuItem(String name, double price) {
        this.name = name;
        this.price = price;
        this.description = "";
        this.imagePath = "";
        this.available = true;
    }
    
    public MenuItem(String name, double price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imagePath = "";
        this.available = true;
    }
    
    // SECTION: Getters (ENCAPSULATION)
    public String getName() {
        return name;
    }
    
    public double getPrice() {
        return price;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getImagePath() {
        return imagePath;
    }
    
    public boolean isAvailable() {
        return available;
    }
    
    // SECTION: Setters (ENCAPSULATION)
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name;
    }
    
    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
    }
    
    public void setDescription(String description) {
        this.description = description != null ? description : "";
    }
    
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath != null ? imagePath : "";
    }
    
    public void setAvailable(boolean available) {
        this.available = available;
    }
    
    // SECTION: Abstract methods (POLYMORPHISM)
    public abstract String getCategory();
    public abstract double calculateDiscount(double discountPercent);
    
    // SECTION: Common behavior
    public final String getDisplayInfo() {
        return String.format("%s - %.2f৳ (%s)", name, price, getCategory());
    }
    
    @Override
    public String toString() {
        return getDisplayInfo();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MenuItem menuItem = (MenuItem) obj;
        return name.equals(menuItem.name);
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
