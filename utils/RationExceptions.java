package ration.utils;

public class RationExceptions {
    public static class CardNotFoundException extends Exception {
        public CardNotFoundException(String message) {
            super(message);
        }
    }
    
    public static class InventoryException extends Exception {
        public InventoryException(String message) {
            super(message);
        }
    }
    
    public static class ValidationException extends Exception {
        public ValidationException(String message) {
            super(message);
        }
    }
    
    public static class DistributionException extends Exception {
        public DistributionException(String message) {
            super(message);
        }
    }

    public static class ItemNotFoundException extends Exception {
        public ItemNotFoundException(String message) {
            super(message);
        }
    }
    
    public static class IOException extends Exception {
        public IOException(String message) {
            super(message);
        }
        
        public IOException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    public static class DataFormatException extends Exception {
        public DataFormatException(String message) {
            super(message);
        }
        
        public DataFormatException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}