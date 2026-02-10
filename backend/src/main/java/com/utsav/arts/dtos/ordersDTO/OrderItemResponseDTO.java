package com.utsav.arts.dtos.ordersDTO;

import java.math.BigDecimal;

/**
 * Data Transfer Object representing a single item within an order.
 *
 * <p><strong>Fields:</strong>
 * <ul>
 *   <li>id – unique identifier of the order item</li>
 *   <li>artworkId – ID of the purchased artwork</li>
 *   <li>artworkTitle – title of the artwork</li>
 *   <li>artworkImgUrl – image URL of the artwork</li>
 *   <li>quantity – quantity purchased</li>
 *   <li>priceAtPurchase – price of the artwork at the time of purchase</li>
 * </ul>
 * </p>
 */
public class OrderItemResponseDTO {
    private int id;
    private int artworkId;
    private String artworkTitle;
    private String artworkImgUrl;
    private int quantity;
    private BigDecimal priceAtPurchase;

    public OrderItemResponseDTO() {}

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getArtworkId() { return artworkId; }
    public void setArtworkId(int artworkId) { this.artworkId = artworkId; }

    public String getArtworkTitle() { return artworkTitle; }
    public void setArtworkTitle(String artworkTitle) { this.artworkTitle = artworkTitle; }

    public String getArtworkImgUrl() { return artworkImgUrl; }
    public void setArtworkImgUrl(String artworkImgUrl) { this.artworkImgUrl = artworkImgUrl; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getPriceAtPurchase() { return priceAtPurchase; }
    public void setPriceAtPurchase(BigDecimal priceAtPurchase) { this.priceAtPurchase = priceAtPurchase; }
}