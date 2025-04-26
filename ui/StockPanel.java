package ration.ui;

import ration.model.Item;
import ration.service.InventoryService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class StockPanel extends JPanel {
    private InventoryService inventoryService;

    // Components for New Item
    private JTextField nameField;
    private JTextField priceField;
    private JTextField qtyField;
    private JTextField unitField;
    private JButton addItemButton;

    // Components for Edit Item
    private JTable itemTable;
    private DefaultTableModel tableModel;
    private JTextField editNameField;
    private JTextField editPriceField;
    private JTextField editQtyField;
    private JTextField editUnitField;
    private JButton updateItemButton;

    public StockPanel(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        JTabbedPane tabs = new JTabbedPane();

        // New Item Tab
        JPanel newItemTab = new JPanel(new GridLayout(5, 2, 5, 5));
        newItemTab.add(new JLabel("Name:"));    nameField = new JTextField(); newItemTab.add(nameField);
        newItemTab.add(new JLabel("Price:"));   priceField = new JTextField(); newItemTab.add(priceField);
        newItemTab.add(new JLabel("Quantity:"));qtyField = new JTextField(); newItemTab.add(qtyField);
        newItemTab.add(new JLabel("Unit:"));    unitField = new JTextField(); newItemTab.add(unitField);
        addItemButton = new JButton("Add Item"); newItemTab.add(new JLabel()); newItemTab.add(addItemButton);

        addItemButton.addActionListener(e -> handleAddItem());
        tabs.addTab("New Item", newItemTab);

        // Edit Item Tab
        JPanel editTab = new JPanel(new BorderLayout(5, 5));

        // Table
        String[] cols = {"ID", "Name", "Price", "Qty", "Unit"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        itemTable = new JTable(tableModel);
        itemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        refreshTable();
        editTab.add(new JScrollPane(itemTable), BorderLayout.CENTER);

        // Edit form
        JPanel form = new JPanel(new GridLayout(5, 2, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder("Edit Selected Item"));
        form.add(new JLabel("Name:")); editNameField = new JTextField(); form.add(editNameField);
        form.add(new JLabel("Price:")); editPriceField = new JTextField(); form.add(editPriceField);
        form.add(new JLabel("Quantity:")); editQtyField = new JTextField(); form.add(editQtyField);
        form.add(new JLabel("Unit:")); editUnitField = new JTextField(); form.add(editUnitField);
        updateItemButton = new JButton("Update Item"); form.add(new JLabel()); form.add(updateItemButton);

        editTab.add(form, BorderLayout.SOUTH);

        // Listeners
        itemTable.getSelectionModel().addListSelectionListener(e -> populateEditForm());
        updateItemButton.addActionListener(e -> handleUpdateItem());

        tabs.addTab("Edit Item", editTab);

        add(tabs, BorderLayout.CENTER);
    }

    private void handleAddItem() {
        try {
            String name = nameField.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());
            double qty = Double.parseDouble(qtyField.getText().trim());
            String unit = unitField.getText().trim();

            Item item = inventoryService.add(name, price, qty, unit);
            if (item != null) {
                JOptionPane.showMessageDialog(this, "Item added: " + item.getId());
                clearNewForm();
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add item.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format.", "Input Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void populateEditForm() {
        int idx = itemTable.getSelectedRow();
        if (idx < 0) return;
        String id = (String) tableModel.getValueAt(idx, 0);
        Item item = inventoryService.getItem(id);
        if (item == null) return;

        editNameField.setText(item.getName());
        editPriceField.setText(String.valueOf(item.getPrice()));
        editQtyField.setText(String.valueOf(item.getQuantity()));
        editUnitField.setText(item.getUnit());
    }

    private void handleUpdateItem() {
        int idx = itemTable.getSelectedRow();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "No item selected.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            String id = (String) tableModel.getValueAt(idx, 0);
            String name = editNameField.getText().trim();
            double price = Double.parseDouble(editPriceField.getText().trim());
            double qty = Double.parseDouble(editQtyField.getText().trim());
            String unit = editUnitField.getText().trim();

            boolean ok = inventoryService.update(id, name, price, qty, unit);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Item updated.");
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Update failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format.", "Input Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void clearNewForm() {
        nameField.setText(""); priceField.setText(""); qtyField.setText(""); unitField.setText("");
    }

    private void refreshTable() {
        List<Item> items = inventoryService.getAll();
        tableModel.setRowCount(0);
        for (Item it : items) {
            tableModel.addRow(new Object[]{it.getId(), it.getName(), it.getPrice(), it.getQuantity(), it.getUnit()});
        }
    }
}
