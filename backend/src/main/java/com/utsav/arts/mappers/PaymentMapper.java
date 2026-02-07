package com.utsav.arts.mappers;

import com.utsav.arts.dtos.paymentDTO.PaymentResponseDTO;
import com.utsav.arts.models.Payment;

public class PaymentMapper {
    // Map Entity → Response DTO
    public static PaymentResponseDTO toResponseDTO(Payment payment) {
        PaymentResponseDTO dto = new PaymentResponseDTO();
        dto.setId(payment.getId());
        dto.setOrderId(payment.getOrder().getId());
        dto.setUserId(payment.getUser().getId());
        dto.setAmount(payment.getAmount());
        dto.setCurrency(payment.getCurrency());
        dto.setMethod(payment.getMethod());
        dto.setStatus(payment.getStatus().name()); // Convert enum to string
        dto.setTransactionId(payment.getTransactionId());
        dto.setCreatedAt(payment.getCreatedAt());
        return dto;
    }
}
