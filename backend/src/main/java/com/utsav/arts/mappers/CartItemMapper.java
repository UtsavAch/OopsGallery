package com.utsav.arts.mappers;

import com.utsav.arts.dtos.cartItemDTO.CartItemRequestDTO;
import com.utsav.arts.dtos.cartItemDTO.CartItemResponseDTO;
import com.utsav.arts.models.Artwork;
import com.utsav.arts.models.Cart;
import com.utsav.arts.models.CartItem;

public class CartItemMapper {

    public static CartItem toEntity(
            CartItemRequestDTO dto,
            Cart cart,
            Artwork artwork
    ) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setArtwork(artwork);
        cartItem.setQuantity(dto.getQuantity());
        return cartItem;
    }

    public static CartItemResponseDTO toResponseDTO(CartItem cartItem) {
        CartItemResponseDTO dto = new CartItemResponseDTO();
        dto.setId(cartItem.getId());
        dto.setCartId(cartItem.getCart().getId());
        dto.setArtworkId(cartItem.getArtwork().getId());
        dto.setQuantity(cartItem.getQuantity());
        return dto;
    }
}
