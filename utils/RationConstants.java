package ration.utils;

/**
 * Constants used throughout the application
 * Demonstrates final class and constants
 */
public final class RationConstants {
    
    // Private constructor to prevent instantiation
    private RationConstants() {}
    
    // Card Category Constants
    public static final String CATEGORY_APL = "APL"; // Above Poverty Line
    public static final String CATEGORY_BPL = "BPL"; // Below Poverty Line
    public static final String CATEGORY_AAY = "AAY"; // Antyodaya Anna Yojana
    
    // Entitlement Constants
    public static final double APL_RICE_KG = 15.0;
    public static final double BPL_RICE_KG = 25.0;
    public static final double AAY_RICE_KG = 35.0;
    
    public static final double APL_WHEAT_KG = 10.0;
    public static final double BPL_WHEAT_KG = 15.0;
    public static final double AAY_WHEAT_KG = 20.0;
    
    public static final double APL_SUGAR_KG = 2.0;
    public static final double BPL_SUGAR_KG = 3.0;
    public static final double AAY_SUGAR_KG = 4.0;
    
    public static final double APL_KEROSENE_LITER = 2.0;
    public static final double BPL_KEROSENE_LITER = 3.0;
    public static final double AAY_KEROSENE_LITER = 5.0;
    
    // Unit Constants
    public static final String UNIT_KG = "kg";
    public static final String UNIT_LITER = "liter";
    public static final String UNIT_PIECE = "piece";
    
    // Menu Option Constants
    public static final int MENU_EXIT = 0;
    public static final int MENU_RATION_CARD = 1;
    public static final int MENU_INVENTORY = 2;
    public static final int MENU_DISTRIBUTION = 3;
    
    // Sub-menu Constants
    public static final int SUBMENU_BACK = 0;
    
    // Ration Card Sub-menu
    public static final int SUBMENU_RC_REGISTER = 1;
    public static final int SUBMENU_RC_ADD_MEMBER = 2;
    public static final int SUBMENU_RC_VIEW_DETAILS = 3;
    
    // Inventory Sub-menu
    public static final int SUBMENU_INV_ADD_ITEM = 1;
    public static final int SUBMENU_INV_UPDATE_ITEM = 2;
    public static final int SUBMENU_INV_VIEW_ITEMS = 3;
    
    // Distribution Sub-menu
    public static final int SUBMENU_DIST_NEW = 1;
    public static final int SUBMENU_DIST_HISTORY = 2;
    
    // Error Messages
    public static final String ERROR_INVALID_OPTION = "Invalid option selected. Please try again.";
    public static final String ERROR_CARD_NOT_FOUND = "Ration card not found with the given number.";
    public static final String ERROR_ITEM_NOT_FOUND = "Item not found with the given ID.";
}