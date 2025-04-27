package ration.utils;

import ration.model.*;
import ration.service.*;

import java.sql.*;
import java.util.List;

public class DatabaseHandler {

    public static void loadAll(CardService cardService, BenSrv benService, InventoryService inventoryService, List<Tx> txList) {
        loadCards(cardService);
        loadBeneficiaries(benService, cardService);
        loadItems(inventoryService);
        loadTransactions(txList);
    }

    private static void loadCards(CardService cardService) {
        String sql = "SELECT * FROM cards";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("id");
                String type = rs.getString("type");
                int count = rs.getInt("count");

                Card card = new Card(type);   // Create normally
                card.setType(type);            // Set again to be safe
                card.setCount(count);          // Custom setter (you'll add)
                card.setId(id);                // Custom setter (you'll add)

                cardService.addCard(card);      // You'll add addCard(Card) method
            }

            System.out.println("✅ Cards loaded.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void loadBeneficiaries(BenSrv benService, CardService cardService) {
        String sql = "SELECT * FROM beneficiaries";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String address = rs.getString("address");
                String phone = rs.getString("phone");
                int age = rs.getInt("age");
                boolean isHead = rs.getBoolean("is_head");
                String cardId = rs.getString("card_id");

                Ben ben = new Ben(name, address, phone, age, isHead);
                ben.setId(id);            // Custom setter
                ben.setCardId(cardId);     // Already existing

                benService.add(ben);
                if (cardId != null && !cardId.isEmpty()) {
                    cardService.addMember(cardId, ben);
                }
            }

            System.out.println("✅ Beneficiaries loaded.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void loadItems(InventoryService inventoryService) {
        String sql = "SELECT * FROM items";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                double quantity = rs.getDouble("quantity");
                String unit = rs.getString("unit");

                Item item = new Item(name, price, quantity, unit);
                item.setId(id);           // Custom setter

                inventoryService.add(item);
            }

            System.out.println("✅ Items loaded.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void loadTransactions(List<Tx> txList) {
        String sql = "SELECT * FROM transactions";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("id");
                String cardId = rs.getString("card_id");
                Timestamp date = rs.getTimestamp("date");
                double total = rs.getDouble("total");

                Tx tx = new Tx(cardId);
                tx.setId(id);            // Custom setter
                tx.setDate(date.toLocalDateTime()); // You can add setDate(LocalDateTime) method
                tx.setTotal(total);      // Custom setter

                // (Optional later) load tx_items separately

                txList.add(tx);
            }

            System.out.println("✅ Transactions loaded.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
