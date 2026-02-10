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

/**
 * Controller to manage customer orders.
 *
 * <p>Supports placing orders, viewing orders, updating order status, and deleting orders.
 * Access is restricted to authenticated users with roles USER or OWNER.
 *
 * <p>Endpoints:
 * <ul>
 *     <li>POST /api/orders → Place an order</li>
 *     <li>GET /api/orders/{id} → Get order by ID</li>
 *     <li>GET /api/orders → Get all orders (OWNER only)</li>
 *     <li>GET /api/orders/user/{userId} → Get orders for a user</li>
 *     <li>GET /api/orders/status/{status} → Get orders by status (OWNER only)</li>
 *     <li>POST /api/orders/{id}/confirm → Confirm order (OWNER only)</li>
 *     <li>POST /api/orders/{id}/ship → Ship order (OWNER only)</li>
 *     <li>POST /api/orders/{id}/deliver → Mark order as delivered (OWNER only)</li>
 *     <li>POST /api/orders/{id}/cancel → Cancel order</li>
 *     <li>DELETE /api/orders/{id} → Delete order (OWNER only)</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/orders")
@PreAuthorize("hasAnyRole('USER', 'OWNER')")
public class OrdersController {

    private final OrdersService ordersService;

    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    // ---------------- PLACE ORDER ----------------
    /**
     * Places a new order for the authenticated user.
     *
     * @param request DTO containing order details like shipping address
     * @param authentication Current authenticated user
     * @return Created OrdersResponseDTO
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'OWNER')")
    public ResponseEntity<OrdersResponseDTO> placeOrder(
            @Valid @RequestBody OrdersRequestDTO request,
            Authentication authentication
    ) {
        // Extract userId from authenticated principal for security
        int userId = ((UserPrincipal) Objects.requireNonNull(authentication.getPrincipal())).getId();

        Orders order = ordersService.placeOrder(
                userId,
                request.getAddress()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(OrdersMapper.toDTO(order));
    }

    // ---------------- READ ----------------
    /**
     * Retrieves an order by ID.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('OWNER') or @ordersService.isOwner(#id, authentication.principal.id)")
    public ResponseEntity<OrdersResponseDTO> findById(@PathVariable int id) {
        // Use orElseThrow to trigger GlobalExceptionHandler's JSON response
        Orders order = ordersService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        return ResponseEntity.ok(OrdersMapper.toDTO(order));
    }

    /**
     * Retrieves all orders (OWNER only).
     */
    @GetMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<List<OrdersResponseDTO>> findAll() {
        List<OrdersResponseDTO> orders = ordersService.findAll()
                .stream()
                .map(OrdersMapper::toDTO)
                .toList();

        return ResponseEntity.ok(orders);
    }

    /**
     * Retrieves all orders for a specific user.
     */
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

    /**
     * Retrieves orders by status (OWNER only).
     */
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
    /**
     * Confirm an order. Can be called by OWNER only.
     * (Note: In a real system, this might be triggered by payment confirmation rather than manual action).
     */
    @PostMapping("/{id}/confirm")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<OrdersResponseDTO> confirmOrder(@PathVariable int id) {
        Orders updated = ordersService.confirmOrder(id);
        return ResponseEntity.ok(OrdersMapper.toDTO(updated));
    }

    /**
     * Ship an order. Can be called by OWNER only.
     */
    @PostMapping("/{id}/ship")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<OrdersResponseDTO> shipOrder(@PathVariable int id) {
        Orders updated = ordersService.shipOrder(id);
        return ResponseEntity.ok(OrdersMapper.toDTO(updated));
    }

    /**
     * Deliver an order. Can be called by OWNER only.
     */
    @PostMapping("/{id}/deliver")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<OrdersResponseDTO> deliverOrder(@PathVariable int id) {
        Orders updated = ordersService.deliverOrder(id);
        return ResponseEntity.ok(OrdersMapper.toDTO(updated));
    }

    /**
     * Cancels an order. Can be called by OWNER or the order owner.
     */
    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasRole('OWNER') or @ordersService.isOwner(#id, authentication.principal.id)")
    public ResponseEntity<OrdersResponseDTO> cancelOrder(@PathVariable int id) {
        Orders updated = ordersService.cancelOrder(id);
        return ResponseEntity.ok(OrdersMapper.toDTO(updated));
    }

    // ---------------- DELETE ----------------
    /**
     * Deletes an order (OWNER only).
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        // service handles 404 if orderId is invalid
        ordersService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}