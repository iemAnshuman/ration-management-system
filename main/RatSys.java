package ration.main;

import ration.ui.MainFrame;
import ration.model.Tx;
import ration.service.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class RatSys {

    public static void main(String[] args) {
        // Prepare the services
        CardService cardService = new Cards();
        InventoryService inventoryService = new Inventory();
        BenSrv benService = new BenSrvImpl();
        List<Tx> txList = new ArrayList<>();

        // (optional) TODO: load CSV data into services and txList here if you want

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(cardService, inventoryService, benService, txList);
            frame.setVisible(true);
        });
    }
}
