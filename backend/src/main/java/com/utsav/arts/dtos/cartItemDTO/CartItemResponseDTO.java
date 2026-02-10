package com.utsav.arts.dtos.cartItemDTO;

/**
 * Data Transfer Object used to send cart item information back to clients.
 *
 * <p><strong>Fields:</strong>
 * <ul>
 *   <li>id – identifier of the cart item</li>
 *   <li>cartId – ID of the cart containing this item</li>
 *   <li>artworkId – ID of the artwork</li>
 *   <li>quantity – quantity of the artwork in the cart</li>
 * </ul>
 * </p>
 */
public class CartItemResponseDTO {

    private int id;
    private int cartId;
    private int artworkId;
    private int quantity;

    public CartItemResponseDTO() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCartId() { return cartId; }
    public void setCartId(int cartId) { this.cartId = cartId; }

    public int getArtworkId() { return artworkId; }
    public void setArtworkId(int artworkId) { this.artworkId = artworkId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
