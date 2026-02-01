package com.utsav.arts.models;

public enum PaymentStatus {
    PENDING,
    SUCCESS,
    FAILED,
    REFUNDED,
    CANCELLED;

    public boolean canTransitionTo(PaymentStatus next) {
        return switch (this) {
            case PENDING -> next == SUCCESS || next == FAILED || next == CANCELLED;
            case SUCCESS -> next == REFUNDED;
            case FAILED, CANCELLED, REFUNDED -> false; // terminal
        };
    }
}