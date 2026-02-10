package com.utsav.arts.dtos.artworkDTO;

import com.utsav.arts.models.ArtCategory;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * Data Transfer Object used to receive artwork creation or update data from clients.
 *
 * <p>This DTO is validated at the controller layer and represents the required
 * and optional attributes needed to create or modify an artwork.</p>
 *
 * <p><strong>Fields:</strong>
 * <ul>
 *   <li>title – artwork title</li>
 *   <li>description – artwork description</li>
 *   <li>category – artwork category (enum)</li>
 *   <li>label – optional tag or label</li>
 *   <li>price – artwork price</li>
 * </ul>
 * </p>
 */
public class ArtworkRequestDTO {

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotNull(message = "Category is required")
    private ArtCategory category;

    private String label; // Optional field

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    private BigDecimal price;

    public ArtworkRequestDTO() {}

    // Getters & Setters
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
}