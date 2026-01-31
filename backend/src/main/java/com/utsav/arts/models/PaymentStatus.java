package com.utsav.arts.models;

public enum PaymentStatus {
    PENDING, // Payment initiated, awaiting confirmation
    SUCCESS, // Payment completed successfully
    FAILED, // Payment failed
    REFUNDED, // Payment refunded to the user
    CANCELLED // Payment cancelled by user or system
}