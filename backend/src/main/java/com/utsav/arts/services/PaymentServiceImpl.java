package com.utsav.arts.services;

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
    private final OrdersRepository ordersRepository;
    private final CartService cartService;
    private final CartItemService cartItemService;


    // Update Constructor
    public PaymentServiceImpl(PaymentRepository paymentRepository, OrdersRepository ordersRepository, CartService cartService, CartItemService cartItemService) {
        this.cartService = cartService;
        this.cartItemService = cartItemService;
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


    private void confirmOrderInternal(Orders order) {
        if (order.getStatus() != OrderStatus.PENDING) return;

        // Confirm the order
        order.setStatus(OrderStatus.CONFIRMED);
        ordersRepository.save(order);

        // Clear the user's cart after successful payment
        cartService.findByUserId(order.getUser().getId())
                .ifPresent(cart -> cartItemService.deleteByCartId(cart.getId()));
    }

}
