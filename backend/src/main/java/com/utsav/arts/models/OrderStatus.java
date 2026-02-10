package com.utsav.arts.models;

/**
 * Enum representing possible states of an order.
 */
public enum OrderStatus {
    PENDING,    // Order placed, payment pending/verifying
    CONFIRMED,  // Payment successful, order is valid
    SHIPPED,    // Sent to courier
    DELIVERED,  // Customer received it
    CANCELLED;  // Order canceled by user or admin


    /**
     * Determines if an order can transition to the given next status.
     *
     * @param next The next OrderStatus to transition to
     * @return true if allowed, false otherwise
     */
    public boolean canTransitionTo(OrderStatus next) {
        return switch (this) {
            case PENDING -> next == CONFIRMED || next == CANCELLED;
            case CONFIRMED -> next == SHIPPED;
            case SHIPPED -> next == DELIVERED;
            case DELIVERED, CANCELLED -> false; // terminal states
        };
    }
}