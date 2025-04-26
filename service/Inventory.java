package ration.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ration.model.Item;

public class Inventory implements InventoryService {
    
    private Map<String, Item> stock;
    
    public Inventory() {
        this.stock = new HashMap<>();
        setupItems();
    }
    
    @Override
    public boolean add(Item item) {
        if (item == null) {
            return false;
        }
        
        for (Item existing : stock.values()) {
            if (existing.getName().equalsIgnoreCase(item.getName())) {
                System.out.println("Error: Item with name '" + item.getName() + "' already exists.");
                return false;
            }
        }
        
        stock.put(item.getId(), item);  
        return true;
    }
    
    @Override
    public Item add(String name, double price, double qty, String unit) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Error: Item name cannot be empty.");
            return null;
        }
        
        for (Item item : stock.values()) {
            if (item.getName().equalsIgnoreCase(name)) {
                System.out.println("Error: Item with name '" + name + "' already exists.");
                return null;
            }
        }
        
        Item newItem = new Item(name, price, qty, unit);
        stock.put(newItem.getId(), newItem);  
        
        return newItem;
    }
    
    @Override
    public boolean update(String id, String name, double price, double qty, String unit) {
        Item item = getItem(id);
        if (item == null) {
            System.out.println("Error: Item not found: " + id);
            return false;
        }
        
        if (name != null && !name.trim().isEmpty()) {
            item.setName(name);
        }
        
        if (price >= 0) {  // Fixed: allow zero prices
            item.setPrice(price);
        }
        
        if (qty >= 0) {
            item.setQuantity(qty);
        }
        
        if (unit != null && !unit.trim().isEmpty()) {
            item.setUnit(unit);
        }
        
        return true;
    }
    
    @Override
    public boolean updateQty(String id, double qty) {
        Item item = getItem(id);
        if (item == null) {
            System.out.println("Error: Item not found: " + id);
            return false;
        }
        
        item.update(qty);  
        return true;
    }
    
    @Override
    public Item getItem(String id) {
        return stock.get(id);
    }
    
    @Override
    public Item findByName(String name) {
        if (name == null) {
            return null;
        }
        
        for (Item item : stock.values()) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        
        return null;
    }
    
    @Override
    public List<Item> getAll() {
        return new ArrayList<>(stock.values());
    }
    
    @Override
    public boolean distribute(String id, double qty) {
        Item item = getItem(id);
        if (item == null) {
            System.out.println("Error: Item not found: " + id);
            return false;
        }
        
        if (!item.hasEnough(qty)) {  
            System.out.println("Error: Not enough " + item.getName() + " in stock");
            System.out.println("Requested: " + qty + " " + item.getUnit() + 
                              ", Available: " + item.getQuantity() + " " + item.getUnit());
            return false;
        }
        
        return item.reduce(qty);  
    }
    
    @Override
    public void showAll() {
        if (stock.isEmpty()) {
            System.out.println("No items in stock.");
            return;
        }
        
        System.out.println("\n----- STOCK ITEMS -----");
        for (Item item : stock.values()) {
            System.out.println(item.toString());
        }
    }
    
    private void setupItems() {
        add("Rice", 3.0, 1000.0, "kg");
        add("Wheat", 2.0, 1000.0, "kg");
        add("Sugar", 15.0, 500.0, "kg");
        add("Salt", 5.0, 200.0, "kg");
        add("Kerosene", 20.0, 500.0, "liter");
    }
}