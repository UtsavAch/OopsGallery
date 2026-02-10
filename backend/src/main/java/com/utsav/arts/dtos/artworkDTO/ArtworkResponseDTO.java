package com.utsav.arts.dtos.artworkDTO;

import com.utsav.arts.models.ArtCategory;

import java.math.BigDecimal;

/**
 * Data Transfer Object used to send artwork data to clients.
 *
 * <p>This DTO represents the public-facing view of an artwork and is used
 * in API responses. It excludes internal or persistence-related fields.</p>
 *
 * <p><strong>Fields:</strong>
 * <ul>
 *   <li>id – artwork identifier</li>
 *   <li>title – artwork title</li>
 *   <li>description – artwork description</li>
 *   <li>category – artwork category</li>
 *   <li>label – optional label or tag</li>
 *   <li>price – artwork price</li>
 *   <li>imgUrl – public image URL</li>
 * </ul>
 * </p>
 */
public class ArtworkResponseDTO {

    private int id;
    private String title;
    private String description;
    private ArtCategory category;
    private String label;
    private BigDecimal price;
    private String imgUrl;

    public ArtworkResponseDTO() {}

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
