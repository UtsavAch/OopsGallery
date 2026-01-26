package com.utsav.arts.controllers;

import com.utsav.arts.dtos.ordersDTO.OrdersRequestDTO;
import com.utsav.arts.dtos.ordersDTO.OrdersResponseDTO;
import com.utsav.arts.mappers.OrdersMapper;
import com.utsav.arts.models.Artwork;
import com.utsav.arts.models.Orders;
import com.utsav.arts.models.User;
import com.utsav.arts.services.ArtworkService;
import com.utsav.arts.services.OrdersService;
import com.utsav.arts.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@PreAuthorize("hasAnyRole('USER', 'OWNER')")
public class OrdersController {

    private final OrdersService ordersService;
    private final UserService userService;
    private final ArtworkService artworkService;

    public OrdersController(OrdersService ordersService, UserService userService, ArtworkService artworkService) {
        this.ordersService = ordersService;
        this.userService = userService;
        this.artworkService = artworkService;
    }

    // CREATE ORDER
    @PostMapping
    // Logic: Only owner or the specific user whose ID is in the request
    @PreAuthorize("hasRole('OWNER') or @userService.isUserOwner(#request.userId, authentication.name)")
    public ResponseEntity<OrdersResponseDTO> createOrder(@RequestBody OrdersRequestDTO request) {
        User user = userService.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Artwork artwork = artworkService.findById(request.getArtworkId())
                .orElseThrow(() -> new IllegalArgumentException("Artwork not found"));

        Orders order = OrdersMapper.toEntity(request, user, artwork);
        Orders saved = ordersService.save(order);

        return ResponseEntity.ok(OrdersMapper.toDTO(saved));
    }

    // GET ORDER BY ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('OWNER') or @ordersService.isOrderOwner(#id, authentication.name)")
    public ResponseEntity<OrdersResponseDTO> getOrderById(@PathVariable int id) {
        Orders order = ordersService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        return ResponseEntity.ok(OrdersMapper.toDTO(order));
    }

    // GET ALL ORDERS
    @GetMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<List<OrdersResponseDTO>> getAllOrders() {
        List<OrdersResponseDTO> orders = ordersService.findAll()
                .stream()
                .map(OrdersMapper::toDTO)
                .toList();

        return ResponseEntity.ok(orders);
    }

    // GET ORDERS BY USER
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('OWNER') or @userService.isUserOwner(#userId, authentication.name)")
    public ResponseEntity<List<OrdersResponseDTO>> getOrdersByUser(@PathVariable int userId) {
        List<OrdersResponseDTO> orders = ordersService.findByUserId(userId)
                .stream()
                .map(OrdersMapper::toDTO)
                .toList();

        return ResponseEntity.ok(orders);
    }

    // GET ORDERS BY STATUS
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<List<OrdersResponseDTO>> getOrdersByStatus(@PathVariable String status) {
        List<OrdersResponseDTO> orders = ordersService.findByStatus(status)
                .stream()
                .map(OrdersMapper::toDTO)
                .toList();

        return ResponseEntity.ok(orders);
    }

    // UPDATE ORDER
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('OWNER') or @ordersService.isOrderOwner(#id, authentication.name)")
    public ResponseEntity<OrdersResponseDTO> updateOrder(@PathVariable int id, @RequestBody OrdersRequestDTO request) {
        Orders order = ordersService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        order.setPrice(request.getPrice());
        order.setAddress(request.getAddress());
        order.setStatus(request.getStatus());

        Orders updated = ordersService.update(order);
        return ResponseEntity.ok(OrdersMapper.toDTO(updated));
    }

    // DELETE ORDER
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Void> deleteOrder(@PathVariable int id) {
        ordersService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}