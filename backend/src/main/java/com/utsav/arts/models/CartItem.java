package com.utsav.arts.models;

import jakarta.persistence.*;

@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "artwork_id")
    private Artwork artwork;

    private int quantity;

    public CartItem() {}

    public CartItem(Cart cart, Artwork artwork, int quantity) {
        this.cart = cart;
        this.artwork = artwork;
        this.quantity = quantity;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Cart getCart() { return cart; }
    public void setCart(Cart cart) { this.cart = cart; }

    public Artwork getArtwork() { return artwork; }
    public void setArtwork(Artwork artwork) { this.artwork = artwork; }

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }
        this.quantity = quantity;
    }
}
