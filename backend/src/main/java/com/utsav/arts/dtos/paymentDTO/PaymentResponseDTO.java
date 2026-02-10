package com.utsav.arts.dtos.paymentDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object representing a payment returned to the client.
 *
 * <p><strong>Fields:</strong>
 * <ul>
 *   <li>id – unique identifier of the payment</li>
 *   <li>orderId – ID of the associated order</li>
 *   <li>userId – ID of the user who made the payment</li>
 *   <li>amount – amount paid</li>
 *   <li>currency – currency of the payment</li>
 *   <li>method – payment method used (e.g., Stripe, PayPal)</li>
 *   <li>status – status of the payment (mapped from PaymentStatus enum)</li>
 *   <li>transactionId – unique transaction identifier from the payment gateway</li>
 *   <li>createdAt – timestamp when the payment was created</li>
 * </ul>
 * </p>
 */
public class PaymentResponseDTO {

    private int id;
    private int orderId;
    private int userId;
    private BigDecimal amount;
    private String currency;
    private String method;
    private String status;       // Will be mapped from enum
    private String transactionId;
    private LocalDateTime createdAt;

    public PaymentResponseDTO() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
