package com.utsav.arts.mappers;

import com.utsav.arts.dtos.ordersDTO.OrderItemResponseDTO;
import com.utsav.arts.dtos.ordersDTO.OrdersResponseDTO;
import com.utsav.arts.models.OrderItem;
import com.utsav.arts.models.Orders;

import java.util.stream.Collectors;

public class OrdersMapper {

    public static OrdersResponseDTO toDTO(Orders order) {
        OrdersResponseDTO dto = new OrdersResponseDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUser().getId());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setAddress(order.getAddress());
        dto.setStatus(order.getStatus().name());
        dto.setOrderedAt(order.getOrderedAt());

        // Map the list of items
        if (order.getOrderItems() != null) {
            dto.setItems(order.getOrderItems().stream()
                    .map(OrdersMapper::toItemDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public static OrderItemResponseDTO toItemDTO(OrderItem item) {
        OrderItemResponseDTO dto = new OrderItemResponseDTO();
        dto.setId(item.getId());
        dto.setArtworkId(item.getArtwork().getId());
        dto.setArtworkTitle(item.getArtwork().getTitle());
        dto.setArtworkImgUrl(item.getArtwork().getImgUrl());
        dto.setQuantity(item.getQuantity());
        dto.setPriceAtPurchase(item.getPriceAtPurchase());
        return dto;
    }
}