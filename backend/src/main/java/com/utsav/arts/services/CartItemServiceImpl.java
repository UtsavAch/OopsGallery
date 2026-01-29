package com.utsav.arts.services;

import com.utsav.arts.models.Cart;
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
    private final CartService cartService;

    public CartItemServiceImpl(CartItemRepository cartItemRepository,
                               CartService cartService) {
        this.cartItemRepository = cartItemRepository;
        this.cartService = cartService;
    }

    @Override
    public CartItem save(CartItem cartItem) {
        // Check if same artwork already exists in cart
        Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndArtworkId(
                cartItem.getCart().getId(),
                cartItem.getArtwork().getId()
        );

        CartItem result;
        if (existingItem.isPresent()) {
            // If exists, increase quantity
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + cartItem.getQuantity());
            result = cartItemRepository.update(item);
        } else {
            result = cartItemRepository.save(cartItem);
        }

        // Recalculate and save cart totals automatically
        Cart cart = cartItem.getCart();
        cartService.save(cart);

        return result;
    }

    @Override
    public CartItem update(CartItem cartItem) {
        // Ensure item exists
        cartItemRepository.findById(cartItem.getId())
                .orElseThrow(() -> new IllegalArgumentException("CartItem not found"));

        CartItem updatedItem = cartItemRepository.update(cartItem);

        // Update cart totals automatically
        Cart cart = cartItem.getCart();
        cartService.save(cart);

        return updatedItem;
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
        CartItem item = cartItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("CartItem not found"));

        Cart cart = item.getCart();

        cartItemRepository.deleteById(id);

        // Update cart totals automatically
        cartService.save(cart);
    }

    @Override
    public void deleteByCartId(int cartId) {
        Cart cart = cartService.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        cartItemRepository.deleteByCartId(cartId);

        // Update cart totals automatically
        cartService.save(cart);
    }

    public boolean isOwner(int cartItemId, int userId) {
        return cartItemRepository.findById(cartItemId)
                .map(item -> item.getCart().getUser().getId() == userId)
                .orElse(false);
    }
}
