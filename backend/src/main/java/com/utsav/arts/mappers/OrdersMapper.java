package com.utsav.arts.mappers;

import com.utsav.arts.dtos.ordersDTO.OrderItemResponseDTO;
import com.utsav.arts.dtos.ordersDTO.OrdersResponseDTO;
import com.utsav.arts.models.OrderItem;
import com.utsav.arts.models.Orders;

import java.util.stream.Collectors;

/**
 * Mapper class for Orders and OrderItem entities to their respective DTOs.
 *
 * <p>Methods include:
 * <ul>
 *   <li>Orders → OrdersResponseDTO</li>
 *   <li>OrderItem → OrderItemResponseDTO</li>
 * </ul>
 */
public class OrdersMapper {

    /**
     * Converts an Orders entity to OrdersResponseDTO.
     * Maps all order items using toItemDTO.
     * @param order Orders entity
     * @return OrdersResponseDTO with mapped fields
     */
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

    /**
     * Converts an OrderItem entity to OrderItemResponseDTO.
     * @param item OrderItem entity
     * @return OrderItemResponseDTO
     */
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