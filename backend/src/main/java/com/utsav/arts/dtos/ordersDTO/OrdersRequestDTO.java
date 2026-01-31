package com.utsav.arts.dtos.ordersDTO;

import jakarta.validation.constraints.NotBlank;

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
