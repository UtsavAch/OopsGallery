package com.utsav.arts.dtos.paymentDTO;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class PaymentRequestDTO {

    @NotNull(message = "Order ID is required")
    @Min(value = 1, message = "Order ID must be a valid positive number")
    private int orderId;

    @NotNull(message = "User ID is required")
    @Min(value = 1, message = "User ID must be a valid positive number")
    private int userId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Payment amount must be greater than zero")
    private BigDecimal amount;

    @NotBlank(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency must be a 3-letter ISO code (e.g., USD, NPR)")
    private String currency;

    @NotBlank(message = "Payment method is required")
    private String method;

    private String status; // Usually set by the system, but validated if passed

    @NotBlank(message = "Transaction ID is required")
    private String transactionId;

    public PaymentRequestDTO() {}

    // Getters & Setters
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
}