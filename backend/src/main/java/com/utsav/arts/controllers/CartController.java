package com.utsav.arts.controllers;

import com.utsav.arts.dtos.cartDTO.CartRequestDTO;
import com.utsav.arts.dtos.cartDTO.CartResponseDTO;
import com.utsav.arts.exceptions.ResourceNotFoundException;
import com.utsav.arts.mappers.CartMapper;
import com.utsav.arts.models.Cart;
import com.utsav.arts.models.User;
import com.utsav.arts.services.CartService;
import com.utsav.arts.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller to manage shopping carts for users.
 *
 * <p>Supports creation, retrieval, and deletion of carts.
 * Access is restricted to authenticated users with roles USER or OWNER.
 *
 * <p>Endpoints:
 * <ul>
 *     <li>POST /api/carts → Create a cart</li>
 *     <li>GET /api/carts/{id} → Get cart by ID</li>
 *     <li>GET /api/carts/user/{userId} → Get cart for a specific user</li>
 *     <li>DELETE /api/carts/{id} → Delete cart by ID</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/carts")
@PreAuthorize("hasAnyRole('USER', 'OWNER')")
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    public CartController(
            CartService cartService,
            UserService userService
    ) {
        this.cartService = cartService;
        this.userService = userService;
    }

    // ---------------- CREATE CART ----------------
    /**
     * Creates a new cart for a user.
     *
     * @param requestDTO Contains the userId for which the cart is created
     * @return Created CartResponseDTO with cart details
     * @throws ResourceNotFoundException if the user does not exist
     */
    @PostMapping
    @PreAuthorize("hasRole('OWNER') or #requestDTO.userId == authentication.principal.id")
    public ResponseEntity<CartResponseDTO> save(
            @Valid @RequestBody CartRequestDTO requestDTO
    ) {
        User user = userService.findById(requestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Cannot create cart: User not found with id: " + requestDTO.getUserId()));

        Cart cart = new Cart(user);
        Cart savedCart = cartService.save(cart);

        return new ResponseEntity<>(
                CartMapper.toResponseDTO(savedCart),
                HttpStatus.CREATED
        );
    }

    // ---------------- READ ----------------
    /**
     * Retrieves a cart by its ID.
     *
     * @param id Cart ID
     * @return CartResponseDTO
     * @throws ResourceNotFoundException if the cart does not exist
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('OWNER') or @cartService.isOwner(#id, authentication.principal.id)")
    public ResponseEntity<CartResponseDTO> findById(
            @PathVariable int id
    ) {
        Cart cart = cartService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + id));

        return ResponseEntity.ok(CartMapper.toResponseDTO(cart));
    }

    /**
     * Retrieves a cart by the user ID.
     *
     * @param userId User ID
     * @return CartResponseDTO for the specified user
     * @throws ResourceNotFoundException if the cart does not exist
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('OWNER') or #userId == authentication.principal.id")
    public ResponseEntity<CartResponseDTO> findByUserId(
            @PathVariable int userId
    ) {
        Cart cart = cartService.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user id: " + userId));

        return ResponseEntity.ok(CartMapper.toResponseDTO(cart));
    }

    // ---------------- DELETE ----------------
    /**
     * Deletes a cart by its ID.
     *
     * @param id Cart ID
     * @return HTTP 204 No Content on success
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER') or @cartService.isOwner(#id, authentication.principal.id)")
    public ResponseEntity<Void> deleteById(@PathVariable int id) {
        // Service already handles ResourceNotFoundException
        cartService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}