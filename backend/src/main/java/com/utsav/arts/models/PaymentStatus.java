package com.utsav.arts.models;

/**
 * Enum representing the status of a payment.
 */
public enum PaymentStatus {
    PENDING, // Payment is initiated but not yet completed
    SUCCESS, // Payment completed successfully
    FAILED, // Payment failed
    REFUNDED, // Payment was refunded
    CANCELLED; // Payment was canceled

    /**
     * Determines if a payment can transition to the given next status.
     *
     * @param next The next PaymentStatus to transition to
     * @return true if the transition is allowed, false otherwise
     */
    public boolean canTransitionTo(PaymentStatus next) {
        return switch (this) {
            case PENDING -> next == SUCCESS || next == FAILED || next == CANCELLED;
            case SUCCESS -> next == REFUNDED;
            case FAILED, CANCELLED, REFUNDED -> false; // terminal
        };
    }
}