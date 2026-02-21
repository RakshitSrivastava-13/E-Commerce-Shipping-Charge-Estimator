package com.jumbotail.shipping.exception;

/**
 * Thrown when an invalid delivery speed is provided.
 */
public class InvalidDeliverySpeedException extends RuntimeException {
    
    public InvalidDeliverySpeedException(String message) {
        super(message);
    }
}
