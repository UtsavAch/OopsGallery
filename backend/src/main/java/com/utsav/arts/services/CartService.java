package com.utsav.arts.services;

import com.utsav.arts.models.Cart;

import java.util.Optional;

public interface CartService {

    Cart save(Cart cart);

    Cart update(Cart cart);

    Optional<Cart> findById(int id);

    Optional<Cart> findByUserId(int userId);

    void deleteById(int id);
}
