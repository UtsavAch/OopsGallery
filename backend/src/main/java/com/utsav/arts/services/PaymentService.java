package com.utsav.arts.services;

import com.utsav.arts.models.Payment;
import com.utsav.arts.models.PaymentStatus;

import java.util.List;
import java.util.Optional;

public interface PaymentService {

    Payment save(Payment payment);

    //Payment update(Payment payment);

    Payment markSuccess(int paymentId);

    Payment markFailed(int paymentId);

    Payment refund(int paymentId);

    Payment cancel(int paymentId);

    Optional<Payment> findById(int id);

    Optional<Payment> findByTransactionId(String transactionId);

    List<Payment> findByUserId(int userId);

    List<Payment> findByOrderId(int orderId);

    List<Payment> findByStatus(PaymentStatus status);

    List<Payment> findAll();

    void deleteById(int id);

    boolean isPaymentOwner(int paymentId, int userId);
}
