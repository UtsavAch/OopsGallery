package com.utsav.arts.services;

import com.utsav.arts.models.Payment;
import com.utsav.arts.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment save(Payment payment) {
        // Set creation timestamp if not provided
        if (payment.getCreatedAt() == null) {
            payment.setCreatedAt(LocalDateTime.now());
        }

        // Default status if not provided
        if (payment.getStatus() == null || payment.getStatus().isBlank()) {
            payment.setStatus("PENDING");
        }

        return paymentRepository.save(payment);
    }

    @Override
    public Payment update(Payment payment) {
        paymentRepository.findById(payment.getId())
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        return paymentRepository.update(payment);
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
    public List<Payment> findByStatus(String status) {
        return paymentRepository.findByStatus(status);
    }

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    @Override
    public void deleteById(int id) {
        if (paymentRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Payment not found");
        }
        paymentRepository.deleteById(id);
    }
}
