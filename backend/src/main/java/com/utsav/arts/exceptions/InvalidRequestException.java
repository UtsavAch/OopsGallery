package com.utsav.arts.exceptions;

/**
 * Exception thrown when a requested resource is not found.
 * Returns HTTP 404 (Not Found) when handled globally.
 */
public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }
}
