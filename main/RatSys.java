package ration.main;

import ration.model.Tx;
import ration.service.*;
import ration.utils.DatabaseHandler;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import ration.ui.MainFrame;

public class RatSys {

    public static void main(String[] args) {
        // Prepare services
        CardService cardService = new Cards();
        InventoryService inventoryService = new Inventory();
        BenSrv benService = new BenSrvImpl();
        List<Tx> txList = new ArrayList<>();

        // Load everything from MySQL
        DatabaseHandler.loadAll(cardService, benService, inventoryService, txList);

        // Start GUI
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(cardService, inventoryService, benService, txList);
            frame.setVisible(true);
        });
    }
}
