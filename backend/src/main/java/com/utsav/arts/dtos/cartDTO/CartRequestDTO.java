package com.utsav.arts.dtos.cartDTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CartRequestDTO {

    @NotNull(message = "User ID is required")
    @Min(value = 1, message = "User ID must be a valid positive number")
    private int userId;

    public CartRequestDTO() {}

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
}