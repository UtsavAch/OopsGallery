package com.utsav.arts.models;

import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 * Represents an item in an order.
 * Stores a snapshot of the artwork price at the time of purchase to maintain historical accuracy.
 */
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "artwork_id", nullable = false)
    private Artwork artwork;

    private int quantity;

    // We store price here to freeze it.
    // If Artwork price changes later, this historical record remains correct.
    private BigDecimal priceAtPurchase;

    /** Default constructor for JPA */
    public OrderItem() {}

    /**
     * Constructs a new OrderItem.
     *
     * @param order The order it belongs to
     * @param artwork The artwork being purchased
     * @param quantity Quantity purchased
     * @param priceAtPurchase Price at purchase time
     */
    public OrderItem(Orders order, Artwork artwork, int quantity, BigDecimal priceAtPurchase) {
        this.order = order;
        this.artwork = artwork;
        this.quantity = quantity;
        this.priceAtPurchase = priceAtPurchase;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Orders getOrder() { return order; }
    public void setOrder(Orders order) { this.order = order; }

    public Artwork getArtwork() { return artwork; }
    public void setArtwork(Artwork artwork) { this.artwork = artwork; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getPriceAtPurchase() { return priceAtPurchase; }
    public void setPriceAtPurchase(BigDecimal priceAtPurchase) { this.priceAtPurchase = priceAtPurchase; }
}