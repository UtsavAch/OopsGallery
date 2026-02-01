package com.utsav.arts.services;

import com.utsav.arts.exceptions.ResourceAlreadyExistsException;
import com.utsav.arts.exceptions.ResourceNotFoundException;
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
        // Checking business rule: One cart per user
        if (cart.getId() == 0) {
            // New cart being created
            cartRepository.findByUserId(cart.getUser().getId())
                    .ifPresent(existing -> {
                        // Use ResourceAlreadyExistsException for a 409 Conflict status
                        throw new ResourceAlreadyExistsException("User with ID " + cart.getUser().getId() + " already has a cart.");
                    });
        } else {
            // If updating an existing cart, verify it actually exists first
            if (cartRepository.findById(cart.getId()).isEmpty()) {
                throw new ResourceNotFoundException("Cannot update: Cart not found with ID " + cart.getId());
            }
        }

        cart.recalculateTotals();
        return cartRepository.save(cart);
    }

    @Override
    public Optional<Cart> findById(int id) {
        // Optional is returned so the Controller can throw ResourceNotFound if empty
        return cartRepository.findById(id);
    }

    @Override
    public Optional<Cart> findByUserId(int userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    public void deleteById(int id) {
        // Ensure consistent 404 behavior for deletions
        if (cartRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Cannot delete: Cart not found with id: " + id);
        }
        cartRepository.deleteById(id);
    }

    public boolean isOwner(int cartId, int userId) {
        return cartRepository.findById(cartId)
                .map(cart -> cart.getUser().getId() == userId)
                .orElse(false);
    }
}