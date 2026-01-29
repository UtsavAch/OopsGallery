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
        if (cart.getId() == 0) {
            // New cart
            cartRepository.findByUserId(cart.getUser().getId())
                    .ifPresent(existing -> {
                        throw new IllegalArgumentException("User already has a cart");
                    });
        } // Existing cart
        cart.recalculateTotals();
        return cartRepository.save(cart);
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

    public boolean isOwner(int cartId, int userId) {
        return cartRepository.findById(cartId)
                .map(cart -> cart.getUser().getId() == userId)
                .orElse(false);
    }
}
