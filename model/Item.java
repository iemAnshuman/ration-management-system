package ration.model;

public class Item {
    private String id;
    private String name;
    private double price;
    private double qty;
    private String unit;
    
    private static int count = 1;
    
    public Item() {
        id = genId();
    }
    
    public Item(String name, double price, double qty, String unit) {
        id = genId();
        this.name = name;
        this.price = price;
        this.qty = qty;
        this.unit = unit;
    }
    
    public void update(double add) {
        qty += add;
    }
    
    public void update(double newQty, boolean replace) {
        if (replace) {
            qty = newQty;
        } else {
            update(newQty);
        }
    }
    
    public boolean hasEnough(double need) {
        return qty >= need;
    }
    
    public boolean reduce(double cut) {
        if (hasEnough(cut)) {
            qty -= cut;
            return true;
        }
        return false;
    }
    
    public void display() {
        System.out.println(id + ": " + name);
        System.out.println("Price: Rs. " + price + " per " + unit);
        System.out.println("Stock: " + qty + " " + unit);
    }
    
    private static String genId() {
        return "I" + String.format("%03d", count++);
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String n) {
        name = n;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double p) {
        price = p;
    }
    
    public double getQuantity() {
        return qty;
    }
    
    public void setQuantity(double q) {
        qty = q;
    }
    
    public String getUnit() {
        return unit;
    }
    
    public void setUnit(String u) {
        unit = u;
    }

    public void setId(String id) { this.id = id; }
    
    @Override
    public String toString() {
        return id + ": " + name + " - Rs." + price + "/" + unit + " (" + qty + " " + unit + ")";
    }
}