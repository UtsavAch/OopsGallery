package com.utsav.arts.repository;

import com.utsav.arts.models.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository {

    Payment save(Payment payment);

    Payment update(Payment payment);

    Optional<Payment> findById(int id);

    Optional<Payment> findByTransactionId(String transactionId);

    List<Payment> findByUserId(int userId);

    List<Payment> findByOrderId(int orderId);

    List<Payment> findByStatus(String status);

    List<Payment> findAll();

    void deleteById(int id);
}
