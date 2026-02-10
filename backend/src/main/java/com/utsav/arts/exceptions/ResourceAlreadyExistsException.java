package com.utsav.arts.exceptions;

/**
 * Exception thrown when a request is invalid or contains invalid data.
 * Returns HTTP 400 (Bad Request) when handled globally.
 */
public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
