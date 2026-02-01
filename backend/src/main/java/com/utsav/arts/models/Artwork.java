package com.utsav.arts.models;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "artworks")
public class Artwork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ArtCategory category;

    private String label;

    private BigDecimal price;

    private String imgUrl;

    public Artwork() {}

    // Constructor for convenience
    public Artwork(int id, String title, String description, ArtCategory category, String label, BigDecimal price,String imgUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.label = label;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public ArtCategory getCategory() { return category; }
    public void setCategory(ArtCategory category) { this.category = category; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getImgUrl() { return imgUrl; }
    public void setImgUrl(String imgUrl) { this.imgUrl = imgUrl; }
}
