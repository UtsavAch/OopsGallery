package com.utsav.arts.dtos.ordersDTO;

public class OrdersRequestDTO {

    private int userId;
    private int artworkId;
    private int price;
    private String address;
    private String status;

    // Getters and setters
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
}
