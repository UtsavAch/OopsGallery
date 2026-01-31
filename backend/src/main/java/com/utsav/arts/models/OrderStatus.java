package com.utsav.arts.models;

public enum OrderStatus {
    PENDING,    // Order placed, payment pending/verifying
    CONFIRMED,  // Payment successful, order is valid
    SHIPPED,    // Sent to courier
    DELIVERED,  // Customer received it
    CANCELLED   // Order cancelled by user or admin
}