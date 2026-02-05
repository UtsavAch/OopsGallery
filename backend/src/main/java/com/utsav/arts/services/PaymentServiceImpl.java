package com.utsav.arts.services;

import com.utsav.arts.exceptions.InvalidRequestException;
import com.utsav.arts.exceptions.ResourceNotFoundException;
import com.utsav.arts.models.Orders; // Import Orders
import com.utsav.arts.models.OrderStatus; // Import OrderStatus
import com.utsav.arts.models.Payment;
import com.utsav.arts.models.PaymentStatus;
import com.utsav.arts.repository.OrdersRepository; // Import Repo
import com.utsav.arts.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service("paymentService")
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrdersRepository ordersRepository; // Inject OrdersRepository

    // Update Constructor
    public PaymentServiceImpl(PaymentRepository paymentRepository, OrdersRepository ordersRepository) {
        this.paymentRepository = paymentRepository;
        this.ordersRepository = ordersRepository;
    }

    @Override
    public Payment save(Payment payment) {
        if (payment.getCreatedAt() == null) {
            payment.setCreatedAt(LocalDateTime.now());
        }
        if (payment.getStatus() == null) {
            payment.setStatus(PaymentStatus.PENDING);
        }

        // EDGE CASE: If a payment is created directly as SUCCESS (rare, but possible)
        Payment saved = paymentRepository.save(payment);
        if (saved.getStatus() == PaymentStatus.SUCCESS) {
            confirmOrderInternal(saved.getOrder());
        }
        return saved;
    }

    // When Payment is Success -> Order is Confirmed
    public Payment markSuccess(int paymentId) {
        Payment payment = updateStatusInternal(paymentId, PaymentStatus.SUCCESS);

        // Automatically confirm the order
        confirmOrderInternal(payment.getOrder());

        return payment;
    }

    // When Payment is Refunded -> Order is Canceled (Optional, depends on policy)
    public Payment refund(int paymentId) {
        Payment payment = updateStatusInternal(paymentId, PaymentStatus.REFUNDED);

        // Specific logic to cancel order if refunded
        cancelOrderInternal(payment.getOrder());

        return payment;
    }

    public Payment markFailed(int paymentId) {
        return updateStatusInternal(paymentId, PaymentStatus.FAILED);
    }

    public Payment cancel(int paymentId) {
        return updateStatusInternal(paymentId, PaymentStatus.CANCELLED);
    }

    @Override
    public Optional<Payment> findById(int id) {
        return paymentRepository.findById(id);
    }

    @Override
    public Optional<Payment> findByTransactionId(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId);
    }

    @Override
    public List<Payment> findByUserId(int userId) {
        return paymentRepository.findByUserId(userId);
    }

    @Override
    public List<Payment> findByOrderId(int orderId) {
        return paymentRepository.findByOrderId(orderId);
    }

    @Override
    public List<Payment> findByStatus(PaymentStatus status) {
        return paymentRepository.findByStatus(status);
    }

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    @Override
    public void deleteById(int id) {
        if (paymentRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Cannot delete: Payment not found with id: " + id);
        }
        paymentRepository.deleteById(id);
    }

    @Override
    public boolean isPaymentOwner(int paymentId, int userId) {
        return findById(paymentId)
                .map(payment -> payment.getUser().getId() == userId)
                .orElse(false);
    }


    // --- Helper Methods to update Orders via Repository (Prevents Circular Dependency) ---
    private void confirmOrderInternal(Orders order) {
        // Only confirm if it's currently PENDING.
        // If it's already SHIPPED or DELIVERED, we don't want to mess it up.
        if (order.getStatus() == OrderStatus.PENDING) {
            order.setStatus(OrderStatus.CONFIRMED);
            ordersRepository.save(order); // Save the order status change
        }
    }

    private Payment updateStatusInternal(int id, PaymentStatus next) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        if (!payment.getStatus().canTransitionTo(next)) {
            throw new InvalidRequestException(
                    "Invalid payment status transition: " +
                            payment.getStatus() + " -> " + next
            );
        }

        payment.setStatus(next);
        return paymentRepository.update(payment);
    }

    private void cancelOrderInternal(Orders order) {
        // Allow cancellation if it hasn't been shipped yet
        if (order.getStatus() != OrderStatus.SHIPPED &&
                order.getStatus() != OrderStatus.DELIVERED &&
                order.getStatus() != OrderStatus.CANCELLED) {

            order.setStatus(OrderStatus.CANCELLED);
            ordersRepository.update(order);
        }
    }
}
