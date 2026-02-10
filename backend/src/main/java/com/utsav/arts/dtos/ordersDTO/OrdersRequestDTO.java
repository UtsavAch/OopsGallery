package com.utsav.arts.dtos.ordersDTO;

import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object used to capture an order request from the client.
 *
 * <p><strong>Fields:</strong>
 * <ul>
 *   <li>address – delivery address for the order</li>
 * </ul>
 * </p>
 */
public class OrdersRequestDTO {

    @NotBlank(message = "Delivery address is required")
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
