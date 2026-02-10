package com.utsav.arts.services;

import com.utsav.arts.exceptions.InvalidRequestException;
import com.utsav.arts.exceptions.ResourceNotFoundException;
import com.utsav.arts.models.Cart;
import com.utsav.arts.models.CartItem;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing cart items.
 * Provides methods to add, update, retrieve, and delete items in a shopping cart.
 */
public interface CartItemService {

    /**
     * Adds a new cart item to the given cart.
     * If the item already exists, its quantity is increased.
     *
     * @param cartItem The cart item to add
     * @param cart     The cart to which the item should be added
     * @return The saved or updated CartItem
     * @throws InvalidRequestException if the quantity is zero or negative
     */
    CartItem save(CartItem cartItem, Cart cart);

    /**
     * Updates the quantity of an existing cart item.
     * If the quantity becomes zero, the item is removed from the cart.
     *
     * @param cartItem The cart item with updated quantity
     * @param cart     The cart containing the item
     * @return The updated CartItem, or null if it was removed
     * @throws ResourceNotFoundException if the cart item does not exist
     */
    CartItem update(CartItem cartItem, Cart cart);

    /**
     * Finds a cart item by its ID.
     *
     * @param id The ID of the cart item
     * @return Optional containing the CartItem if found, otherwise empty
     */
    Optional<CartItem> findById(int id);

    /**
     * Retrieves all items in a specific cart.
     *
     * @param cartId The ID of the cart
     * @return List of CartItems in the cart
     */
    List<CartItem> findByCartId(int cartId);

    /**
     * Finds a specific cart item in a cart by artwork ID.
     *
     * @param cartId    The cart ID
     * @param artworkId The artwork ID
     * @return Optional containing the CartItem if found
     */
    Optional<CartItem> findByCartIdAndArtworkId(int cartId, int artworkId);

    /**
     * Deletes a cart item by its ID.
     *
     * @param id The ID of the cart item to delete
     * @throws ResourceNotFoundException if the cart item does not exist
     */
    void deleteById(int id);

    /**
     * Deletes all items in a specific cart.
     *
     * @param cartId The ID of the cart to clear
     * @throws ResourceNotFoundException if the cart does not exist
     */
    void deleteByCartId(int cartId);

    /**
     * Checks if a specific user is the owner of the cart item.
     *
     * @param cartItemId The ID of the cart item
     * @param userId     The ID of the user
     * @return true if the user owns the item, false otherwise
     */
    boolean isOwner(int cartItemId, int userId);

    /**
     * Decreases the quantity of a cart item by 1.
     * If quantity becomes zero, the item is removed.
     *
     * @param cartItemId The ID of the cart item
     * @throws ResourceNotFoundException if the cart item does not exist
     */
    void decreaseQuantity(int cartItemId);

    /**
     * Increases the quantity of a cart item by 1.
     *
     * @param cartItemId The ID of the cart item
     * @throws ResourceNotFoundException if the cart item does not exist
     */
    void increaseQuantity(int cartItemId);
}
