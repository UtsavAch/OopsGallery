package com.utsav.arts.mappers;

import com.utsav.arts.dtos.ordersDTO.OrdersRequestDTO;
import com.utsav.arts.dtos.ordersDTO.OrdersResponseDTO;
import com.utsav.arts.models.Artwork;
import com.utsav.arts.models.Orders;
import com.utsav.arts.models.User;

public class OrdersMapper {

    public static Orders toEntity(
            OrdersRequestDTO dto,
            User user,
            Artwork artwork
    ) {
        Orders order = new Orders();
        order.setUser(user);
        order.setArtwork(artwork);
        order.setPrice(dto.getPrice());
        order.setAddress(dto.getAddress());
        order.setStatus(dto.getStatus());
        return order;
    }

    public static OrdersResponseDTO toDTO(Orders order) {
        OrdersResponseDTO dto = new OrdersResponseDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUser().getId());
        dto.setArtworkId(order.getArtwork().getId());
        dto.setPrice(order.getPrice());
        dto.setAddress(order.getAddress());
        dto.setStatus(order.getStatus());
        dto.setOrderedAt(order.getOrderedAt());
        return dto;
    }
}
