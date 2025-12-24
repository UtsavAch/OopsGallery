package com.utsav.arts.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "artwork_id", nullable = false)
    private Artwork artwork;

    private int price;

    private String address;

    private String status;

    @Column(name = "ordered_at")
    private LocalDateTime orderedAt;

    // Default constructor required by JPA
    public Orders() {}

    // Convenience constructor
    public Orders(int id, User user, Artwork artwork, int price, String address, String status, LocalDateTime orderedAt) {
        this.id = id;
        this.user = user;
        this.artwork = artwork;
        this.price = price;
        this.address = address;
        this.status = status;
        this.orderedAt = orderedAt;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Artwork getArtwork() { return artwork; }
    public void setArtwork(Artwork artwork) { this.artwork = artwork; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getOrderedAt() { return orderedAt; }
    public void setOrderedAt(LocalDateTime orderedAt) { this.orderedAt = orderedAt; }
}
