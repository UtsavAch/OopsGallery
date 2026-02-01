package com.utsav.arts.services;

import com.utsav.arts.models.OrderStatus;
import com.utsav.arts.models.Orders;

import java.util.List;
import java.util.Optional;

public interface OrdersService {

    Orders placeOrder(int userId, String address);

    Orders confirmOrder(int orderId);

    Orders shipOrder(int orderId);

    Orders deliverOrder(int orderId);

    Orders cancelOrder(int orderId);

    Optional<Orders> findById(int id);

    List<Orders> findAll();

    List<Orders> findByUserId(int userId);

    List<Orders> findByStatus(OrderStatus status);

    void deleteById(int id);

    boolean isOwner(int orderId, int userId);
}
