package com.utsav.arts.dtos.cartDTO;

import java.math.BigDecimal;
import java.util.List;

public class CartResponseDTO {

    private int id;
    private int userId;
    private List<Integer> cartItems;
    private int totalItems;
    private BigDecimal totalPrice;

    public CartResponseDTO() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public List<Integer> getCartItems() { return cartItems; }
    public void setCartItems(List<Integer> cartItems) {
        this.cartItems = cartItems;
    }

    public int getTotalItems() { return totalItems; }
    public void setTotalItems(int totalItems) { this.totalItems = totalItems; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
