package com.utsav.arts.models;

public enum OrderStatus {
    PENDING,    // Order placed, payment pending/verifying
    CONFIRMED,  // Payment successful, order is valid
    SHIPPED,    // Sent to courier
    DELIVERED,  // Customer received it
    CANCELLED;  // Order canceled by user or admin

    // Helper: define allowed next statuses
    public boolean canTransitionTo(OrderStatus next) {
        return !switch (this) {
            case PENDING -> next == CONFIRMED || next == CANCELLED;
            case CONFIRMED -> next == SHIPPED;
            case SHIPPED -> next == DELIVERED;
            case DELIVERED, CANCELLED -> false; // terminal states
        };
    }
}