package com.utsav.arts.dtos.cartDTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object used to receive cart creation or update requests from clients.
 *
 * <p>Represents the minimal information required to create or update a cart.</p>
 *
 * <p><strong>Fields:</strong>
 * <ul>
 *   <li>userId – the ID of the user who owns the cart</li>
 * </ul>
 * </p>
 */
public class CartRequestDTO {

    @NotNull(message = "User ID is required")
    @Min(value = 1, message = "User ID must be a valid positive number")
    private int userId;

    public CartRequestDTO() {}

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
}