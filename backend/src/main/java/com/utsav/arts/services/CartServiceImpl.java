package com.utsav.arts.services;

import com.utsav.arts.models.Cart;
import com.utsav.arts.repository.CartRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("cartService")
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public Cart save(Cart cart) {
        // Ensure user has only one cart
        cartRepository.findByUserId(cart.getUser().getId())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("User already has a cart");
                });

        return cartRepository.save(cart);
    }

    @Override
    public Cart update(Cart cart) {
        cartRepository.findById(cart.getId())
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        return cartRepository.update(cart);
    }

    @Override
    public Optional<Cart> findById(int id) {
        return cartRepository.findById(id);
    }

    @Override
    public Optional<Cart> findByUserId(int userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    public void deleteById(int id) {
        if (cartRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Cart not found");
        }
        cartRepository.deleteById(id);
    }
}
