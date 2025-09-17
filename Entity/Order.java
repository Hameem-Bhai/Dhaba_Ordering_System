package Entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Order class - represents a customer's order
 * Demonstrates ENCAPSULATION, FILE I/O, and EXCEPTION HANDLING
 */
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final Map<String, OrderItem> items = new LinkedHashMap<>();
    private final List<String> history = new ArrayList<>();
    private Customer customer;
    private LocalDateTime orderDate;
    private String orderStatus; // pending, confirmed, preparing, ready, completed, cancelled
    private double discountPercent;
    private boolean discountLocked; // apply only after confirm
    private String specialInstructions;
    
    // Constructor with ENCAPSULATION
    public Order() {
        this.customer = null;
        this.orderDate = LocalDateTime.now();
        this.orderStatus = "pending";
        this.discountPercent = 0.0;
        this.discountLocked = false;
        this.specialInstructions = "";
    }
    
    public Order(Customer customer) {
        this();
        this.customer = customer;
    }
    
    // Getter methods (ENCAPSULATION)
    public Customer getCustomer() {
        return customer;
    }
    
    public LocalDateTime getOrderDate() {
        return orderDate;
    }
    
    public String getOrderStatus() {
        return orderStatus;
    }
    
    public double getDiscountPercent() {
        return discountPercent;
    }
    public boolean isDiscountLocked() {
        return discountLocked;
    }
    
    public String getSpecialInstructions() {
        return specialInstructions;
    }
    
    // Setter methods with validation (ENCAPSULATION)
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public void setOrderStatus(String orderStatus) {
        if (orderStatus == null || orderStatus.trim().isEmpty()) {
            throw new IllegalArgumentException("Order status cannot be null or empty");
        }
        this.orderStatus = orderStatus.trim();
    }
    
    public void setDiscountPercent(double discountPercent) {
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Discount percent must be between 0 and 100");
        }
        // Only allow setting discount before confirmation lock
        this.discountPercent = discountPercent;
    }

    public void lockDiscount() {
        this.discountLocked = true;
    }
    
    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions != null ? specialInstructions : "";
    }
    
    // Business logic methods
    public void addItem(MenuItem item) {
        addItem(item, 1);
    }

    public void addItem(MenuItem item, int quantity) {
        if (item == null) {
            throw new IllegalArgumentException("Menu item cannot be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (!item.isAvailable()) {
            throw new IllegalStateException("Item is not available: " + item.getName());
        }
        
        OrderItem existing = items.get(item.getName());
        if (existing == null) {
            existing = new OrderItem(item, 0);
            items.put(item.getName(), existing);
        }
        existing.setQuantity(existing.getQuantity() + quantity);
        for (int i = 0; i < quantity; i++) {
            history.add(item.getName());
        }
    }

    public boolean removeLastUnit() {
        if (history.isEmpty()) return false;
        String last = history.remove(history.size() - 1);
        OrderItem oi = items.get(last);
        if (oi != null) {
            int q = oi.getQuantity() - 1;
            if (q <= 0) {
                items.remove(last);
            } else {
                oi.setQuantity(q);
            }
        }
        return true;
    }

    public void clear() {
        items.clear();
        history.clear();
        discountPercent = 0.0;
        specialInstructions = "";
    }

    public List<OrderItem> getItems() {
        return new ArrayList<>(items.values());
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public double getSubtotal() {
        double sum = 0.0;
        for (OrderItem oi : items.values()) {
            sum += oi.getTotalPrice();
        }
        return sum;
    }
    
    public double getDiscountAmount() {
        double subtotal = getSubtotal();
        double tierDisc = (customer != null) ? subtotal * customer.getDiscountRate() : 0.0;
        double manualDisc = discountLocked ? subtotal * (discountPercent / 100) : 0.0; // only after confirm
        return Math.max(tierDisc, manualDisc);
    }

    public double getTotalPrice() {
        return getSubtotal() - getDiscountAmount();
    }
    
    public int getTotalItems() {
        return items.values().stream().mapToInt(OrderItem::getQuantity).sum();
    }

    public String getBill() {
        StringBuilder bill = new StringBuilder();
        bill.append("---Hameem's Dhaba---\n");
        bill.append("---Dhanmondi, Dhaka---\n");
        bill.append("Date: ").append(orderDate.toLocalDate()).append("\n");
        bill.append("Time: ").append(orderDate.toLocalTime()).append("\n");
        bill.append("Status: ").append(orderStatus).append("\n");
        
        if (customer != null) {
            bill.append("Customer: ").append(customer.getName()).append("\n");
            bill.append("Tier: ").append(customer.getCustomerTier()).append("\n");
        }
        
        bill.append("=====================\n");
        bill.append("Your order:\n");
        
        for (OrderItem oi : items.values()) {
            MenuItem mi = oi.getItem();
            bill.append(mi.getName());
            // Append attribute details for better visibility
            String details = formatItemDetails(mi);
            if (!details.isEmpty()) {
                bill.append(" [").append(details).append("]");
            }
            bill.append(" x").append(oi.getQuantity())
                .append(" - ")
                .append(String.format("%.2f", oi.getTotalPrice()))
                .append("৳\n");
        }
        
        bill.append("=====================\n");
        bill.append("Subtotal: ").append(String.format("%.2f", getSubtotal())).append("৳\n");
        
        if (getDiscountAmount() > 0) {
            bill.append("Discount: -").append(String.format("%.2f", getDiscountAmount())).append("৳\n");
        }
        
        bill.append("Total: ").append(String.format("%.2f", getTotalPrice())).append("৳\n");
        
        if (!specialInstructions.isEmpty()) {
            bill.append("Special Instructions: ").append(specialInstructions).append("\n");
        }
        
        return bill.toString();
    }
    
    public void confirmOrder() {
        if (isEmpty()) {
            throw new IllegalStateException("Cannot confirm empty order");
        }
        setOrderStatus("confirmed");
        lockDiscount(); // apply manual discount only after confirmation
        if (customer != null) {
            customer.updateOrderStats(getTotalPrice());
        }
    }
    
    public void cancelOrder() {
        setOrderStatus("cancelled");
    }

    // Helper to format attribute details per item for receipts
    private String formatItemDetails(MenuItem item) {
        try {
            if (item instanceof Food) {
                Food f = (Food) item;
                String veg = f.isVegetarian() ? "Veg" : "Non-Veg";
                return f.getCuisine() + ", " + f.getSpiceLevel() + ", " + veg;
            } else if (item instanceof Drinks) {
                Drinks d = (Drinks) item;
                return d.getServingInfo();
            } else {
                return item.getCategory();
            }
        } catch (Exception e) {
            return item.getCategory();
        }
    }
}
