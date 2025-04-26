package ration.utils;

/**
 * Utility class for validation functions
 * Demonstrates utility class pattern
 */
public final class ValidationUtils {
    
    // Private constructor to prevent instantiation
    private ValidationUtils() {}
    
    /**
     * Validate a phone number (basic validation)
     * @param phoneNumber The phone number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }
        
        // Basic validation - only digits and length between 10-15
        return phoneNumber.matches("\\d{10,15}");
    }
    
    /**
     * Validate card category
     * @param category The category to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidCategory(String category) {
        if (category == null) {
            return false;
        }
        
        return category.equals(RationConstants.CATEGORY_APL) ||
               category.equals(RationConstants.CATEGORY_BPL) ||
               category.equals(RationConstants.CATEGORY_AAY);
    }
    
    /**
     * Validate a name (basic validation)
     * @param name The name to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty();
    }
    
    /**
     * Validate age (basic validation)
     * @param age The age to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidAge(int age) {
        return age > 0 && age < 120; // Basic range check
    }
    
    /**
     * Validate price (basic validation)
     * @param price The price to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPrice(double price) {
        return price >= 0;
    }
    
    /**
     * Validate quantity (basic validation)
     * @param quantity The quantity to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidQuantity(double quantity) {
        return quantity >= 0;
    }
}