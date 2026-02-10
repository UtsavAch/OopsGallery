package com.utsav.arts.services;

import com.utsav.arts.exceptions.ResourceAlreadyExistsException;
import com.utsav.arts.exceptions.ResourceNotFoundException;
import com.utsav.arts.models.Cart;

import java.util.Optional;

/**
 * Service interface for managing shopping carts.
 * Provides methods to create, retrieve, delete, and verify ownership of carts.
 */
public interface CartService {

    /**
     * Saves a cart.
     * <p>
     * - If the cart is new (id = 0), it will create a new cart.
     * - If the cart already exists, it will update it.
     * - Before creating a new cart, it ensures the user does not already have one.
     *
     * @param cart The cart to save or update
     * @return The saved Cart entity
     * @throws ResourceAlreadyExistsException if the user already has a cart
     * @throws ResourceNotFoundException      if attempting to update a cart that does not exist
     */
    Cart save(Cart cart);

    /**
     * Finds a cart by its ID.
     *
     * @param id The ID of the cart
     * @return Optional containing the Cart if found, otherwise empty
     */
    Optional<Cart> findById(int id);

    /**
     * Finds a cart belonging to a specific user.
     *
     * @param userId The ID of the user
     * @return Optional containing the Cart if found, otherwise empty
     */
    Optional<Cart> findByUserId(int userId);

    /**
     * Deletes a cart by its ID.
     *
     * @param id The ID of the cart to delete
     * @throws ResourceNotFoundException if the cart does not exist
     */
    void deleteById(int id);

    /**
     * Checks if a user is the owner of a cart.
     *
     * @param cartId The ID of the cart
     * @param userId The ID of the user
     * @return true if the user owns the cart, false otherwise
     */
    boolean isOwner(int cartId, int userId);
}
