package com.jumbotail.shipping.exception;

/**
 * Thrown when no warehouses are available in the system to calculate shipping charges.
 */
public class NoWarehousesAvailableException extends RuntimeException {
    
    public NoWarehousesAvailableException(String message) {
        super(message);
    }
}
