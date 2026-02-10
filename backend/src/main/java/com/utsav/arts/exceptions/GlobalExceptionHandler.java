package com.utsav.arts.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.security.access.AccessDeniedException;

import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.Map;

/**
 * Global exception handler for the application.
 *
 * <p>Handles specific custom exceptions and common Spring exceptions to provide
 * consistent HTTP responses with proper status codes and messages.
 *
 * <p>Exceptions handled:
 * <ul>
 *   <li>ResourceNotFoundException → 404 Not Found</li>
 *   <li>ResourceAlreadyExistsException → 409 Conflict</li>
 *   <li>InvalidRequestException → 400 Bad Request</li>
 *   <li>MethodArgumentNotValidException → 400 Bad Request with validation details</li>
 *   <li>AccessDeniedException → 403 Forbidden</li>
 *   <li>Generic Exception → 500 Internal Server Error</li>
 * </ul>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /** Handles ResourceNotFoundException and returns 404 status with message */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /** Handles ResourceAlreadyExistsException and returns 409 status with message */
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleAlreadyExists(ResourceAlreadyExistsException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    /** Handles InvalidRequestException and returns 400 status with message */
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidRequest(InvalidRequestException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /**
     * Handles validation errors from @Valid annotated request bodies.
     * Aggregates all field error messages into a single comma-separated string.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return buildErrorResponse(HttpStatus.BAD_REQUEST, message);
    }

    /** Handles Spring Security access denied exceptions (403 Forbidden) */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.FORBIDDEN.value());
        body.put("error", "Forbidden");
        body.put("message", "You do not have permission to access this resource.");

        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    /** Handles any other generic exceptions, returning 500 Internal Server Error */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        ex.printStackTrace(); // optional: log properly
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong");
    }

    /** Utility method to build a consistent error response body */
    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String message) {
        Map<String, Object> body = Map.of("status", status.value(), "message", message);
        return new ResponseEntity<>(body, status);
    }
}
