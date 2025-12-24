package com.utsav.arts.repository;

import com.utsav.arts.models.Orders;

import java.util.List;
import java.util.Optional;

public interface OrdersRepository {

    Orders save(Orders order);

    Orders update(Orders order);

    Optional<Orders> findById(int id);

    List<Orders> findAll();

    List<Orders> findByUserId(int userId);

    List<Orders> findByArtworkId(int artworkId);

    List<Orders> findByStatus(String status);

    void deleteById(int id);
}
