package com.utsav.arts.dtos.ordersDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object representing an order returned to the client.
 *
 * <p><strong>Fields:</strong>
 * <ul>
 *   <li>id – unique identifier of the order</li>
 *   <li>userId – ID of the user who placed the order</li>
 *   <li>items – list of OrderItemResponseDTO objects representing the order items</li>
 *   <li>totalPrice – total price of the order</li>
 *   <li>address – delivery address of the order</li>
 *   <li>status – current status of the order</li>
 *   <li>orderedAt – timestamp when the order was placed</li>
 * </ul>
 * </p>
 */
public class OrdersResponseDTO {

    private int id;
    private int userId;
    private List<OrderItemResponseDTO> items;
    private BigDecimal totalPrice;
    private String address;
    private String status;
    private LocalDateTime orderedAt;

    public OrdersResponseDTO() {}

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public List<OrderItemResponseDTO> getItems() { return items; }
    public void setItems(List<OrderItemResponseDTO> items) { this.items = items; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getOrderedAt() { return orderedAt; }
    public void setOrderedAt(LocalDateTime orderedAt) { this.orderedAt = orderedAt; }
}