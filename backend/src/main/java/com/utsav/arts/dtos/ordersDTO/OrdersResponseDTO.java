package com.utsav.arts.dtos.ordersDTO;

import java.time.LocalDateTime;

public class OrdersResponseDTO {

    private int id;
    private int userId;
    private int artworkId;
    private int price;
    private String address;
    private String status;
    private LocalDateTime orderedAt;

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getArtworkId() { return artworkId; }
    public void setArtworkId(int artworkId) { this.artworkId = artworkId; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getOrderedAt() { return orderedAt; }
    public void setOrderedAt(LocalDateTime orderedAt) { this.orderedAt = orderedAt; }
}
