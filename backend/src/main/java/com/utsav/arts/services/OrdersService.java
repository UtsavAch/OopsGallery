package com.utsav.arts.services;

import com.utsav.arts.models.Orders;

import java.util.List;
import java.util.Optional;

public interface OrdersService {

    Orders save(Orders order);

    Orders update(Orders order);

    Optional<Orders> findById(int id);

    List<Orders> findAll();

    List<Orders> findByUserId(int userId);

    List<Orders> findByArtworkId(int artworkId);

    List<Orders> findByStatus(String status);

    void deleteById(int id);
}
