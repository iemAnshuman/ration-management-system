package ration.ui;

import ration.model.Item;
import ration.model.Tx;
import ration.model.Tx.ItemDetail;
import ration.service.CardService;
import ration.service.InventoryService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DistributionPanel extends JPanel {
    private CardService cardService;
    private InventoryService inventoryService;
    private List<Tx> transactions;

    // Components for New Distribution
    private JTextField cardIdField;
    private JButton loadCardButton;
    private JTextArea cardInfoArea;
    private JTable itemsTable;
    private DefaultTableModel itemsModel;
    private JTextField distQtyField;
    private JButton addButton;
    private JTable cartTable;
    private DefaultTableModel cartModel;
    private JButton finalizeButton;

    // Components for History
    private JTable historyTable;
    private DefaultTableModel historyModel;

    public DistributionPanel(CardService cs, InventoryService is) {
        this.cardService = cs;
        this.inventoryService = is;
        this.transactions = new ArrayList<>();
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        JTabbedPane tabs = new JTabbedPane();

        // --- New Distribution Tab ---
        JPanel newDist = new JPanel(new BorderLayout(5,5));
        JPanel north = new JPanel();
        north.add(new JLabel("Card ID:"));
        cardIdField = new JTextField(8);
        north.add(cardIdField);
        loadCardButton = new JButton("Load Card");
        north.add(loadCardButton);
        newDist.add(north, BorderLayout.NORTH);

        cardInfoArea = new JTextArea(4,40);
        cardInfoArea.setEditable(false);
        newDist.add(new JScrollPane(cardInfoArea), BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setResizeWeight(0.5);

        // Left: Items
        JPanel left = new JPanel(new BorderLayout(5,5));
        String[] itemCols = {"ID","Name","Available","Unit","Price"};
        itemsModel = new DefaultTableModel(itemCols,0) {
            public boolean isCellEditable(int r,int c){return false;}
        };
        itemsTable = new JTable(itemsModel);
        left.add(new JScrollPane(itemsTable), BorderLayout.CENTER);
        JPanel leftSouth = new JPanel();
        leftSouth.add(new JLabel("Qty:"));
        distQtyField = new JTextField(5);
        leftSouth.add(distQtyField);
        addButton = new JButton("Add to Cart");
        leftSouth.add(addButton);
        left.add(leftSouth, BorderLayout.SOUTH);
        split.setLeftComponent(left);

        // Right: Cart
        JPanel right = new JPanel(new BorderLayout(5,5));
        String[] cartCols = {"ID","Name","Qty","Price"};
        cartModel = new DefaultTableModel(cartCols,0) {
            public boolean isCellEditable(int r,int c){return false;}
        };
        cartTable = new JTable(cartModel);
        right.add(new JScrollPane(cartTable), BorderLayout.CENTER);
        finalizeButton = new JButton("Finalize Distribution");
        right.add(finalizeButton, BorderLayout.SOUTH);
        split.setRightComponent(right);

        newDist.add(split, BorderLayout.SOUTH);
        tabs.addTab("New Distribution", newDist);

        // --- History Tab ---
        JPanel histPanel = new JPanel(new BorderLayout(5,5));
        String[] histCols = {"TX ID","Card","Date","Total"};
        historyModel = new DefaultTableModel(histCols,0) {
            public boolean isCellEditable(int r,int c){return false;}
        };
        historyTable = new JTable(historyModel);
        histPanel.add(new JScrollPane(historyTable), BorderLayout.CENTER);
        tabs.addTab("History", histPanel);

        add(tabs, BorderLayout.CENTER);

        // Listeners
        loadCardButton.addActionListener(e -> loadCard());
        addButton.addActionListener(e -> addToCart());
        finalizeButton.addActionListener(e -> finalizeDistribution());
        historyTable.getSelectionModel().addListSelectionListener(e -> showTransactionDetail(e));
    }

    private void loadCard() {
        String cid = cardIdField.getText().trim();
        var card = cardService.findCard(cid);
        if (card == null) {
            cardInfoArea.setText("Card not found.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Card ID: ").append(card.getId()).append("   Type: ").append(card.getType()).append("\n");
        sb.append("Members: ").append(card.getCount()).append("\n");
        sb.append("-------------------\n");
        for (var b : card.getMembers()) {
            sb.append(b.getName()).append(b.isHead()?" (Head)":"").append("\n");
        }
        cardInfoArea.setText(sb.toString());
        // populate items
        refreshItems();
        cartModel.setRowCount(0);
    }

    private void refreshItems() {
        itemsModel.setRowCount(0);
        for (Item it : inventoryService.getAll()) {
            itemsModel.addRow(new Object[]{it.getId(), it.getName(), it.getQuantity(), it.getUnit(), it.getPrice()});
        }
    }

    private void addToCart() {
        int idx = itemsTable.getSelectedRow();
        if (idx<0) return;
        String id = (String)itemsModel.getValueAt(idx,0);
        String name = (String)itemsModel.getValueAt(idx,1);
        double avail = (double)itemsModel.getValueAt(idx,2);
        String qtyStr = distQtyField.getText().trim();
        try {
            double q = Double.parseDouble(qtyStr);
            if (q<=0||q>avail) {
                JOptionPane.showMessageDialog(this,"Invalid qty","Error",JOptionPane.WARNING_MESSAGE);
                return;
            }
            double price = (double)itemsModel.getValueAt(idx,4);
            cartModel.addRow(new Object[]{id,name,q,price});
            itemsModel.setValueAt(avail - q, idx, 2);
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(this,"Invalid qty format","Error",JOptionPane.WARNING_MESSAGE);
        }
    }

    private void finalizeDistribution() {
        String cid = cardIdField.getText().trim();
        Tx tx = new Tx(cid);

        for (int i = 0; i < cartModel.getRowCount(); i++) {
            String id = (String) cartModel.getValueAt(i, 0);
            String name = (String) cartModel.getValueAt(i, 1);
            double q = (double) cartModel.getValueAt(i, 2);
            double price = (double) cartModel.getValueAt(i, 3);

            inventoryService.distribute(id, q);  // Update stock
            tx.addItem(id, name, q, price, inventoryService.getItem(id).getUnit());
        }

        try {
            ration.utils.DatabaseHandler.saveTransaction(tx);
            ration.utils.DatabaseHandler.saveTxItems(tx);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to save transaction: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        transactions.add(tx); // Keep in memory for session
        historyModel.addRow(new Object[]{tx.getId(), tx.getCard(), tx.getDateString(), tx.getTotal()});
        JOptionPane.showMessageDialog(this, "Distribution complete: " + tx.getId());

        cartModel.setRowCount(0);
        refreshItems();
    }


    private void showTransactionDetail(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int idx = historyTable.getSelectedRow();
            if (idx<0) return;
            Tx tx = transactions.get(idx);
            StringBuilder sb = new StringBuilder();
            sb.append("Transaction ").append(tx.getId()).append(" Details:\n");
            for (ItemDetail d : tx.getItems()) {
                sb.append(d.toString()).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString());
        }
    }
}
