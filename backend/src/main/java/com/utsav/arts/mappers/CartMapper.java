package com.utsav.arts.mappers;

import com.utsav.arts.dtos.cartDTO.CartResponseDTO;
import com.utsav.arts.models.Cart;
import com.utsav.arts.models.CartItem;

public class CartMapper {

    public static CartResponseDTO toResponseDTO(Cart cart) {
        CartResponseDTO dto = new CartResponseDTO();

        dto.setId(cart.getId());
        dto.setUserId(cart.getUser().getId());

        dto.setCartItems(
                cart.getItems()
                        .stream()
                        .map(CartItem::getId)
                        .toList()
        );

        dto.setTotalItems(cart.getTotalItems());
        dto.setTotalPrice(cart.getTotalPrice());

        return dto;
    }
}
