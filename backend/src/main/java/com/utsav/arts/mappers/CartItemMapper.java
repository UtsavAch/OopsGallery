package com.utsav.arts.mappers;

import com.utsav.arts.dtos.cartItemDTO.CartItemRequestDTO;
import com.utsav.arts.dtos.cartItemDTO.CartItemResponseDTO;
import com.utsav.arts.models.Artwork;
import com.utsav.arts.models.Cart;
import com.utsav.arts.models.CartItem;

/**
 * Mapper class for CartItem entity and DTOs.
 *
 * <p>Provides methods to map:
 * <ul>
 *   <li>CartItemRequestDTO → CartItem entity</li>
 *   <li>CartItem entity → CartItemResponseDTO</li>
 * </ul>
 */
public class CartItemMapper {

    /**
     * Maps CartItemRequestDTO to a CartItem entity with associated Cart and Artwork.
     * @param dto CartItemRequestDTO containing quantity and IDs
     * @param cart Cart entity to attach
     * @param artwork Artwork entity to attach
     * @return CartItem entity
     */
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

    /**
     * Maps a CartItem entity to CartItemResponseDTO.
     * @param cartItem CartItem entity to map
     * @return CartItemResponseDTO
     */
    public static CartItemResponseDTO toResponseDTO(CartItem cartItem) {
        CartItemResponseDTO dto = new CartItemResponseDTO();
        dto.setId(cartItem.getId());
        dto.setCartId(cartItem.getCart().getId());
        dto.setArtworkId(cartItem.getArtwork().getId());
        dto.setQuantity(cartItem.getQuantity());
        return dto;
    }
}
