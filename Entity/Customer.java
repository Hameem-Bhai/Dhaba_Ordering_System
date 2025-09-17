package Entity;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Customer implements Serializable 
{
    private static final long serialVersionUID = 1L;
    
    // VARIABLE LEGEND: name, age, gender, phoneNumber, email, registrationDate, totalOrders, totalSpent
    private String name; //name
    private int age; //age
    private String gender; //gender
    private String phoneNumber; //phoneNumber
    private String email; //email
    private LocalDateTime registrationDate; //registrationDate
    private int totalOrders; //totalOrders
    private double totalSpent; //totalSpent
    
    public Customer() 
    {
        this.name = "";
        this.age = 0;
        this.gender = "Unknown";
        this.phoneNumber = "";
        this.email = "";
        this.registrationDate = LocalDateTime.now();
        this.totalOrders = 0;
        this.totalSpent = 0.0;
    }
    
    public Customer(String s1, int s2, String s3)
    {
        this();
        setName(s1);
        setAge(s2);
        setGender(s3);
    }
    
    public Customer(String s1, int s2, String s3, String s4, String s5)
    {
        this(s1, s2, s3);
        setPhoneNumber(s4);
        setEmail(s5);
    }
    
    public String getName() {
        return name;
    }
    
    public int getAge() {
        return age;
    }
    
    public String getGender() {
        return gender;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public String getEmail() {
        return email;
    }
    
    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }
    
    public int getTotalOrders() {
        return totalOrders;
    }
    
    public double getTotalSpent() {
        return totalSpent;
    }
    
    public void setName(String s1) {
        if (s1 == null || s1.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = s1.trim();
    }
    
    public void setAge(int s2) {
        if (s2 < 0 || s2 > 150) {
            throw new IllegalArgumentException("Age must be between 0 and 150");
        }
        this.age = s2;
    }
    
    public void setGender(String s3) {
        if (s3 == null || s3.trim().isEmpty()) {
            throw new IllegalArgumentException("Gender cannot be null or empty");
        }
        this.gender = s3.trim();
    }
    
    public void setPhoneNumber(String s4) {
        if (s4 != null && !s4.matches("\\d{10,15}")) {
            throw new IllegalArgumentException("Phone number must be 11 digits");
        }
        this.phoneNumber = s4 != null ? s4 : "";
    }
    
    public void setEmail(String s5) {
        if (s5 != null && !s5.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = s5 != null ? s5 : "";
    }
    
    public void updateOrderStats(double orderAmount) {
        this.totalOrders++;
        this.totalSpent += orderAmount;
    }
    
    public String getCustomerTier() {
        if (totalSpent >= 5000) return "Gold";
        if (totalSpent >= 2000) return "Silver";
        if (totalSpent >= 500) return "Bronze";
        return "Regular";
    }
    
    public double getDiscountRate() {
        String tier = getCustomerTier();
        switch (tier) {
            case "Gold": return 0.15; // 15% discount
            case "Silver": return 0.10; // 10% discount
            case "Bronze": return 0.05; // 5% discount
            default: return 0.0; // No discount
        }
    }
    
    @Override
    public String toString() {
        return String.format("Customer: %s, Age: %d, Gender: %s, Tier: %s", 
                           name, age, gender, getCustomerTier());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Customer customer = (Customer) obj;
        return name.equals(customer.name) && 
               phoneNumber.equals(customer.phoneNumber);
    }
    
    @Override
    public int hashCode() {
        return name.hashCode() + phoneNumber.hashCode();
    }
}
