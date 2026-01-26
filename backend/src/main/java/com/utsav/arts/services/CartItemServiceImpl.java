package com.utsav.arts.services;

import com.utsav.arts.models.CartItem;
import com.utsav.arts.repository.CartItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("cartItemService")
@Transactional
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;

    public CartItemServiceImpl(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public CartItem save(CartItem cartItem) {
        // Check for existing cart item with same artwork
        Optional<CartItem> existingItem =
                cartItemRepository.findByCartIdAndArtworkId(
                        cartItem.getCart().getId(),
                        cartItem.getArtwork().getId()
                );
        if (existingItem.isPresent()) {
            // If it exists, increase quantity
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + cartItem.getQuantity());
            return cartItemRepository.update(item);
        }

        return cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem update(CartItem cartItem) {
        // Ensure the item exists before updating
        cartItemRepository.findById(cartItem.getId())
                .orElseThrow(() -> new IllegalArgumentException("CartItem not found"));

        return cartItemRepository.update(cartItem);
    }

    @Override
    public Optional<CartItem> findById(int id) {
        return cartItemRepository.findById(id);
    }

    @Override
    public List<CartItem> findByCartId(int cartId) {
        return cartItemRepository.findByCartId(cartId);
    }

    @Override
    public Optional<CartItem> findByCartIdAndArtworkId(int cartId, int artworkId) {
        return cartItemRepository.findByCartIdAndArtworkId(cartId, artworkId);
    }

    @Override
    public void deleteById(int id) {
        if (cartItemRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("CartItem not found");
        }
        cartItemRepository.deleteById(id);
    }

    @Override
    public void deleteByCartId(int cartId) {
        cartItemRepository.deleteByCartId(cartId);
    }

    public boolean isOwnerOfItem(int itemId, String userEmail) {
        return findById(itemId)
                .map(item -> item.getCart().getUser().getEmail().equals(userEmail))
                .orElse(false);
    }
}
