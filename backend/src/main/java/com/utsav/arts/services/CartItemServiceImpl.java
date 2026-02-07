package com.utsav.arts.services;

import com.utsav.arts.exceptions.InvalidRequestException;
import com.utsav.arts.exceptions.ResourceNotFoundException;
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
    public CartItem save(CartItem cartItem, Cart cart) {
        validateQuantity(cartItem.getQuantity());

        if (cartItem.getQuantity() == 0) {
            throw new InvalidRequestException("Quantity must be at least 1 when adding to cart");
        }

        CartItem result = cartItemRepository
                .findByCartIdAndArtworkId(cart.getId(), cartItem.getArtwork().getId())
                .map(existingItem -> {
                    existingItem.setQuantity(existingItem.getQuantity() + cartItem.getQuantity());
                    return cartItemRepository.update(existingItem);
                })
                .orElseGet(() -> {
                    cartItem.setCart(cart);
                    cart.getItems().add(cartItem); // keep cart.items in sync
                    return cartItemRepository.save(cartItem);
                });

        recalculateCart(cart);
        return result;
    }

    @Override
    public CartItem update(CartItem cartItem, Cart cart) {
        CartItem existingItem = cartItemRepository.findById(cartItem.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItem.getId()));

        int qty = cartItem.getQuantity();
        validateQuantity(qty);

        if (qty == 0) {
            cart.getItems().remove(existingItem); // keep cart.items in sync
            cartItemRepository.deleteById(existingItem.getId());
            recalculateCart(cart);
            return null;
        }

        existingItem.setQuantity(qty);
        CartItem updatedItem = cartItemRepository.update(existingItem);
        recalculateCart(cart);
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
                .orElseThrow(() -> new ResourceNotFoundException("Cannot delete: Cart item not found with id: " + id));

        Cart cart = item.getCart();
        cart.getItems().remove(item); // sync list
        cartItemRepository.deleteById(id);

        recalculateCart(cart);
    }

    @Override
    public void deleteByCartId(int cartId) {
        Cart cart = cartService.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot clear items: Cart not found with id: " + cartId));

        cart.getItems().clear(); // clear items in memory
        cartItemRepository.deleteByCartId(cartId);
        recalculateCart(cart);
    }

    @Override
    public void decreaseQuantity(int cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));

        Cart cart = item.getCart();
        int newQty = item.getQuantity() - 1;

        if (newQty <= 0) {
            cart.getItems().remove(item);
            cartItemRepository.deleteById(cartItemId);
        } else {
            item.setQuantity(newQty);
            cartItemRepository.update(item);
        }

        recalculateCart(cart);
    }

    @Override
    public void increaseQuantity(int cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));

        Cart cart = item.getCart();
        item.setQuantity(item.getQuantity() + 1);
        cartItemRepository.update(item);

        recalculateCart(cart);
    }

    public boolean isOwner(int cartItemId, int userId) {
        return cartItemRepository.findById(cartItemId)
                .map(item -> item.getCart().getUser().getId() == userId)
                .orElse(false);
    }

    // HELPER METHODS

    private void validateQuantity(int quantity) {
        if (quantity < 0) {
            throw new InvalidRequestException("Quantity cannot be negative");
        }
    }

    private void recalculateCart(Cart cart) {
        cart.recalculateTotals();
        cartService.save(cart);
    }
}