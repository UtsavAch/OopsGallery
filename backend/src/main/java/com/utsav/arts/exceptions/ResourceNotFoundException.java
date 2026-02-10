package com.utsav.arts.exceptions;

/**
 * Exception thrown when a resource already exists and cannot be created again.
 * Returns HTTP 409 (Conflict) when handled globally.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
