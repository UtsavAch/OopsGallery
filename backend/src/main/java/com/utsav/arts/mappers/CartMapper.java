package com.utsav.arts.mappers;

import com.utsav.arts.dtos.cartDTO.CartResponseDTO;
import com.utsav.arts.dtos.cartItemDTO.CartItemResponseDTO;
import com.utsav.arts.models.Cart;

import java.util.stream.Collectors;

public class CartMapper {

    public static CartResponseDTO toResponseDTO(Cart cart) {
        CartResponseDTO dto = new CartResponseDTO();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUser().getId());
        dto.setTotalItems(cart.getTotalItems());
        dto.setTotalPrice(cart.getTotalPrice());

        // map CartItems
        dto.setCartItems(
                cart.getItems().stream()
                        .map(item -> {
                            CartItemResponseDTO itemDTO = new CartItemResponseDTO();
                            itemDTO.setId(item.getId());
                            itemDTO.setCartId(cart.getId());
                            itemDTO.setArtworkId(item.getArtwork().getId());
                            itemDTO.setQuantity(item.getQuantity());
                            return itemDTO;
                        })
                        .collect(Collectors.toList())
        );

        return dto;
    }
}
