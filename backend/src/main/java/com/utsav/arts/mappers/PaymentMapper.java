package com.utsav.arts.mappers;

import com.utsav.arts.dtos.paymentDTO.PaymentRequestDTO;
import com.utsav.arts.dtos.paymentDTO.PaymentResponseDTO;
import com.utsav.arts.models.Orders;
import com.utsav.arts.models.Payment;
import com.utsav.arts.models.User;

public class PaymentMapper {

    public static Payment toEntity(
            PaymentRequestDTO dto,
            Orders order,
            User user
    ) {
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setUser(user);
        payment.setAmount(dto.getAmount());
        payment.setCurrency(dto.getCurrency());
        payment.setMethod(dto.getMethod());
        payment.setStatus(dto.getStatus());
        payment.setTransactionId(dto.getTransactionId());
        return payment;
    }

    public static PaymentResponseDTO toResponseDTO(Payment payment) {
        PaymentResponseDTO dto = new PaymentResponseDTO();
        dto.setId(payment.getId());
        dto.setOrderId(payment.getOrder().getId());
        dto.setUserId(payment.getUser().getId());
        dto.setAmount(payment.getAmount());
        dto.setCurrency(payment.getCurrency());
        dto.setMethod(payment.getMethod());
        dto.setStatus(payment.getStatus());
        dto.setTransactionId(payment.getTransactionId());
        dto.setCreatedAt(payment.getCreatedAt());
        return dto;
    }
}
