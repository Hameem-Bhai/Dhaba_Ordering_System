package Entity;
import java.io.Serializable;

/**
 * OrderItem class - represents an item in an order
 * Demonstrates ENCAPSULATION and COMPOSITION
 */
public class OrderItem implements Serializable {
    private static final long serialVersionUID = 1L;
    // VARIABLE LEGEND: item, quantity, specialInstructions
    private MenuItem item;
    private int quantity;
    private String specialInstructions;

    // SECTION: Constructors (ENCAPSULATION)
    public OrderItem(MenuItem item, int quantity) {
        if (item == null) {
            throw new IllegalArgumentException("Menu item cannot be null");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.item = item;
        this.quantity = quantity;
        this.specialInstructions = "";
    }
    
    public OrderItem(MenuItem item, int quantity, String specialInstructions) {
        this(item, quantity);
        this.specialInstructions = specialInstructions != null ? specialInstructions : "";
    }

    // SECTION: Getters (ENCAPSULATION)
    public MenuItem getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }
    
    public String getSpecialInstructions() {
        return specialInstructions;
    }

    // SECTION: Setters with validation (ENCAPSULATION)
    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.quantity = quantity;
    }
    
    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions != null ? specialInstructions : "";
    }

    // SECTION: Quantity helpers
    public void increaseQuantity() {
        this.quantity++;
    }

    public void decreaseQuantity() {
        if (this.quantity > 0) {
            this.quantity--;
        }
    }

    // SECTION: Pricing helpers
    public double getTotalPrice() {
        return item.getPrice() * quantity;
    }
    
    public double getItemPrice() {
        return item.getPrice();
    }
    
    public String getItemName() {
        return item.getName();
    }
    
    public String getItemCategory() {
        return item.getCategory();
    }

    // SECTION: Object helpers
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(item.getName()).append(" x").append(quantity);
        if (!specialInstructions.isEmpty()) {
            sb.append(" (").append(specialInstructions).append(")");
        }
        sb.append(" - ").append(String.format("%.2f", getTotalPrice())).append("৳");
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        OrderItem orderItem = (OrderItem) obj;
        return item.equals(orderItem.item) && 
               specialInstructions.equals(orderItem.specialInstructions);
    }
    
    @Override
    public int hashCode() {
        return item.hashCode() + specialInstructions.hashCode();
    }
}
