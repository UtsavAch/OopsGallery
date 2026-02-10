package com.utsav.arts.services;

import com.utsav.arts.exceptions.ResourceNotFoundException;
import com.utsav.arts.models.Payment;
import com.utsav.arts.models.PaymentStatus;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing payments.
 * Handles creating, retrieving, filtering, and deleting payments.
 */
public interface PaymentService {

    /**
     * Saves a payment.
     * <p>
     * - If createdAt is null, it sets the current timestamp.
     * - If status is null, it sets the status to PENDING.
     * - If the payment is saved with SUCCESS status, it confirms the related order.
     *
     * @param payment The payment to save
     * @return The saved payment
     */
    Payment save(Payment payment);

    /**
     * Finds a payment by its ID.
     *
     * @param id Payment ID
     * @return Optional containing the payment if found, otherwise empty
     */
    Optional<Payment> findById(int id);

    /**
     * Finds a payment by its transaction ID.
     *
     * @param transactionId The transaction ID
     * @return Optional containing the payment if found, otherwise empty
     */
    Optional<Payment> findByTransactionId(String transactionId);

    /**
     * Returns all payments for a specific user.
     *
     * @param userId User ID
     * @return List of payments made by the user
     */
    List<Payment> findByUserId(int userId);

    /**
     * Returns all payments for a specific order.
     *
     * @param orderId Order ID
     * @return List of payments associated with the order
     */
    List<Payment> findByOrderId(int orderId);

    /**
     * Returns all payments with a specific status.
     *
     * @param status Payment status
     * @return List of payments with the given status
     */
    List<Payment> findByStatus(PaymentStatus status);

    /**
     * Returns all payments in the system.
     *
     * @return List of all payments
     */
    List<Payment> findAll();

    /**
     * Deletes a payment by its ID.
     *
     * @param id Payment ID
     * @throws ResourceNotFoundException if payment does not exist
     */
    void deleteById(int id);

    /**
     * Checks if a payment belongs to a specific user.
     *
     * @param paymentId Payment ID
     * @param userId    User ID
     * @return true if the payment belongs to the user, false otherwise
     */
    boolean isPaymentOwner(int paymentId, int userId);

}
