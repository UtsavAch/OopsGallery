package com.utsav.arts.dtos.cartItemDTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CartItemRequestDTO {

    @NotNull(message = "Cart ID is required")
    @Min(value = 1, message = "Cart ID must be a valid positive number")
    private int cartId;

    @NotNull(message = "Artwork ID is required")
    @Min(value = 1, message = "Artwork ID must be a valid positive number")
    private int artworkId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    public CartItemRequestDTO() {}

    // Getters & Setters
    public int getCartId() { return cartId; }
    public void setCartId(int cartId) { this.cartId = cartId; }

    public int getArtworkId() { return artworkId; }
    public void setArtworkId(int artworkId) { this.artworkId = artworkId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}