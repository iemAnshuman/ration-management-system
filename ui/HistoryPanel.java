package ration.ui;

import ration.model.Tx;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HistoryPanel extends JPanel {
    private List<Tx> txList;
    private JTextField cardIdField;
    private JButton filterButton;
    private JTable txTable;
    private DefaultTableModel tableModel;

    public HistoryPanel(List<Tx> txList) {
        this.txList = txList;
        setLayout(new BorderLayout(5,5));
        initComponents();
    }

    private void initComponents() {
        // Top filter panel
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Card ID (blank = all):"));
        cardIdField = new JTextField(10);
        top.add(cardIdField);
        filterButton = new JButton("Show");
        top.add(filterButton);
        add(top, BorderLayout.NORTH);

        // Table setup
        String[] cols = {"TX ID", "Card", "Date", "Total"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        txTable = new JTable(tableModel);
        add(new JScrollPane(txTable), BorderLayout.CENTER);

        // Button action
        filterButton.addActionListener(e -> refreshTable());

        // Initial load
        refreshTable();
    }

    private void refreshTable() {
        String cid = cardIdField.getText().trim();
        tableModel.setRowCount(0);
        for (Tx tx : txList) {
            if (cid.isEmpty() || tx.getCard().equalsIgnoreCase(cid)) {
                tableModel.addRow(new Object[]{
                    tx.getId(),
                    tx.getCard(),
                    tx.getDateString(),
                    String.format("%.2f", tx.getTotal())
                });
            }
        }
    }
}
