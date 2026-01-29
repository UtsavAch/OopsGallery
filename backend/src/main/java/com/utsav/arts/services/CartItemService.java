package com.utsav.arts.services;

import com.utsav.arts.models.CartItem;

import java.util.List;
import java.util.Optional;

public interface CartItemService {

    CartItem save(CartItem cartItem);

    CartItem update(CartItem cartItem);

    Optional<CartItem> findById(int id);

    List<CartItem> findByCartId(int cartId);

    Optional<CartItem> findByCartIdAndArtworkId(int cartId, int artworkId);

    void deleteById(int id);

    void deleteByCartId(int cartId);

    boolean isOwner(int cartItemId, int userId);

    void decreaseQuantity(int cartItemId);

    void increaseQuantity(int cartItemId);
}
