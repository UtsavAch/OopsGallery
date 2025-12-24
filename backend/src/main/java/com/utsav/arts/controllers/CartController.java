package com.utsav.arts.controllers;

import com.utsav.arts.dtos.cartDTO.CartRequestDTO;
import com.utsav.arts.dtos.cartDTO.CartResponseDTO;
import com.utsav.arts.mappers.CartMapper;
import com.utsav.arts.models.Cart;
import com.utsav.arts.models.User;
import com.utsav.arts.services.CartService;
import com.utsav.arts.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
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
    @PostMapping
    public ResponseEntity<CartResponseDTO> save(
            @RequestBody CartRequestDTO requestDTO
    ) {
        User user = userService.findById(requestDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Cart cart = new Cart(user);
        Cart savedCart = cartService.save(cart);

        return new ResponseEntity<>(
                CartMapper.toResponseDTO(savedCart),
                HttpStatus.CREATED
        );
    }

    // ---------------- READ ----------------
    @GetMapping("/{id}")
    public ResponseEntity<CartResponseDTO> findById(
            @PathVariable int id
    ) {
        return cartService.findById(id)
                .map(cart -> ResponseEntity.ok(
                        CartMapper.toResponseDTO(cart)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CartResponseDTO> findByUserId(
            @PathVariable int userId
    ) {
        return cartService.findByUserId(userId)
                .map(cart -> ResponseEntity.ok(
                        CartMapper.toResponseDTO(cart)))
                .orElse(ResponseEntity.notFound().build());
    }

    // ---------------- DELETE ----------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable int id) {
        cartService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
