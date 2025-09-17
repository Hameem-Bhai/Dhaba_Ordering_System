package Entity;

import java.util.List;

/**
 * OrderService interface - defines contract for order operations
 * Demonstrates ABSTRACTION through interface
 */
public interface OrderService {
    
   
    Order createOrder(Customer customer);
    
    
    void addItemToOrder(Order order, MenuItem item, int quantity);
    
    
    boolean removeItemFromOrder(Order order, String itemName);
    
    
    void applyDiscount(Order order, double discountPercent);
    
    void confirmOrder(Order order);
   
    void cancelOrder(Order order);
    
    List<Order> getOrderHistory(Customer customer);
    
    
    double calculateTotalRevenue(List<Order> orders);
    
    
    List<String> getMostPopularItems(List<Order> orders, int limit);
}
