package Entity;

/**
 * Food class - represents food items
 * Demonstrates INHERITANCE from MenuItem and POLYMORPHISM
 */
public class Food extends MenuItem {
    private static final long serialVersionUID = 1L;
    // VARIABLE LEGEND: cuisine, spiceLevel, isVegetarian
    private String cuisine; // Bangladeshi, Italian, Chinese, etc.
    private String spiceLevel; // mild, medium, hot
    private boolean isVegetarian;
    
    // SECTION: Constructors (INHERITANCE)
    public Food(String name, double price) {
        super(name, price);
        this.cuisine = "Bangladeshi";
        this.spiceLevel = "medium";
        this.isVegetarian = false;
    }
    
    public Food(String name, double price, String description) {
        super(name, price, description);
        this.cuisine = "Bangladeshi";
        this.spiceLevel = "medium";
        this.isVegetarian = false;
    }
    
    public Food(String name, double price, String description, String cuisine, String spiceLevel, boolean isVegetarian) {
        super(name, price, description);
        this.cuisine = cuisine;
        this.spiceLevel = spiceLevel;
        this.isVegetarian = isVegetarian;
    }
    
    // SECTION: Getters & Setters (ENCAPSULATION)
    public String getCuisine() {
        return cuisine;
    }
    
    public void setCuisine(String cuisine) {
        if (cuisine == null || cuisine.trim().isEmpty()) {
            throw new IllegalArgumentException("Cuisine cannot be null or empty");
        }
        this.cuisine = cuisine;
    }
    
    public String getSpiceLevel() {
        return spiceLevel;
    }
    
    public void setSpiceLevel(String spiceLevel) {
        if (spiceLevel == null || (!spiceLevel.equals("mild") && !spiceLevel.equals("medium") && !spiceLevel.equals("hot"))) {
            throw new IllegalArgumentException("Spice level must be 'mild', 'medium', or 'hot'");
        }
        this.spiceLevel = spiceLevel;
    }
    
    public boolean isVegetarian() {
        return isVegetarian;
    }
    
    public void setVegetarian(boolean vegetarian) {
        this.isVegetarian = vegetarian;
    }
    
    // SECTION: Polymorphism overrides
    @Override
    public String getCategory() {
        return "Food";
    }
    
    @Override
    public double calculateDiscount(double discountPercent) {
        // Food items get standard discount
        return getPrice() * (discountPercent / 100);
    }
    
    // SECTION: Object helpers
    @Override
    public String toString() {
        String vegStatus = isVegetarian ? "Veg" : "Non-Veg";
        return String.format("%s (%s, %s, %s) - %.2f৳", 
                           getName(), cuisine, spiceLevel, vegStatus, getPrice());
    }
    
    // Method specific to food
    public String getCookingInfo() {
        return String.format("%s cuisine, %s spice level", cuisine, spiceLevel);
    }
}
