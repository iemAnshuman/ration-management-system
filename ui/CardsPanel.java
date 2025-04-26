package ration.ui;

import ration.model.Card;
import ration.model.Ben;
import ration.service.CardService;
import ration.service.BenSrv;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CardsPanel extends JPanel {
    private CardService cardService;
    private BenSrv benService;

    // Components for New Card tab
    private JComboBox<String> typeCombo;
    private JButton createCardButton;

    // Components for Add Member tab
    private JTextField addCardIdField;
    private JTextField nameField;
    private JTextField addrField;
    private JTextField phoneField;
    private JTextField ageField;
    private JCheckBox headCheck;
    private JButton addMemberButton;

    // Components for View Card tab
    private JTextField viewCardIdField;
    private JButton viewCardButton;
    private JTextArea viewCardArea;
    private JButton viewHistButton;

    public CardsPanel(CardService cs, BenSrv bs) {
        this.cardService = cs;
        this.benService = bs;
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        JTabbedPane tabs = new JTabbedPane();

        // New Card Tab
        JPanel newCardTab = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        newCardTab.add(new JLabel("Card Type:"), gbc);
        gbc.gridx = 1;
        typeCombo = new JComboBox<>(new String[]{"APL","BPL","AAY"});
        newCardTab.add(typeCombo, gbc);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        createCardButton = new JButton("Create Card");
        newCardTab.add(createCardButton, gbc);

        createCardButton.addActionListener(e -> handleCreateCard());

        tabs.addTab("New Card", newCardTab);

        // Add Member Tab
        JPanel addMemTab = new JPanel(new GridLayout(7,2,5,5));
        addMemTab.add(new JLabel("Card ID:")); addCardIdField = new JTextField(); addMemTab.add(addCardIdField);
        addMemTab.add(new JLabel("Name:")); nameField = new JTextField(); addMemTab.add(nameField);
        addMemTab.add(new JLabel("Address:")); addrField = new JTextField(); addMemTab.add(addrField);
        addMemTab.add(new JLabel("Phone:")); phoneField = new JTextField(); addMemTab.add(phoneField);
        addMemTab.add(new JLabel("Age:")); ageField = new JTextField(); addMemTab.add(ageField);
        addMemTab.add(new JLabel("Head of Family:")); headCheck = new JCheckBox(); addMemTab.add(headCheck);
        addMemberButton = new JButton("Add Member"); addMemTab.add(new JLabel()); addMemTab.add(addMemberButton);

        addMemberButton.addActionListener(e -> handleAddMember());

        tabs.addTab("Add Member", addMemTab);

        // View Card Tab
        JPanel viewTab = new JPanel(new BorderLayout(5,5));
        JPanel top = new JPanel();
        viewTab.add(top, BorderLayout.NORTH);
        top.add(new JLabel("Card ID:"));
        viewCardIdField = new JTextField(10); top.add(viewCardIdField);
        viewCardButton = new JButton("View"); top.add(viewCardButton);
        viewHistButton = new JButton("History"); top.add(viewHistButton);

        viewCardArea = new JTextArea(15, 40);
        viewCardArea.setEditable(false);
        viewTab.add(new JScrollPane(viewCardArea), BorderLayout.CENTER);

        viewCardButton.addActionListener(e -> handleViewCard(false));
        viewHistButton.addActionListener(e -> handleViewCard(true));

        tabs.addTab("View Card", viewTab);

        add(tabs, BorderLayout.CENTER);
    }

    private void handleCreateCard() {
        String type = (String) typeCombo.getSelectedItem();
        Card card = cardService.newCard(type);
        if (card != null) {
            JOptionPane.showMessageDialog(this, "Created card ID: " + card.getId());
        } else {
            JOptionPane.showMessageDialog(this, "Failed to create card.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAddMember() {
        try {
            String cid = addCardIdField.getText().trim();
            String name = nameField.getText().trim();
            String addr = addrField.getText().trim();
            String phone = phoneField.getText().trim();
            int age = Integer.parseInt(ageField.getText().trim());
            boolean isHead = headCheck.isSelected();

            Ben ben = benService.add(name, addr, phone, age, isHead);
            if (ben != null && cardService.addMember(cid, ben)) {
                JOptionPane.showMessageDialog(this, "Member added: " + ben.getName());
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add member.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid age.", "Input Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleViewCard(boolean showHistory) {
        String cid = viewCardIdField.getText().trim();
        Card card = cardService.findCard(cid);
        if (card == null) {
            viewCardArea.setText("Card not found.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(card.getId()).append("\nType: ").append(card.getType())
          .append("\nMembers: ").append(card.getCount()).append("\n\n");
        card.getMembers().forEach(b -> sb.append(b.getName())
                                          .append(b.isHead() ? " (Head)" : "")
                                          .append("\n"));

        if (showHistory) {
            sb.append("\n-- History --\n");
            card.showHist(); // This prints to console; consider refactoring Hist access for GUI
        }

        viewCardArea.setText(sb.toString());
    }
}
