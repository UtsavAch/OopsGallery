package com.utsav.arts.dtos.cartItemDTO;

public class CartItemRequestDTO {

    private int cartId;
    private int artworkId;
    private int quantity;

    public CartItemRequestDTO() {}

    public int getCartId() { return cartId; }
    public void setCartId(int cartId) { this.cartId = cartId; }

    public int getArtworkId() { return artworkId; }
    public void setArtworkId(int artworkId) { this.artworkId = artworkId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
