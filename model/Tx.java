package ration.model;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Tx {
    private String id;
    private String card;
    private LocalDateTime date;
    private double total;
    private List<ItemDetail> items; 

    
    public static int count = 10001;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Tx(String id, String card, LocalDateTime date, double total) {
        this.id = id;
        this.card = card;
        this.date = date;
        this.items = new ArrayList<>(); 
        this.total = total; 
        
        try {
            int numPart = Integer.parseInt(id.substring(2)); 
             if (numPart >= count) {
                 count = numPart + 1;
             }
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
             System.err.println("Warning: Could not parse transaction ID for counter: " + id);
        }
    }

    
    public Tx(String card) {
        this.id = genId(); 
        this.card = card;
        this.date = LocalDateTime.now();
        this.items = new ArrayList<>();
        this.total = 0.0;
    }

    
    public void addItem(String itemId, String itemName, double qty, double price, String unit) {
        ItemDetail itemDetail = new ItemDetail(itemId, itemName, qty, price, unit);
        items.add(itemDetail);
        recalculateTotal(); 
    }

     private void recalculateTotal() {
         this.total = items.stream()
                           .mapToDouble(item -> item.qty * item.price)
                           .sum();
     }

    public void display() {
        System.out.println("\n----- TX INFO -----");
        System.out.println("ID: " + id);
        System.out.println("Card: " + card);
        System.out.println("Date: " + getDateString()); 

        System.out.println("\nItems:");
         if (items.isEmpty()) {
             System.out.println("No items in this transaction.");
         } else {
             for (ItemDetail i : items) {
                 System.out.println("- " + i.toString());
             }
         }

        System.out.println("\nTotal: Rs. " + String.format("%.2f", total));
    }

    public String getDateString() {
        return date.format(DATE_FORMATTER);
    }

    
    private static String genId() {
        return "TX" + count++;
    }

    
    public static class ItemDetail { 
        private String itemId; 
        private String name;   
        private double qty;
        private double price;  
        private String unit;

        
        public ItemDetail(String itemId, String name, double qty, double price, String unit) {
            this.itemId = itemId;
            this.name = name;
            this.qty = qty;
            this.price = price;
            this.unit = unit;
        }

         private ItemDetail(String data) throws IllegalArgumentException {
             String[] parts = data.split(":"); 
             if (parts.length < 5) {
                 throw new IllegalArgumentException("Invalid Tx ItemDetail format: " + data);
             }
             try {
                 this.itemId = parts[0].trim();
                 this.name = parts[1].trim();
                 this.qty = Double.parseDouble(parts[2].trim());
                 this.price = Double.parseDouble(parts[3].trim());
                 this.unit = parts[4].trim();
             } catch (NumberFormatException | IndexOutOfBoundsException e) {
                 throw new IllegalArgumentException("Error parsing Tx ItemDetail data: " + data, e);
             }
         }


        @Override
        public String toString() {
            // Format for display
            return name + " (" + itemId + "): " + qty + " " + unit + " @ Rs." + String.format("%.2f", price) + " = Rs." +
                   String.format("%.2f", (qty * price));
        }

     
        public String toDataString() {
            return itemId + ":" + name + ":" + qty + ":" + price + ":" + unit;
        }

         
         public static ItemDetail fromDataString(String data) throws IllegalArgumentException {
             return new ItemDetail(data);
         }

        public String getItemId() { return itemId; }
        public String getName() { return name; }
        public double getQty() { return qty; }
        public double getPrice() { return price; }
        public String getUnit() { return unit; }
  
    }


    public String getId() { return id; }
    public String getCard() { return card; }
    public LocalDateTime getDateTime() { return date; }
    public double getTotal() { return total; }
    public List<ItemDetail> getItems() { return new ArrayList<>(items); } 
 

    public String toDataString() {
        String itemDetailsString = items.stream()
                                        .map(ItemDetail::toDataString)
                                        .collect(Collectors.joining(";"));
         if (itemDetailsString.isEmpty()) {
             itemDetailsString = "NONE";
         }
        return id + "|" + card + "|" + getDateString() + "|" + itemDetailsString;
    }

     public static Tx fromDataString(String data) throws IllegalArgumentException {
         String[] parts = data.split("\\|");
         if (parts.length < 4) { 
             throw new IllegalArgumentException("Invalid data format for Tx: " + data);
         }
         try {
             String id = parts[0].trim();
             String card = parts[1].trim();
             LocalDateTime date = LocalDateTime.parse(parts[2].trim(), DATE_FORMATTER);
             
             
             Tx tx = new Tx(id, card, date, 0.0); 
             return tx;
         } catch (DateTimeParseException | IndexOutOfBoundsException e) {
             throw new IllegalArgumentException("Error parsing Tx data: " + data, e);
         }
     }

     
     public void setItemDetailsFromString(String itemDetailsString) {
         items.clear();
         if (itemDetailsString != null && !itemDetailsString.isEmpty() && !itemDetailsString.equalsIgnoreCase("NONE")) {
             String[] itemDataStrings = itemDetailsString.split(";");
             for (String itemData : itemDataStrings) {
                 try {
                     items.add(ItemDetail.fromDataString(itemData));
                 } catch (IllegalArgumentException e) {
                     System.err.println("Warning: Skipping invalid item detail in Tx " + this.id + ": " + itemData + " (" + e.getMessage() + ")");
                 }
             }
         }
         recalculateTotal(); 
     }

    public void setId(String id) { this.id = id; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public void setTotal(double total) { this.total = total; }
}