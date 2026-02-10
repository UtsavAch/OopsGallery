package com.utsav.arts.repository;

import com.utsav.arts.models.CartItem;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link CartItem} persistence.
 * <p>
 * Provides CRUD operations and custom queries for cart items,
 * including lookups by cart and artwork identifiers.
 */
public interface CartItemRepository {

    /**
     * Persists a new cart item in the database.
     *
     * @param cartItem the CartItem entity to save
     * @return the persisted CartItem
     */
    CartItem save(CartItem cartItem);

    /**
     * Updates an existing cart item.
     *
     * @param cartItem the CartItem entity with updated values
     * @return the updated CartItem
     */
    CartItem update(CartItem cartItem);

    /**
     * Finds a cart item by its unique identifier.
     *
     * @param id the cart item ID
     * @return an Optional containing the CartItem if found, otherwise empty
     */
    Optional<CartItem> findById(int id);

    /**
     * Retrieves all cart items belonging to a specific cart.
     *
     * @param cartId the cart ID
     * @return list of CartItem entities for the given cart
     */
    List<CartItem> findByCartId(int cartId);

    /**
     * Finds a cart item by cart ID and artwork ID.
     * <p>
     * Used to detect existing items in a cart when adding or updating quantities.
     *
     * @param cartId the cart ID
     * @param artworkId the artwork ID
     * @return Optional containing the CartItem if found, otherwise empty
     */
    Optional<CartItem> findByCartIdAndArtworkId(int cartId, int artworkId);

    /**
     * Deletes a cart item by its ID.
     *
     * @param id the cart item ID
     */
    void deleteById(int id);

    /**
     * Deletes all cart items belonging to a specific cart.
     *
     * @param cartId the cart ID
     */
    void deleteByCartId(int cartId);
}
