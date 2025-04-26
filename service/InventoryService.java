package ration.service;
import java.util.List;
import ration.model.Item;

public interface InventoryService {
    boolean add(Item item);
    
    Item add(String name, double price, double qty, String unit);
    
    boolean update(String id, String name, double price, double qty, String unit);
    
    boolean updateQty(String id, double qty);
    
    Item getItem(String id);
    
    Item findByName(String name);
    
    List<Item> getAll();
    
    boolean distribute(String id, double qty);
    
    void showAll();
}