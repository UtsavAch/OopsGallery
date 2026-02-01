package com.utsav.arts.controllers;

import com.utsav.arts.configurations.UserPrincipal;
import com.utsav.arts.dtos.ordersDTO.OrdersRequestDTO;
import com.utsav.arts.dtos.ordersDTO.OrdersResponseDTO;
import com.utsav.arts.exceptions.ResourceNotFoundException;
import com.utsav.arts.mappers.OrdersMapper;
import com.utsav.arts.models.OrderStatus;
import com.utsav.arts.models.Orders;
import com.utsav.arts.services.OrdersService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/orders")
@PreAuthorize("hasAnyRole('USER', 'OWNER')")
public class OrdersController {

    private final OrdersService ordersService;

    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    // ---------------- PLACE ORDER ----------------
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'OWNER')")
    public ResponseEntity<OrdersResponseDTO> placeOrder(
            @Valid @RequestBody OrdersRequestDTO request,
            Authentication authentication
    ) {
        // Extract userId from authenticated principal for security
        int userId = ((UserPrincipal) Objects.requireNonNull(authentication.getPrincipal())).getId();

        // Service now handles business logic errors (e.g., empty cart) via InvalidRequestException
        Orders order = ordersService.placeOrder(
                userId,
                request.getAddress()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(OrdersMapper.toDTO(order));
    }

    // ---------------- READ ----------------

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('OWNER') or @ordersService.isOwner(#id, authentication.principal.id)")
    public ResponseEntity<OrdersResponseDTO> findById(@PathVariable int id) {
        // Use orElseThrow to trigger GlobalExceptionHandler's JSON response
        Orders order = ordersService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        return ResponseEntity.ok(OrdersMapper.toDTO(order));
    }

    @GetMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<List<OrdersResponseDTO>> findAll() {
        List<OrdersResponseDTO> orders = ordersService.findAll()
                .stream()
                .map(OrdersMapper::toDTO)
                .toList();

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('OWNER') or authentication.principal.id == #userId")
    public ResponseEntity<List<OrdersResponseDTO>> findByUserId(
            @PathVariable int userId
    ) {
        List<OrdersResponseDTO> orders = ordersService.findByUserId(userId)
                .stream()
                .map(OrdersMapper::toDTO)
                .toList();

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<List<OrdersResponseDTO>> findByStatus(
            @PathVariable OrderStatus status
    ) {
        List<OrdersResponseDTO> orders = ordersService.findByStatus(status)
                .stream()
                .map(OrdersMapper::toDTO)
                .toList();

        return ResponseEntity.ok(orders);
    }

    // ---------------- UPDATE ----------------
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<OrdersResponseDTO> updateStatus(
            @PathVariable int id,
            @RequestParam OrderStatus status
    ) {
        // service handles 404 if orderId is invalid
        Orders updated = ordersService.updateStatus(id, status);
        return ResponseEntity.ok(OrdersMapper.toDTO(updated));
    }

    // ---------------- DELETE ----------------
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        // service handles 404 if orderId is invalid
        ordersService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}