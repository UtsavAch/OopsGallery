package com.utsav.arts.controllers;

import com.utsav.arts.dtos.cartItemDTO.CartItemRequestDTO;
import com.utsav.arts.dtos.cartItemDTO.CartItemResponseDTO;
import com.utsav.arts.mappers.CartItemMapper;
import com.utsav.arts.models.Artwork;
import com.utsav.arts.models.Cart;
import com.utsav.arts.models.CartItem;
import com.utsav.arts.services.ArtworkService;
import com.utsav.arts.services.CartItemService;
import com.utsav.arts.services.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart-items")
public class CartItemController {

    private final CartItemService cartItemService;
    private final CartService cartService;
    private final ArtworkService artworkService;

    public CartItemController(
            CartItemService cartItemService,
            CartService cartService,
            ArtworkService artworkService
    ) {
        this.cartItemService = cartItemService;
        this.cartService = cartService;
        this.artworkService = artworkService;
    }

    // ---------------- ADD TO CART ----------------
    @PostMapping
    public ResponseEntity<CartItemResponseDTO> save(
            @RequestBody CartItemRequestDTO requestDTO
    ) {
        Cart cart = cartService.findById(requestDTO.getCartId())
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        Artwork artwork = artworkService.findById(requestDTO.getArtworkId())
                .orElseThrow(() -> new IllegalArgumentException("Artwork not found"));

        CartItem cartItem =
                CartItemMapper.toEntity(requestDTO, cart, artwork);

        CartItem savedItem = cartItemService.save(cartItem);

        return new ResponseEntity<>(
                CartItemMapper.toResponseDTO(savedItem),
                HttpStatus.CREATED
        );
    }

    // ---------------- UPDATE QUANTITY ----------------
    @PutMapping("/{id}")
    public ResponseEntity<CartItemResponseDTO> update(
            @PathVariable int id,
            @RequestBody CartItemRequestDTO requestDTO
    ) {
        Cart cart = cartService.findById(requestDTO.getCartId())
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        Artwork artwork = artworkService.findById(requestDTO.getArtworkId())
                .orElseThrow(() -> new IllegalArgumentException("Artwork not found"));

        CartItem cartItem =
                CartItemMapper.toEntity(requestDTO, cart, artwork);
        cartItem.setId(id);

        CartItem updatedItem = cartItemService.update(cartItem);

        return ResponseEntity.ok(
                CartItemMapper.toResponseDTO(updatedItem)
        );
    }

    // ---------------- READ ----------------
    @GetMapping("/{id}")
    public ResponseEntity<CartItemResponseDTO> findById(
            @PathVariable int id
    ) {
        return cartItemService.findById(id)
                .map(item -> ResponseEntity.ok(
                        CartItemMapper.toResponseDTO(item)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cart/{cartId}")
    public ResponseEntity<List<CartItemResponseDTO>> findByCartId(
            @PathVariable int cartId
    ) {
        List<CartItemResponseDTO> items =
                cartItemService.findByCartId(cartId)
                        .stream()
                        .map(CartItemMapper::toResponseDTO)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(items);
    }

    // ---------------- DELETE ----------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable int id) {
        cartItemService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/cart/{cartId}")
    public ResponseEntity<Void> deleteByCartId(
            @PathVariable int cartId
    ) {
        cartItemService.deleteByCartId(cartId);
        return ResponseEntity.noContent().build();
    }
}
