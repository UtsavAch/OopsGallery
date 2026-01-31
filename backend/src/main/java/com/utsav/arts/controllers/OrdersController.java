package com.utsav.arts.controllers;

import com.utsav.arts.configurations.UserPrincipal;
import com.utsav.arts.dtos.ordersDTO.OrdersRequestDTO;
import com.utsav.arts.dtos.ordersDTO.OrdersResponseDTO;
import com.utsav.arts.mappers.OrdersMapper;
import com.utsav.arts.models.OrderStatus;
import com.utsav.arts.models.Orders;
import com.utsav.arts.services.OrdersService;
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
            @RequestBody OrdersRequestDTO request,
            Authentication authentication
    ) {
        int userId = ((UserPrincipal) Objects.requireNonNull(authentication.getPrincipal())).getId();

        Orders order = ordersService.placeOrder(
                userId,
                request.getAddress()
        );

        return ResponseEntity.status(201)
                .body(OrdersMapper.toDTO(order));
    }
    // ---------------- READ ----------------

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('OWNER') or @ordersService.isOwner(#id, authentication.principal.id)")
    public ResponseEntity<OrdersResponseDTO> findById(@PathVariable int id) {
        return ordersService.findById(id)
                .map(order -> ResponseEntity.ok(OrdersMapper.toDTO(order)))
                .orElse(ResponseEntity.notFound().build());
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
    // Only OWNER can change order status
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<OrdersResponseDTO> updateStatus(
            @PathVariable int id,
            @RequestParam OrderStatus status
    ) {
        Orders updated = ordersService.updateStatus(id, status);
        return ResponseEntity.ok(OrdersMapper.toDTO(updated));
    }

    // ---------------- DELETE ----------------
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        ordersService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
