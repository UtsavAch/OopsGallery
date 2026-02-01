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
    public CartItem save(CartItem cartItem) {
        validateQuantity(cartItem.getQuantity());

        // Use InvalidRequestException for business logic violations (400 Bad Request)
        if (cartItem.getQuantity() == 0) {
            throw new InvalidRequestException("Quantity must be at least 1 when adding to cart");
        }

        Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndArtworkId(
                cartItem.getCart().getId(),
                cartItem.getArtwork().getId()
        );

        CartItem result;
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + cartItem.getQuantity());
            result = cartItemRepository.update(item);
        } else {
            result = cartItemRepository.save(cartItem);
        }

        cartService.save(cartItem.getCart());
        return result;
    }

    @Override
    public CartItem update(CartItem cartItem) {
        // Use ResourceNotFoundException for 404
        CartItem existingItem = cartItemRepository.findById(cartItem.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItem.getId()));

        int qty = cartItem.getQuantity();
        validateQuantity(qty);

        if (qty == 0) {
            cartItemRepository.deleteById(existingItem.getId());
            cartService.save(existingItem.getCart());
            return null;
        }

        existingItem.setQuantity(qty);
        CartItem updatedItem = cartItemRepository.update(existingItem);
        cartService.save(existingItem.getCart());

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
        cartItemRepository.deleteById(id);
        cartService.save(cart);
    }

    @Override
    public void deleteByCartId(int cartId) {
        Cart cart = cartService.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot clear items: Cart not found with id: " + cartId));

        cartItemRepository.deleteByCartId(cartId);
        cartService.save(cart);
    }

    @Override
    public void decreaseQuantity(int cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));

        int newQty = item.getQuantity() - 1;
        validateQuantity(newQty);

        if (newQty == 0) {
            cartItemRepository.deleteById(cartItemId);
        } else {
            item.setQuantity(newQty);
            cartItemRepository.update(item);
        }

        cartService.save(item.getCart());
    }

    @Override
    public void increaseQuantity(int cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));

        int newQty = item.getQuantity() + 1;
        validateQuantity(newQty);

        item.setQuantity(newQty);
        cartItemRepository.update(item);

        cartService.save(item.getCart());
    }

    public boolean isOwner(int cartItemId, int userId) {
        return cartItemRepository.findById(cartItemId)
                .map(item -> item.getCart().getUser().getId() == userId)
                .orElse(false);
    }

    private void validateQuantity(int quantity) {
        if (quantity < 0) {
            throw new InvalidRequestException("Quantity cannot be negative");
        }
    }
}