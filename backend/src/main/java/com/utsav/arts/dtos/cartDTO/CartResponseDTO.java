package com.utsav.arts.dtos.cartDTO;

import java.util.List;

public class CartResponseDTO {

    private int id;
    private int userId;
    private List<Integer> cartItemIds;

    public CartResponseDTO() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public List<Integer> getCartItemIds() { return cartItemIds; }
    public void setCartItemIds(List<Integer> cartItemIds) {
        this.cartItemIds = cartItemIds;
    }
}
