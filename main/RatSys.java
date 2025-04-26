package ration.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import ration.model.*;
import ration.service.*;
import ration.utils.RationConstants;
import ration.utils.RationExceptions;
import ration.utils.FileHandler;



public class RatSys {

    private CardService rs;
    private InventoryService is;
    private BenSrv bs;
    private Scanner sc;
    private List<Tx> tx;

    public RatSys() {
        rs = new Cards();
        is = new Inventory();
        bs = new BenSrvImpl();
        sc = new Scanner(System.in);
        tx = new ArrayList<>();
        try {
            loadFromCSV();
        } catch (RationExceptions.IOException e) {
            System.err.println("Failed to load data: " + e.getMessage());
            loadDemo();
        }
    }

    public static void main(String[] args) {
        RatSys sys = new RatSys();
        sys.run();
    }

    public void run() {
        boolean go = true;

        try {
            while (go) {
                menu();
                int opt = getInt("Pick: ");

                switch (opt) {
                    case RationConstants.MENU_EXIT:
                        saveData();
                        go = false;
                        System.out.println("Bye!");
                        break;

                    case RationConstants.MENU_RATION_CARD:
                        cardMenu();
                        break;

                    case RationConstants.MENU_INVENTORY:
                        invMenu();
                        break;

                    case RationConstants.MENU_DISTRIBUTION:
                        distMenu();
                        break;

                    case 4: // New export option
                        saveData();
                        break;

                    default:
                        System.out.println("Bad option!");
                }
            }
        } catch (Exception e) {
            System.err.println("Critical error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            sc.close();
        }
    }

    private void loadFromCSV() throws RationExceptions.IOException {
        FileHandler.importFromCSV(rs, bs, is, tx);
    }

    private void saveData() {
        try {
            FileHandler.exportToCSV(rs, bs, is, tx);
            System.out.println("Data saved!");
        } catch (RationExceptions.IOException e) {
            System.err.println("Save failed: " + e.getMessage());
        }
    }

    private void menu() {
        System.out.println("\n====================");
        System.out.println("\tRATION SYSTEM");
        System.out.println("====================");
        System.out.println("1. Cards");
        System.out.println("2. Stock");
        System.out.println("3. Dist");
        System.out.println("4. Save Data");
        System.out.println("0. Exit");
        System.out.println("====================");
    }

    private void cardMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n----- CARDS -----");
            System.out.println("1. New Card");
            System.out.println("2. Add Member");
            System.out.println("3. View Card");
            System.out.println("0. Back");

            int opt = getInt("Pick: ");

            switch (opt) {
                case RationConstants.SUBMENU_BACK:
                    back = true;
                    break;

                case RationConstants.SUBMENU_RC_REGISTER:
                    newCard();
                    break;

                case RationConstants.SUBMENU_RC_ADD_MEMBER:
                    addMem();
                    break;

                case RationConstants.SUBMENU_RC_VIEW_DETAILS:
                    viewCard();
                    break;

                default:
                    System.out.println("Bad option!");
            }
        }
    }

    private void invMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n----- STOCK -----");
            System.out.println("1. New Item");
            System.out.println("2. Edit Item");
            System.out.println("3. List Items");
            System.out.println("0. Back");

            int opt = getInt("Pick: ");

            switch (opt) {
                case RationConstants.SUBMENU_BACK:
                    back = true;
                    break;

                case RationConstants.SUBMENU_INV_ADD_ITEM:
                    newItem();
                    break;

                case RationConstants.SUBMENU_INV_UPDATE_ITEM:
                    editItem();
                    break;

                case RationConstants.SUBMENU_INV_VIEW_ITEMS:
                    is.showAll();
                    break;

                default:
                    System.out.println("Bad option!");
            }
        }
    }

    private void distMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n----- DIST -----");
            System.out.println("1. New Dist");
            System.out.println("2. History");
            System.out.println("0. Back");

            int opt = getInt("Pick: ");

            switch (opt) {
                case RationConstants.SUBMENU_BACK:
                    back = true;
                    break;

                case RationConstants.SUBMENU_DIST_NEW:
                    newDist();
                    break;

                case RationConstants.SUBMENU_DIST_HISTORY:
                    showHist();
                    break;

                default:
                    System.out.println("Bad option!");
            }
        }
    }

    private void newCard() {
        System.out.println("\n----- NEW CARD -----");

        System.out.println("Pick type:");
        System.out.println("1. APL");
        System.out.println("2. BPL");
        System.out.println("3. AAY");

        int cat = getInt("Pick: ");
        String typ;

        switch (cat) {
            case 1: typ = Card.TYPE_APL; break;
            case 2: typ = Card.TYPE_BPL; break;
            case 3: typ = Card.TYPE_AAY; break;
            default:
                System.out.println("Bad pick, using APL");
                typ = Card.TYPE_APL;
        }

        try {
            Card rc = rs.newCard(typ);

            if (rc != null) {
                System.out.println("\nCard made!");
                System.out.println("ID: " + rc.getId());
                System.out.println("Type: " + rc.getType());

                System.out.println("\nAdd main person:");
                addMain(rc.getId());
            } else {
                System.out.println("Failed! Try again.");
            }
        } catch (Exception e) {
            System.err.println("Error creating card: " + e.getMessage());
        }
    }

    private void addMain(String cid) {
        System.out.println("\n----- MAIN PERSON -----");

        System.out.print("Name: ");
        String name = sc.nextLine();

        System.out.print("Addr: ");
        String addr = sc.nextLine();

        System.out.print("Phone: ");
        String ph = sc.nextLine();

        int age = getInt("Age: ");

        try {
            Ben ben = bs.add(name, addr, ph, age, true);

            if (ben != null) {
                boolean ok = rs.addMember(cid, ben);

                if (ok) {
                    System.out.println("\nPerson added!");

                    System.out.print("Add more? (y/n): ");
                    String ans = sc.nextLine().trim().toLowerCase();

                    if (ans.equals("y") || ans.equals("yes")) {
                        addMore(cid);
                    }
                } else {
                    System.out.println("Add failed!");
                }
            } else {
                System.out.println("Make failed!");
            }
        } catch (Exception e) {
            System.err.println("Error adding person: " + e.getMessage());
        }
    }

    private void addMore(String cid) {
        boolean more = true;

        while (more) {
            addToCard(cid);

            System.out.print("Add more? (y/n): ");
            String ans = sc.nextLine().trim().toLowerCase();

            more = ans.equals("y") || ans.equals("yes");
        }
    }

    private void addMem() {
        System.out.println("\n----- ADD MEMBER -----");

        System.out.print("Card ID: ");
        String cid = sc.nextLine().trim();

        Card rc = rs.findCard(cid);

        if (rc == null) {
            System.out.println("Card not found!");
            return;
        }

        addToCard(cid);
    }

    private void addToCard(String cid) {
        System.out.println("\n----- ADD MEMBER -----");

        System.out.print("Name: ");
        String name = sc.nextLine();

        System.out.print("Addr: ");
        String addr = sc.nextLine();

        System.out.print("Phone: ");
        String ph = sc.nextLine();

        int age = getInt("Age: ");

        try {
            Ben ben = bs.add(name, addr, ph, age, false);

            if (ben != null) {
                boolean ok = rs.addMember(cid, ben);

                if (ok) {
                    System.out.println("\nAdded OK!");
                } else {
                    System.out.println("Add failed!");
                }
            } else {
                System.out.println("Make failed!");
            }
        } catch (Exception e) {
            System.err.println("Error adding member: " + e.getMessage());
        }
    }

    private void viewCard() {
        System.out.println("\n----- VIEW CARD -----");

        System.out.print("Card ID: ");
        String cid = sc.nextLine().trim();

        Card rc = rs.findCard(cid);

        if (rc == null) {
            System.out.println("Not found!");
            return;
        }

        rc.show();

        System.out.print("\nSee history? (y/n): ");
        String ans = sc.nextLine().trim().toLowerCase();

        if (ans.equals("y") || ans.equals("yes")) {
            rc.showHist();
        }
    }

    private void newItem() {
        System.out.println("\n----- NEW ITEM -----");

        System.out.print("Name: ");
        String name = sc.nextLine();

        double price = getDouble("Price: ");
        double qty = getDouble("Qty: ");

        System.out.print("Unit: ");
        String unit = sc.nextLine();

        try {
            Item item = is.add(name, price, qty, unit);

            if (item != null) {
                System.out.println("\nItem added!");
                item.display();
            } else {
                System.out.println("Add failed!");
            }
        } catch (Exception e) {
            System.err.println("Error adding item: " + e.getMessage());
        }
    }

    private void editItem() {
        System.out.println("\n----- EDIT ITEM -----");

        is.showAll();

        System.out.print("\nItem ID: ");
        String iid = sc.nextLine().trim();

        Item item = is.getItem(iid);

        if (item == null) {
            System.out.println("Not found!");
            return;
        }

        System.out.println("\nCurrent Item:");
        item.display();

        System.out.println("\nNew details (blank = keep old):");

        System.out.print("Name: ");
        String name = sc.nextLine();

        double price = item.getPrice();
        try {
            System.out.print("Price (" + item.getPrice() + "): ");
            String input = sc.nextLine();
            if (!input.isEmpty()) {
                price = Double.parseDouble(input);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid price, keeping old value");
        }

        double qty = item.getQuantity();
        try {
            System.out.print("Qty (" + item.getQuantity() + "): ");
            String input = sc.nextLine();
            if (!input.isEmpty()) {
                qty = Double.parseDouble(input);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity, keeping old value");
        }

        System.out.print("Unit (" + item.getUnit() + "): ");
        String unit = sc.nextLine();
        if (unit.isEmpty()) {
            unit = item.getUnit();
        }

        try {
            boolean ok = is.update(iid, name.isEmpty() ? item.getName() : name, price, qty, unit);

            if (ok) {
                System.out.println("\nItem updated!");
                item.display();
            } else {
                System.out.println("Update failed!");
            }
        } catch (Exception e) {
            System.err.println("Error updating item: " + e.getMessage());
        }
    }

    private void newDist() {
        System.out.println("\n----- NEW DIST -----");

        System.out.print("Card ID: ");
        String cid = sc.nextLine().trim();

        Card rc = rs.findCard(cid);

        if (rc == null) {
            System.out.println("Card not found!");
            return;
        }

        rc.show();

        Tx tr = new Tx(cid);

        System.out.println("\nItems:");
        is.showAll();

        boolean more = true;

        while (more) {
            System.out.print("\nItem ID: ");
            String iid = sc.nextLine().trim();

            Item item = is.getItem(iid);

            if (item == null) {
                System.out.println("Not found!");
                continue;
            }

            double qty = getDouble("Qty (" + item.getQuantity() + " " + item.getUnit() + "): ");

            if (!item.hasEnough(qty)) {
                System.out.println("Not enough!");
                continue;
            }

            try {
                tr.addItem(item.getId(), item.getName(), qty, item.getPrice(), item.getUnit());

                boolean cut = is.distribute(iid, qty);

                if (!cut) {
                    System.out.println("Update failed!");
                    continue;
                }

                System.out.print("More? (y/n): ");
                String ans = sc.nextLine().trim().toLowerCase();

                more = ans.equals("y") || ans.equals("yes");
            } catch (Exception e) {
                System.err.println("Error adding item to transaction: " + e.getMessage());
            }
        }

        tx.add(tr);

        tr.display();

        System.out.println("\nDist done!");
    }

    private void showHist() {
        System.out.println("\n----- HISTORY -----");

        if (tx.isEmpty()) {
            System.out.println("No data yet.");
            return;
        }

        System.out.print("Card ID (or 'all'): ");
        String in = sc.nextLine().trim();

        if (in.equalsIgnoreCase("all")) {
            for (Tx tr : tx) {
                tr.display();
                System.out.println("----------");
            }
        } else {
            boolean found = false;

            for (Tx tr : tx) {
                if (tr.getCard().equals(in)) {
                    tr.display();
                    System.out.println("----------");
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No data for: " + in);
            }
        }
    }

    private int getInt(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Not a valid number! Please try again.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage() + ". Please try again.");
            }
        }
    }

    private double getDouble(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Not a valid number! Please try again.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage() + ". Please try again.");
            }
        }
    }

    private void loadDemo() {
        try {
            Card rc = rs.newCard(Card.TYPE_BPL);
            Ben p1 = bs.add("Anshuman", "Delhi", "9876543210", 20, true);
            rs.addMember(rc.getId(), p1);

            Ben p2 = bs.add("Saral", "Bareli", "9876543211", 21, false);
            rs.addMember(rc.getId(), p2);

            Ben p3 = bs.add("Priyanshu", "UK", "9876543212", 19, false);
            rs.addMember(rc.getId(), p3);

            Card rc2 = rs.newCard(Card.TYPE_APL);
            Ben p4 = bs.add("Anadi", "dehradun", "9876543213", 20, true);
            rs.addMember(rc2.getId(), p4);
        } catch (Exception e) {
            System.err.println("Error loading demo data: " + e.getMessage());
        }
    }
}