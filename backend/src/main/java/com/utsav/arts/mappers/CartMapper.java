package com.utsav.arts.mappers;

import com.utsav.arts.dtos.cartDTO.CartResponseDTO;
import com.utsav.arts.models.Cart;
import com.utsav.arts.models.CartItem;

import java.util.stream.Collectors;

public class CartMapper {

    public static CartResponseDTO toResponseDTO(Cart cart) {
        CartResponseDTO dto = new CartResponseDTO();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUser().getId());

        if (cart.getItems() != null) {
            dto.setCartItemIds(
                    cart.getItems()
                            .stream()
                            .map(CartItem::getId)
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }
}
