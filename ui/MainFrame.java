package ration.ui;

import ration.service.CardService;
import ration.service.InventoryService;
import ration.service.BenSrv;
import ration.utils.FileHandler;
import ration.utils.RationExceptions;
import ration.model.Tx;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class MainFrame extends JFrame {
    private CardService cardService;
    private InventoryService inventoryService;
    private BenSrv benService;
    private List<Tx> txList;

    public MainFrame(CardService cs,
                     InventoryService is,
                     BenSrv bs,
                     List<Tx> txList) {
        super("Ration Management System");
        this.cardService = cs;
        this.inventoryService = is;
        this.benService = bs;
        this.txList = txList;
        initialize();
    }

    private void initialize() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Cards", new CardsPanel(cardService, benService));
        tabs.addTab("Stock", new StockPanel(inventoryService));
        tabs.addTab("Distribution", new DistributionPanel(cardService, inventoryService));
        tabs.addTab("History", new HistoryPanel(txList));

        add(tabs, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        // Instantiate services
        CardService cs = new ration.service.Cards();
        InventoryService is = new ration.service.Inventory();
        BenSrv bs = new ration.service.BenSrvImpl();
        List<Tx> txList = new ArrayList<>();

        // Load existing data
        try {
            FileHandler.importFromCSV(cs, bs, is, txList);
        } catch (RationExceptions.IOException e) {
            System.err.println("Failed to load data: " + e.getMessage());
        }

        // Launch GUI
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(cs, is, bs, txList);
            frame.setVisible(true);
        });
    }
}
