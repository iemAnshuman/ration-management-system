package ration.utils;

import ration.model.*;
import ration.service.*;
import ration.utils.RationExceptions;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileHandler {
    private static final String DATA_DIR = "data";
    
    static {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
        } catch (IOException e) {
            System.err.println("Error creating data directory: " + e.getMessage());
            throw new RuntimeException("Cannot create data directory", e);
        }
    }
    
    public static void exportToCSV(CardService cardService, BenSrv benService,
                                InventoryService invService, List<Tx> transactions) 
                                throws RationExceptions.IOException {
        try {
            exportItems(invService);
            exportBeneficiaries(benService);
            exportCards(cardService);
            exportTransactions(transactions);
            
            System.out.println("Data exported to " + DATA_DIR + " directory");
        } catch (IOException e) {
            throw new RationExceptions.IOException("Export failed: " + e.getMessage(), e);
        }
    }
    
    public static void importFromCSV(CardService cardService, BenSrv benService,
                                   InventoryService invService, List<Tx> transactions) 
                                   throws RationExceptions.IOException {
        try {
            importItems(invService);
            importBeneficiaries(benService);
            importCards(cardService, benService);
            importTransactions(transactions, cardService, invService);
            
            System.out.println("Data imported from " + DATA_DIR + " directory");
        } catch (IOException e) {
            throw new RationExceptions.IOException("Import failed: " + e.getMessage(), e);
        }
    }
    
    private static void exportItems(InventoryService invService) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("id,name,price,quantity,unit");
        
        for (Item item : invService.getAll()) {
            lines.add(item.getId() + "," + 
                    escapeCsv(item.getName()) + "," + 
                    item.getPrice() + "," + 
                    item.getQuantity() + "," + 
                    escapeCsv(item.getUnit()));
        }
        
        writeFile("items.csv", lines);
    }
    
    private static void exportBeneficiaries(BenSrv benService) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("id,name,address,phone,age,isHead,cardId");
        
        for (Ben ben : benService.getAll()) {
            String cardId = ben.getCardId() != null ? ben.getCardId() : "";
            lines.add(ben.getId() + "," + 
                    escapeCsv(ben.getName()) + "," + 
                    escapeCsv(ben.getAddr()) + "," + 
                    escapeCsv(ben.getPhone()) + "," + 
                    ben.getAge() + "," + 
                    (ben.isHead() ? "1" : "0") + "," + 
                    cardId);
        }
        
        writeFile("beneficiaries.csv", lines);
    }
    
    private static void exportCards(CardService cardService) throws IOException {
        
        
        
        
        
        List<String> lines = new ArrayList<>();
        lines.add("id,type,count");
        
        
        
        
        writeFile("cards.csv", lines);
    }
    
    private static void exportTransactions(List<Tx> transactions) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("id,cardId,date,total");
        
        List<String> itemLines = new ArrayList<>();
        itemLines.add("txId,itemId,itemName,quantity,price,unit");
        
        for (Tx tx : transactions) {
            lines.add(tx.getId() + "," + 
                    tx.getCard() + "," + 
                    tx.getDateString() + "," + 
                    tx.getTotal());
            
            for (Tx.ItemDetail item : tx.getItems()) {
                itemLines.add(tx.getId() + "," + 
                            item.getItemId() + "," + 
                            escapeCsv(item.getName()) + "," + 
                            item.getQty() + "," + 
                            item.getPrice() + "," + 
                            escapeCsv(item.getUnit()));
            }
        }
        
        writeFile("transactions.csv", lines);
        writeFile("tx_items.csv", itemLines);
    }
    
    private static void importItems(InventoryService invService) throws IOException {
        if (!fileExists("items.csv")) {
            return;
        }
        
        List<String> lines = readFile("items.csv");
        boolean header = true;
        
        for (String line : lines) {
            if (header) {
                header = false;
                continue;
            }
            
            try {
                String[] parts = parseCsvLine(line);
                if (parts.length >= 5) {
                    String name = parts[1];
                    double price = Double.parseDouble(parts[2]);
                    double qty = Double.parseDouble(parts[3]);
                    String unit = parts[4];
                    
                    invService.add(name, price, qty, unit);
                }
            } catch (Exception e) {
                System.err.println("Error parsing item: " + line);
            }
        }
    }
    
    private static void importBeneficiaries(BenSrv benService) throws IOException {
        if (!fileExists("beneficiaries.csv")) {
            return;
        }
        
        List<String> lines = readFile("beneficiaries.csv");
        boolean header = true;
        
        for (String line : lines) {
            if (header) {
                header = false;
                continue;
            }
            
            try {
                String[] parts = parseCsvLine(line);
                if (parts.length >= 6) {
                    String name = parts[1];
                    String addr = parts[2];
                    String phone = parts[3];
                    int age = Integer.parseInt(parts[4]);
                    boolean isHead = "1".equals(parts[5]);
                    
                    benService.add(name, addr, phone, age, isHead);
                }
            } catch (Exception e) {
                System.err.println("Error parsing beneficiary: " + line);
            }
        }
    }
    
    private static void importCards(CardService cardService, BenSrv benService) throws IOException {
        if (!fileExists("cards.csv")) {
            return;
        }
        
        
        
        List<String> lines = readFile("cards.csv");
        boolean header = true;
        
        for (String line : lines) {
            if (header) {
                header = false;
                continue;
            }
            
            try {
                String[] parts = parseCsvLine(line);
                if (parts.length >= 2) {
                    String type = parts[1];
                    cardService.newCard(type);
                }
            } catch (Exception e) {
                System.err.println("Error parsing card: " + line);
            }
        }
    }
    
    private static void importTransactions(List<Tx> transactions, CardService cardService, 
                                         InventoryService invService) throws IOException {
        if (!fileExists("transactions.csv") || !fileExists("tx_items.csv")) {
            return;
        }
        
        
    }
    
    private static String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        
        return value;
    }
    
    private static String[] parseCsvLine(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '\"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '\"') {
                    sb.append('\"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                tokens.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        
        tokens.add(sb.toString());
        return tokens.toArray(new String[0]);
    }
    
    private static void writeFile(String filename, List<String> lines) throws IOException {
        Path path = Paths.get(DATA_DIR, filename);
        Files.write(path, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
    
    private static List<String> readFile(String filename) throws IOException {
        Path path = Paths.get(DATA_DIR, filename);
        
        if (!Files.exists(path)) {
            return new ArrayList<>();
        }
        
        return Files.readAllLines(path);
    }
    
    private static boolean fileExists(String filename) {
        return Files.exists(Paths.get(DATA_DIR, filename));
    }
}