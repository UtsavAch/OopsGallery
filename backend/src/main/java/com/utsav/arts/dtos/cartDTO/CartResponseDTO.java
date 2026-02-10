package com.utsav.arts.dtos.cartDTO;

import com.utsav.arts.dtos.cartItemDTO.CartItemResponseDTO;
import java.math.BigDecimal;
import java.util.List;

/**
 * Data Transfer Object used to send cart data to clients.
 *
 * <p>Represents a cart including its items, totals, and ownership information.</p>
 *
 * <p><strong>Fields:</strong>
 * <ul>
 *   <li>id – cart identifier</li>
 *   <li>userId – ID of the user who owns the cart</li>
 *   <li>cartItems – list of items in the cart</li>
 *   <li>totalItems – total number of items in the cart</li>
 *   <li>totalPrice – total price of all items in the cart</li>
 * </ul>
 * </p>
 */
public class CartResponseDTO {

    private int id;
    private int userId;
    private List<CartItemResponseDTO> cartItems;
    private int totalItems;
    private BigDecimal totalPrice;

    public CartResponseDTO() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public List<CartItemResponseDTO> getCartItems() { return cartItems; }
    public void setCartItems(List<CartItemResponseDTO> cartItems) {
        this.cartItems = cartItems;
    }

    public int getTotalItems() { return totalItems; }
    public void setTotalItems(int totalItems) { this.totalItems = totalItems; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
