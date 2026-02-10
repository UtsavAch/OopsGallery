package com.utsav.arts.repository;

import com.utsav.arts.models.OrderStatus;
import com.utsav.arts.models.Orders;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Orders} persistence.
 * <p>
 * Provides data access methods for retrieving and managing orders,
 * including user-based, artwork-based, and status-based queries.
 */
public interface OrdersRepository {

    /**
     * Saves a new order or updates an existing one.
     *
     * @param order the Orders entity to persist
     * @return the persisted Orders entity
     */
    Orders save(Orders order);

    /**
     * Updates an existing order.
     *
     * @param order the Orders entity to update
     * @return the updated Orders entity
     */
    Orders update(Orders order);

    /**
     * Finds an order by its unique identifier.
     *
     * @param id the order ID
     * @return an Optional containing the Orders if found, otherwise empty
     */
    Optional<Orders> findById(int id);

    /**
     * Retrieves all orders in the system.
     *
     * @return a list of all Orders, typically ordered by creation date
     */
    List<Orders> findAll();

    /**
     * Retrieves all orders placed by a specific user.
     *
     * @param userId the user ID
     * @return a list of Orders belonging to the user
     */
    List<Orders> findByUserId(int userId);

    /**
     * Retrieves all orders that contain a specific artwork.
     * <p>
     * Useful for analytics, reporting, or determining artwork sales history.
     *
     * @param artworkId the artwork ID
     * @return a list of Orders containing the artwork
     */
    List<Orders> findByArtworkId(int artworkId);

    /**
     * Retrieves all orders with the specified status.
     *
     * @param status the OrderStatus to filter by
     * @return a list of Orders matching the given status
     */
    List<Orders> findByStatus(OrderStatus status);

    /**
     * Deletes an order by its unique identifier.
     *
     * @param id the order ID
     */
    void deleteById(int id);
}