package Entity;

/**
 * Drinks class - represents beverage items
 * Demonstrates INHERITANCE from MenuItem and POLYMORPHISM
 */
public class Drinks extends MenuItem {
    private static final long serialVersionUID = 1L;
    // VARIABLE LEGEND: temperature, size
    private String temperature; // hot, cold, room temperature
    private int size; // size in ml
    
    // SECTION: Constructors (INHERITANCE)
    public Drinks(String name, double price) {
        super(name, price);
        this.temperature = "cold";
        this.size = 250; // default size
    }
    
    public Drinks(String name, double price, String description) {
        super(name, price, description);
        this.temperature = "cold";
        this.size = 250;
    }
    
    public Drinks(String name, double price, String description, String temperature, int size) {
        super(name, price, description);
        this.temperature = temperature;
        this.size = size;
    }
    
    // SECTION: Getters & Setters (ENCAPSULATION)
    public String getTemperature() {
        return temperature;
    }
    
    public void setTemperature(String temperature) {
        if (temperature == null || (!temperature.equals("hot") && !temperature.equals("cold") && !temperature.equals("room"))) {
            throw new IllegalArgumentException("Temperature must be 'hot', 'cold', or 'room'");
        }
        this.temperature = temperature;
    }
    
    public int getSize() {
        return size;
    }
    
    public void setSize(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be positive");
        }
        this.size = size;
    }
    
    // SECTION: Polymorphism overrides
    @Override
    public String getCategory() {
        return "Drinks";
    }
    
    @Override
    public double calculateDiscount(double discountPercent) {
        // Drinks get 10% extra discount during happy hour
        double baseDiscount = getPrice() * (discountPercent / 100);
        return baseDiscount * 1.1; // 10% extra discount
    }
    
    // SECTION: Object helpers
    @Override
    public String toString() {
        return String.format("%s (%s, %dml) - %.2f৳", 
                           getName(), temperature, size, getPrice());
    }
    
    // Method specific to drinks
    public String getServingInfo() {
        return String.format("Served %s in %dml", temperature, size);
    }
}
