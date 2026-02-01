package com.utsav.arts.controllers;

import com.utsav.arts.dtos.cartItemDTO.CartItemRequestDTO;
import com.utsav.arts.dtos.cartItemDTO.CartItemResponseDTO;
import com.utsav.arts.exceptions.ResourceNotFoundException;
import com.utsav.arts.mappers.CartItemMapper;
import com.utsav.arts.models.Artwork;
import com.utsav.arts.models.Cart;
import com.utsav.arts.models.CartItem;
import com.utsav.arts.services.ArtworkService;
import com.utsav.arts.services.CartItemService;
import com.utsav.arts.services.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart-items")
@PreAuthorize("hasAnyRole('USER', 'OWNER')")
public class CartItemController {

    private final CartItemService cartItemService;
    private final CartService cartService;
    private final ArtworkService artworkService;

    public CartItemController(CartItemService cartItemService,
                              CartService cartService,
                              ArtworkService artworkService) {
        this.cartItemService = cartItemService;
        this.cartService = cartService;
        this.artworkService = artworkService;
    }

    // ---------------- ADD TO CART ----------------
    @PostMapping
    @PreAuthorize("hasRole('OWNER') or @cartService.isOwner(#requestDTO.cartId, authentication.principal.id)")
    public ResponseEntity<CartItemResponseDTO> save(@Valid @RequestBody CartItemRequestDTO requestDTO) {
        // Use ResourceNotFoundException for 404 instead of generic IllegalArgumentException
        Cart cart = cartService.findById(requestDTO.getCartId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + requestDTO.getCartId()));

        Artwork artwork = artworkService.findById(requestDTO.getArtworkId())
                .orElseThrow(() -> new ResourceNotFoundException("Artwork not found with id: " + requestDTO.getArtworkId()));

        CartItem cartItem = CartItemMapper.toEntity(requestDTO, cart, artwork);
        CartItem savedItem = cartItemService.save(cartItem);

        return new ResponseEntity<>(CartItemMapper.toResponseDTO(savedItem), HttpStatus.CREATED);
    }

    // ---------------- UPDATE QUANTITY ----------------
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('OWNER') or @cartItemService.isOwner(#id, authentication.principal.id)")
    public ResponseEntity<CartItemResponseDTO> update(@PathVariable int id, @Valid @RequestBody CartItemRequestDTO requestDTO) {
        Cart cart = cartService.findById(requestDTO.getCartId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + requestDTO.getCartId()));

        Artwork artwork = artworkService.findById(requestDTO.getArtworkId())
                .orElseThrow(() -> new ResourceNotFoundException("Artwork not found with id: " + requestDTO.getArtworkId()));

        CartItem cartItem = CartItemMapper.toEntity(requestDTO, cart, artwork);
        cartItem.setId(id);

        CartItem updatedItem = cartItemService.update(cartItem);

        return ResponseEntity.ok(CartItemMapper.toResponseDTO(updatedItem));
    }

    // ---------------- READ ----------------
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('OWNER') or @cartItemService.isOwner(#id, authentication.principal.id)")
    public ResponseEntity<CartItemResponseDTO> findById(@PathVariable int id) {
        // Throw exception to trigger GlobalExceptionHandler
        CartItem item = cartItemService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + id));

        return ResponseEntity.ok(CartItemMapper.toResponseDTO(item));
    }

    @GetMapping("/cart/{cartId}")
    @PreAuthorize("hasRole('OWNER') or @cartService.isOwner(#cartId, authentication.principal.id)")
    public ResponseEntity<List<CartItemResponseDTO>> findByCartId(@PathVariable int cartId) {
        // Verify cart exists first
        if (cartService.findById(cartId).isEmpty()) {
            throw new ResourceNotFoundException("Cart not found with id: " + cartId);
        }

        List<CartItemResponseDTO> items = cartItemService.findByCartId(cartId)
                .stream()
                .map(CartItemMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(items);
    }

    // ---------------- DELETE ----------------
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER') or @cartItemService.isOwner(#id, authentication.principal.id)")
    public ResponseEntity<Void> deleteById(@PathVariable int id) {
        cartItemService.deleteById(id); // Service now handles ResourceNotFoundException
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/cart/{cartId}")
    @PreAuthorize("hasRole('OWNER') or @cartService.isOwner(#cartId, authentication.principal.id)")
    public ResponseEntity<Void> deleteByCartId(@PathVariable int cartId) {
        cartItemService.deleteByCartId(cartId); // Service now handles ResourceNotFoundException
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/decrease")
    @PreAuthorize("hasRole('OWNER') or @cartItemService.isOwner(#id, authentication.principal.id)")
    public ResponseEntity<Void> decreaseQuantity(@PathVariable int id) {
        cartItemService.decreaseQuantity(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/increase")
    @PreAuthorize("hasRole('OWNER') or @cartItemService.isOwner(#id, authentication.principal.id)")
    public ResponseEntity<Void> increaseQuantity(@PathVariable int id) {
        cartItemService.increaseQuantity(id);
        return ResponseEntity.noContent().build();
    }
}