package Entity;

import java.util.*;
import java.util.stream.Collectors;

/**
 * OrderServiceImpl class - implements OrderService interface
 * Demonstrates ABSTRACTION, POLYMORPHISM, and EXCEPTION HANDLING
 */
public class OrderServiceImpl implements OrderService {
    // VARIABLE LEGEND: allOrders, menuManager
    private List<Order> allOrders;
    private MenuManager menuManager;
    
    // SECTION: Constructors (ENCAPSULATION)
    public OrderServiceImpl() {
        this.allOrders = new ArrayList<>();
        this.menuManager = new MenuManager();
    }
    
    public OrderServiceImpl(MenuManager menuManager) {
        this.allOrders = new ArrayList<>();
        this.menuManager = menuManager != null ? menuManager : new MenuManager();
    }
    
    // SECTION: Service methods (ABSTRACTION)
    @Override
    public Order createOrder(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        Order order = new Order(customer);
        allOrders.add(order);
        return order;
    }
    
    @Override
    public void addItemToOrder(Order order, MenuItem item, int quantity) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        if (item == null) {
            throw new IllegalArgumentException("Menu item cannot be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (!item.isAvailable()) {
            throw new IllegalStateException("Item is not available: " + item.getName());
        }
        
        order.addItem(item, quantity);
    }
    
    @Override
    public boolean removeItemFromOrder(Order order, String itemName) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        // UI uses last-added removal; keep behavior simple here
        return order.removeLastUnit();
    }
    
    @Override
    public void applyDiscount(Order order, double discountPercent) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Discount percent must be between 0 and 100");
        }
        
        order.setDiscountPercent(discountPercent);
    }
    
    @Override
    public void confirmOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        if (order.isEmpty()) {
            throw new IllegalStateException("Cannot confirm empty order");
        }
        
        order.confirmOrder();
    }
    
    @Override
    public void cancelOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        
        order.cancelOrder();
    }
    
    @Override
    public List<Order> getOrderHistory(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        
        return allOrders.stream()
                .filter(order -> customer.equals(order.getCustomer()))
                .collect(Collectors.toList());
    }
    
    @Override
    public double calculateTotalRevenue(List<Order> orders) {
        if (orders == null) {
            throw new IllegalArgumentException("Orders list cannot be null");
        }
        
        return orders.stream()
                .filter(order -> "confirmed".equals(order.getOrderStatus()) || 
                               "completed".equals(order.getOrderStatus()))
                .mapToDouble(Order::getTotalPrice)
                .sum();
    }
    
    @Override
    public List<String> getMostPopularItems(List<Order> orders, int limit) {
        if (orders == null) {
            throw new IllegalArgumentException("Orders list cannot be null");
        }
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit must be positive");
        }
        
        Map<String, Integer> itemCounts = new HashMap<>();
        
        for (Order order : orders) {
            if ("confirmed".equals(order.getOrderStatus()) || 
                "completed".equals(order.getOrderStatus())) {
                for (OrderItem orderItem : order.getItems()) {
                    String itemName = orderItem.getItemName();
                    itemCounts.put(itemName, 
                        itemCounts.getOrDefault(itemName, 0) + orderItem.getQuantity());
                }
            }
        }
        
        return itemCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
    
    // SECTION: Additional business logic
    public List<Order> getAllOrders() {
        return new ArrayList<>(allOrders);
    }
    
    public void setAllOrders(List<Order> orders) {
        this.allOrders = orders != null ? new ArrayList<>(orders) : new ArrayList<>();
    }
    
    public MenuManager getMenuManager() {
        return menuManager;
    }
    
    public void setMenuManager(MenuManager menuManager) {
        this.menuManager = menuManager;
    }
    
    public List<Order> getOrdersByStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }
        
        return allOrders.stream()
                .filter(order -> status.equals(order.getOrderStatus()))
                .collect(Collectors.toList());
    }
    
    public List<Order> getOrdersByDateRange(java.time.LocalDate startDate, java.time.LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start and end dates cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        
        return allOrders.stream()
                .filter(order -> {
                    java.time.LocalDate orderDate = order.getOrderDate().toLocalDate();
                    return !orderDate.isBefore(startDate) && !orderDate.isAfter(endDate);
                })
                .collect(Collectors.toList());
    }
    
    public Map<String, Object> getCustomerStatistics(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        
        List<Order> customerOrders = getOrderHistory(customer);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalOrders", customerOrders.size());
        stats.put("totalSpent", customer.getTotalSpent());
        stats.put("averageOrderValue", customerOrders.isEmpty() ? 0.0 : 
            customerOrders.stream().mapToDouble(Order::getTotalPrice).average().orElse(0.0));
        stats.put("customerTier", customer.getCustomerTier());
        stats.put("discountRate", customer.getDiscountRate());
        
        return stats;
    }
}
