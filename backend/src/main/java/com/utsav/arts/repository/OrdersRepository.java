package com.utsav.arts.repository;

import com.utsav.arts.models.OrderStatus;
import com.utsav.arts.models.Orders;

import java.util.List;
import java.util.Optional;

public interface OrdersRepository {

    Orders save(Orders order);

    Orders update(Orders order);

    Optional<Orders> findById(int id);

    List<Orders> findAll();

    List<Orders> findByUserId(int userId);

    // Useful for: "Show me all orders that contain this specific artwork"
    List<Orders> findByArtworkId(int artworkId);

    // Updated to use strict Enum type
    List<Orders> findByStatus(OrderStatus status);

    void deleteById(int id);
}