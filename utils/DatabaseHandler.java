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

    public static void saveCard(Card card) {
        Connection conn = null;
        PreparedStatement pst = null;

        try {
            conn = DatabaseManager.getConnection();
            String sql = "INSERT INTO cards (id, type, count) VALUES (?, ?, ?)";
            pst = conn.prepareStatement(sql);

            pst.setString(1, card.getId());
            pst.setString(2, card.getType());
            pst.setInt(3, card.getCount());

            pst.executeUpdate();
            System.out.println("Card saved to database: " + card.getId());

        } catch (SQLException e) {
            System.err.println("Error saving card: " + e.getMessage());
        } finally {
            try {
                if (pst != null) pst.close();
            } catch (Exception e) {
                System.err.println("Error closing PreparedStatement: " + e.getMessage());
            }
            try {
                if (conn != null) conn.close();
            } catch (Exception e) {
                System.err.println("Error closing Connection: " + e.getMessage());
            }
        }
    }

    public static void saveBeneficiary(Ben ben) {
        Connection conn = null;
        PreparedStatement pst = null;

        try {
            conn = DatabaseManager.getConnection();
            String sql = "INSERT INTO beneficiaries " +
                    "(id, name, address, phone, age, is_head, card_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            pst = conn.prepareStatement(sql);

            pst.setInt(1, ben.getId());
            pst.setString(2, ben.getName());
            pst.setString(3, ben.getAddr());
            pst.setString(4, ben.getPhone());
            pst.setInt(5, ben.getAge());
            pst.setBoolean(6, ben.isHead());
            pst.setString(7, ben.getCardId());

            pst.executeUpdate();
            System.out.println("Beneficiary saved to database: " + ben.getName());

        } catch (SQLException e) {
            System.err.println("Error saving beneficiary: " + e.getMessage());
        } finally {
            try {
                if (pst != null) pst.close();
            } catch (Exception e) {
                System.err.println("Error closing PreparedStatement: " + e.getMessage());
            }
            try {
                if (conn != null) conn.close();
            } catch (Exception e) {
                System.err.println("Error closing Connection: " + e.getMessage());
            }
        }
    }

    public static void updateCard(Card card) {
        Connection conn = null;
        PreparedStatement pst = null;

        try {
            conn = DatabaseManager.getConnection();
            String sql = "UPDATE cards SET type = ?, count = ? WHERE id = ?";
            pst = conn.prepareStatement(sql);

            pst.setString(1, card.getType());
            pst.setInt(2, card.getCount());
            pst.setString(3, card.getId());

            int rows = pst.executeUpdate();
            if (rows > 0) {
                System.out.println("Card updated: " + card.getId());
            } else {
                System.out.println("No card found to update with id: " + card.getId());
            }

        } catch (SQLException e) {
            System.err.println("Error updating card: " + e.getMessage());
        } finally {
            try { if (pst != null) pst.close(); } catch (Exception e) { }
            try { if (conn != null) conn.close(); } catch (Exception e) { }
        }
    }

    public static void deleteCard(String cardId) {
        Connection conn = null;
        PreparedStatement pst = null;

        try {
            conn = DatabaseManager.getConnection();
            String sql = "DELETE FROM cards WHERE id = ?";
            pst = conn.prepareStatement(sql);

            pst.setString(1, cardId);

            int rows = pst.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Card deleted: " + cardId);
            } else {
                System.out.println("⚠ No card found to delete with id: " + cardId);
            }

        } catch (SQLException e) {
            System.err.println("⚠ Error deleting card: " + e.getMessage());
        } finally {
            try { if (pst != null) pst.close(); } catch (Exception e) { }
            try { if (conn != null) conn.close(); } catch (Exception e) { }
        }
    }

    public static void updateBeneficiary(Ben ben) {
        Connection conn = null;
        PreparedStatement pst = null;

        try {
            conn = DatabaseManager.getConnection();
            String sql = "UPDATE beneficiaries SET name = ?, address = ?, phone = ?, age = ?, is_head = ?, card_id = ? WHERE id = ?";
            pst = conn.prepareStatement(sql);

            pst.setString(1, ben.getName());
            pst.setString(2, ben.getAddr());
            pst.setString(3, ben.getPhone());
            pst.setInt(4, ben.getAge());
            pst.setBoolean(5, ben.isHead());
            pst.setString(6, ben.getCardId());
            pst.setInt(7, ben.getId());

            int rows = pst.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Beneficiary updated: " + ben.getName());
            } else {
                System.out.println("⚠ No beneficiary found to update with id: " + ben.getId());
            }

        } catch (SQLException e) {
            System.err.println("⚠ Error updating beneficiary: " + e.getMessage());
        } finally {
            try { if (pst != null) pst.close(); } catch (Exception e) { }
            try { if (conn != null) conn.close(); } catch (Exception e) { }
        }
    }

    public static void deleteBeneficiary(int id) {
        Connection conn = null;
        PreparedStatement pst = null;

        try {
            conn = DatabaseManager.getConnection();
            String sql = "DELETE FROM beneficiaries WHERE id = ?";
            pst = conn.prepareStatement(sql);

            pst.setInt(1, id);

            int rows = pst.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Beneficiary deleted with id: " + id);
            } else {
                System.out.println("⚠ No beneficiary found to delete with id: " + id);
            }

        } catch (SQLException e) {
            System.err.println("⚠ Error deleting beneficiary: " + e.getMessage());
        } finally {
            try { if (pst != null) pst.close(); } catch (Exception e) { }
            try { if (conn != null) conn.close(); } catch (Exception e) { }
        }
    }

    public static void saveItem(Item item) {
        Connection conn = null;
        PreparedStatement pst = null;

        try {
            conn = DatabaseManager.getConnection();
            String sql = "INSERT INTO items (id, name, price, quantity, unit) VALUES (?, ?, ?, ?, ?)";
            pst = conn.prepareStatement(sql);

            pst.setString(1, item.getId());
            pst.setString(2, item.getName());
            pst.setDouble(3, item.getPrice());
            pst.setDouble(4, item.getQuantity());
            pst.setString(5, item.getUnit());

            int rows = pst.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Item saved: " + item.getName());
            } else {
                System.out.println("⚠ Failed to save item: " + item.getName());
            }

        } catch (SQLException e) {
            System.err.println("⚠ Error saving item: " + e.getMessage());
        } finally {
            try { if (pst != null) pst.close(); } catch (Exception e) { }
            try { if (conn != null) conn.close(); } catch (Exception e) { }
        }
    }


    public static void updateItem(Item item) {
        Connection conn = null;
        PreparedStatement pst = null;

        try {
            conn = DatabaseManager.getConnection();
            String sql = "UPDATE items SET name = ?, price = ?, quantity = ?, unit = ? WHERE id = ?";
            pst = conn.prepareStatement(sql);

            pst.setString(1, item.getName());
            pst.setDouble(2, item.getPrice());
            pst.setDouble(3, item.getQuantity());
            pst.setString(4, item.getUnit());
            pst.setString(5, item.getId());

            int rows = pst.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Item updated: " + item.getName());
            } else {
                System.out.println("⚠ No item found to update with id: " + item.getId());
            }

        } catch (SQLException e) {
            System.err.println("⚠ Error updating item: " + e.getMessage());
        } finally {
            try { if (pst != null) pst.close(); } catch (Exception e) { }
            try { if (conn != null) conn.close(); } catch (Exception e) { }
        }
    }


    public static void deleteItem(String itemId) {
        Connection conn = null;
        PreparedStatement pst = null;

        try {
            conn = DatabaseManager.getConnection();
            String sql = "DELETE FROM items WHERE id = ?";
            pst = conn.prepareStatement(sql);

            pst.setString(1, itemId);

            int rows = pst.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Item deleted with id: " + itemId);
            } else {
                System.out.println("⚠ No item found to delete with id: " + itemId);
            }

        } catch (SQLException e) {
            System.err.println("⚠ Error deleting item: " + e.getMessage());
        } finally {
            try { if (pst != null) pst.close(); } catch (Exception e) { }
            try { if (conn != null) conn.close(); } catch (Exception e) { }
        }
    }


    public static void saveTransaction(Tx tx) {
        Connection conn = null;
        PreparedStatement pst = null;

        try {
            conn = DatabaseManager.getConnection();
            String sql = "INSERT INTO transactions (id, card_id, date, total) VALUES (?, ?, ?, ?)";
            pst = conn.prepareStatement(sql);

            pst.setString(1, tx.getId());
            pst.setString(2, tx.getCard());
            pst.setTimestamp(3, Timestamp.valueOf(tx.getDateTime()));
            pst.setDouble(4, tx.getTotal());

            int rows = pst.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Transaction saved: " + tx.getId());
            } else {
                System.out.println("⚠ Failed to save transaction: " + tx.getId());
            }

        } catch (SQLException e) {
            System.err.println("⚠ Error saving transaction: " + e.getMessage());
        } finally {
            try { if (pst != null) pst.close(); } catch (Exception e) { }
            try { if (conn != null) conn.close(); } catch (Exception e) { }
        }
    }

    public static void saveTxItems(Tx tx) {
        Connection conn = null;
        PreparedStatement pst = null;

        try {
            conn = DatabaseManager.getConnection();
            String sql = "INSERT INTO tx_items (tx_id, item_id, item_name, quantity, price, unit) VALUES (?, ?, ?, ?, ?, ?)";
            pst = conn.prepareStatement(sql);

            for (Tx.ItemDetail detail : tx.getItems()) {
                pst.setString(1, tx.getId());
                pst.setString(2, detail.getItemId());
                pst.setString(3, detail.getName());
                pst.setDouble(4, detail.getQty());
                pst.setDouble(5, detail.getPrice());
                pst.setString(6, detail.getUnit());

                pst.addBatch(); // Batch insert for all items
            }

            pst.executeBatch();
            System.out.println("✅ Transaction items saved for: " + tx.getId());

        } catch (SQLException e) {
            System.err.println("⚠ Error saving transaction items: " + e.getMessage());
        } finally {
            try { if (pst != null) pst.close(); } catch (Exception e) { }
            try { if (conn != null) conn.close(); } catch (Exception e) { }
        }
    }
}
