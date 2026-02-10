package com.utsav.arts.services;

import com.utsav.arts.exceptions.InvalidRequestException;
import com.utsav.arts.exceptions.ResourceNotFoundException;
import com.utsav.arts.models.OrderStatus;
import com.utsav.arts.models.Orders;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing customer orders.
 * Handles creating, updating, retrieving, and deleting orders.
 */
public interface OrdersService {

    /**
     * Places a new order for a user.
     * <p>
     * - Converts the user's cart items into order items.
     * - Calculates the total price.
     * - Sets the order status to PENDING.
     *
     * @param userId  ID of the user placing the order
     * @param address Shipping address for the order
     * @return The created order
     * @throws ResourceNotFoundException if the user or their cart does not exist
     * @throws InvalidRequestException   if the user's cart is empty
     */
    Orders placeOrder(int userId, String address);

    /**
     * Confirms an order, changing its status to CONFIRMED.
     *
     * @param orderId ID of the order to confirm
     * @return The updated order
     * @throws ResourceNotFoundException if the order does not exist
     * @throws InvalidRequestException   if the order cannot transition to CONFIRMED
     */
    Orders confirmOrder(int orderId);

    /**
     * Marks an order as shipped.
     *
     * @param orderId ID of the order to ship
     * @return The updated order
     * @throws ResourceNotFoundException if the order does not exist
     * @throws InvalidRequestException   if the order cannot transition to SHIPPED
     */
    Orders shipOrder(int orderId);

    /**
     * Marks an order as delivered.
     *
     * @param orderId ID of the order to deliver
     * @return The updated order
     * @throws ResourceNotFoundException if the order does not exist
     * @throws InvalidRequestException   if the order cannot transition to DELIVERED
     */
    Orders deliverOrder(int orderId);

    /**
     * Cancels an order.
     *
     * @param orderId ID of the order to cancel
     * @return The updated order
     * @throws ResourceNotFoundException if the order does not exist
     * @throws InvalidRequestException   if the order cannot transition to CANCELLED
     */
    Orders cancelOrder(int orderId);

    /**
     * Finds an order by its ID.
     *
     * @param id The order ID
     * @return Optional containing the order if found, otherwise empty
     */
    Optional<Orders> findById(int id);

    /**
     * Returns all orders.
     *
     * @return List of all orders
     */
    List<Orders> findAll();

    /**
     * Returns all orders for a specific user.
     *
     * @param userId The user ID
     * @return List of orders belonging to the user
     */
    List<Orders> findByUserId(int userId);

    /**
     * Returns all orders with a specific status.
     *
     * @param status The order status to filter by
     * @return List of orders with the given status
     */
    List<Orders> findByStatus(OrderStatus status);

    /**
     * Deletes an order by its ID.
     *
     * @param id ID of the order to delete
     * @throws ResourceNotFoundException if the order does not exist
     */
    void deleteById(int id);

    /**
     * Checks if a user is the owner of a given order.
     *
     * @param orderId The order ID
     * @param userId  The user ID
     * @return true if the user owns the order, false otherwise
     */
    boolean isOwner(int orderId, int userId);
}
