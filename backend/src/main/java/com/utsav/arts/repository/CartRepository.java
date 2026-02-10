package com.utsav.arts.repository;

import com.utsav.arts.models.Cart;

import java.util.Optional;

/**
 * Repository interface for managing {@link Cart} persistence.
 * <p>
 * Provides operations for creating, retrieving, and deleting shopping carts.
 * Enforces the business constraint of one cart per user at the service layer.
 */
public interface CartRepository {

    /**
     * Saves a cart entity.
     * <p>
     * If the cart is new, it will be persisted.
     * If it already exists, it will be updated.
     *
     * @param cart the Cart entity to save
     * @return the persisted or updated Cart
     */
    Cart save(Cart cart);

    /**
     * Finds a cart by its unique identifier.
     *
     * @param id the cart ID
     * @return an Optional containing the Cart if found, otherwise empty
     */
    Optional<Cart> findById(int id);

    /**
     * Finds the cart belonging to a specific user.
     * <p>
     * A user is expected to have at most one active cart.
     *
     * @param userId the user ID
     * @return an Optional containing the Cart if found, otherwise empty
     */
    Optional<Cart> findByUserId(int userId);

    /**
     * Deletes a cart by its unique identifier.
     *
     * @param id the cart ID
     */
    void deleteById(int id);
}
