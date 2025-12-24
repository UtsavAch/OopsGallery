package com.utsav.arts.models;

import jakarta.persistence.*;

@Entity
@Table(name = "artworks")
public class Artwork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    private String description;

    private String category;

    private String label;

    private int price;

    private int likes;

    private String imgUrl;

    // Link to User entity
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    // Default constructor required by JPA
    public Artwork() {}

    // Constructor for convenience
    public Artwork(int id, String title, String description, String category, String label, int price, int likes, String imgUrl, User owner) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.label = label;
        this.price = price;
        this.likes = likes;
        this.imgUrl = imgUrl;
        this.owner = owner;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public int getLikes() { return likes; }
    public void setLikes(int likes) { this.likes = likes; }

    public String getImgUrl() { return imgUrl; }
    public void setImgUrl(String imgUrl) { this.imgUrl = imgUrl; }

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }
}
