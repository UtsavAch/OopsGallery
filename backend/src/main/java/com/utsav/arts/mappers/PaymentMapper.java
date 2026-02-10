package com.utsav.arts.mappers;

import com.utsav.arts.dtos.paymentDTO.PaymentResponseDTO;
import com.utsav.arts.models.Payment;

/**
 * Mapper class for Payment entity to PaymentResponseDTO.
 *
 * <p>Provides method to convert Payment → PaymentResponseDTO.
 */
public class PaymentMapper {
    /**
     * Converts a Payment entity to PaymentResponseDTO.
     * @param payment Payment entity
     * @return PaymentResponseDTO with mapped fields
     */
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
