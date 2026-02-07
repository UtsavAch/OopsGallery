package com.utsav.arts.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    private int totalItems;

    private BigDecimal totalPrice;

    public Cart() {}

    public Cart(User user) {
        this.user = user;
        this.items = new ArrayList<>();
        this.totalItems = 0;
        this.totalPrice = BigDecimal.ZERO;
    }

    // ---------------- Getters & Setters ----------------
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }

    public int getTotalItems() { return totalItems; }
    public BigDecimal getTotalPrice() { return totalPrice; }

    // ---------------- Auto-recalculate totals ----------------
    public void recalculateTotals() {
        if (items == null || items.isEmpty()) {
            this.totalItems = 0;
            this.totalPrice = BigDecimal.ZERO;
            return;
        }

        this.totalItems = items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();

        this.totalPrice = items.stream()
                .map(item ->
                        item.getArtwork().getPrice()
                                .multiply(BigDecimal.valueOf(item.getQuantity()))
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // ---------------- JPA Callbacks ----------------
    @PrePersist
    @PreUpdate
    private void preSave() {
        recalculateTotals();
    }
}
