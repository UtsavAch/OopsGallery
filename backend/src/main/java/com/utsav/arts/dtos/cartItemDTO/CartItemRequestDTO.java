package com.utsav.arts.dtos.cartItemDTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object used to receive requests for adding or updating
 * an item in a cart.
 *
 * <p><strong>Fields:</strong>
 * <ul>
 *   <li>cartId – ID of the cart to which the item belongs</li>
 *   <li>artworkId – ID of the artwork to add/update</li>
 *   <li>quantity – quantity of the artwork (default must be at least 1)</li>
 * </ul>
 * </p>
 */
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