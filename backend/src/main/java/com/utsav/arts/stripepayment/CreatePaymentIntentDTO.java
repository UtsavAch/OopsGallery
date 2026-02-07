package com.utsav.arts.stripepayment;

import jakarta.validation.constraints.*;

public class CreatePaymentIntentDTO {

    @NotNull(message = "Order ID is required")
    @Min(value = 1, message = "Order ID must be a valid positive number")
    private int orderId;

    @NotBlank(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency must be a 3-letter ISO code (e.g., EUR, USD)")
    private String currency;

    public CreatePaymentIntentDTO() {}

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
