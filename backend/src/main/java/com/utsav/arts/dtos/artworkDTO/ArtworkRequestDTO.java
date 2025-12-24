package com.utsav.arts.dtos.artworkDTO;

public class ArtworkRequestDTO {

    private String title;
    private String description;
    private String category;
    private String label;
    private int price;
    private String imgUrl;
    private int ownerId;

    public ArtworkRequestDTO() {}

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

    public String getImgUrl() { return imgUrl; }
    public void setImgUrl(String imgUrl) { this.imgUrl = imgUrl; }

    public int getOwnerId() { return ownerId; }
    public void setOwnerId(int ownerId) { this.ownerId = ownerId; }
}
