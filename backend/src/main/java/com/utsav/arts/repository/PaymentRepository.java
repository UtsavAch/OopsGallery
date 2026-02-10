package com.utsav.arts.repository;

import com.utsav.arts.models.Payment;
import com.utsav.arts.models.PaymentStatus;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Payment} entities.
 *
 * <p>
 * Defines CRUD operations and common query methods related to payments,
 * including lookups by transaction, user, order, and payment status.
 * </p>
 */
public interface PaymentRepository {

    /**
     * Persists a new payment entity.
     *
     * @param payment the payment to be saved
     * @return the persisted payment
     */
    Payment save(Payment payment);

    /**
     * Updates an existing payment entity.
     *
     * @param payment the payment to update
     * @return the updated payment
     */
    Payment update(Payment payment);

    /**
     * Finds a payment by its unique identifier.
     *
     * @param id the payment ID
     * @return an {@link Optional} containing the payment if found
     */
    Optional<Payment> findById(int id);

    /**
     * Finds a payment using its external transaction ID
     * (e.g., Stripe PaymentIntent ID).
     *
     * @param transactionId the external transaction identifier
     * @return an {@link Optional} containing the payment if found
     */
    Optional<Payment> findByTransactionId(String transactionId);

    /**
     * Retrieves all payments made by a specific user.
     *
     * @param userId the user ID
     * @return a list of payments associated with the user
     */
    List<Payment> findByUserId(int userId);

    /**
     * Retrieves all payments associated with a specific order.
     *
     * @param orderId the order ID
     * @return a list of payments for the given order
     */
    List<Payment> findByOrderId(int orderId);

    /**
     * Retrieves all payments with a given payment status.
     *
     * @param status the payment status
     * @return a list of payments matching the status
     */
    List<Payment> findByStatus(PaymentStatus status);

    /**
     * Retrieves all payments in the system.
     *
     * @return a list of all payments
     */
    List<Payment> findAll();

    /**
     * Deletes a payment by its ID.
     *
     * @param id the payment ID
     */
    void deleteById(int id);
}
