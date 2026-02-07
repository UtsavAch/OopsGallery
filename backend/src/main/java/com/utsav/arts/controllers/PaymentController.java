package com.utsav.arts.controllers;

import com.utsav.arts.configurations.UserPrincipal;
import com.utsav.arts.stripepayment.CreatePaymentIntentDTO;
import com.utsav.arts.dtos.paymentDTO.PaymentResponseDTO;
import com.utsav.arts.exceptions.InvalidRequestException;
import com.utsav.arts.exceptions.ResourceNotFoundException;
import com.utsav.arts.mappers.PaymentMapper;
import com.utsav.arts.models.Orders;
import com.utsav.arts.models.Payment;
import com.utsav.arts.models.PaymentStatus;
import com.utsav.arts.stripepayment.StripeService;
import com.utsav.arts.services.OrdersService;
import com.utsav.arts.services.PaymentService;
import com.utsav.arts.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@PreAuthorize("hasAnyRole('USER', 'OWNER')")
public class PaymentController {

    private final PaymentService paymentService;
    private final UserService userService;
    private final OrdersService ordersService;
    private final StripeService stripeService;

    public PaymentController(PaymentService paymentService, UserService userService, OrdersService ordersService, StripeService stripeService) {
        this.paymentService = paymentService;
        this.userService = userService;
        this.ordersService = ordersService;
        this.stripeService = stripeService;
    }

    // ---------------- READ ----------------
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('OWNER') or @paymentService.isPaymentOwner(#id, authentication.principal.id)")
    public ResponseEntity<PaymentResponseDTO> findById(@PathVariable int id) {
        Payment payment = paymentService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment record not found with id: " + id));

        return ResponseEntity.ok(PaymentMapper.toResponseDTO(payment));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('OWNER') or #userId == authentication.principal.id")
    public ResponseEntity<List<PaymentResponseDTO>> findByUserId(@PathVariable int userId) {
        // Verify user exists to avoid returning empty lists for non-existent users
        userService.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        List<PaymentResponseDTO> payments = paymentService.findByUserId(userId)
                .stream()
                .map(PaymentMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasRole('OWNER') or @ordersService.isOwner(#orderId, authentication.principal.id)")
    public ResponseEntity<List<PaymentResponseDTO>> findByOrderId(@PathVariable int orderId) {
        List<PaymentResponseDTO> payments = paymentService.findByOrderId(orderId)
                .stream()
                .map(PaymentMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<List<PaymentResponseDTO>> findByStatus(@PathVariable String status) {
        PaymentStatus paymentStatus;
        try {
            paymentStatus = PaymentStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Throwing our custom InvalidRequestException for a clean 400 Bad Request
            throw new InvalidRequestException("Invalid payment status: " + status);
        }

        List<PaymentResponseDTO> payments = paymentService.findByStatus(paymentStatus)
                .stream()
                .map(PaymentMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(payments);
    }

    @GetMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<List<PaymentResponseDTO>> findAll() {
        List<PaymentResponseDTO> payments = paymentService.findAll()
                .stream()
                .map(PaymentMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(payments);
    }

    // ---------------- UPDATE ----------------
    @PostMapping("/intent")
    @PreAuthorize("hasRole('OWNER') or hasRole('USER')")
    public ResponseEntity<Map<String, String>> createPaymentIntent(
            @Valid @RequestBody CreatePaymentIntentDTO requestDTO,
            Authentication authentication
    ) throws Exception {

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        assert principal != null;
        int userId = principal.getId();

        // Verify order ownership
        if (!ordersService.isOwner(requestDTO.getOrderId(), userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Fetch order (server-side truth)
        Orders order = ordersService.findById(requestDTO.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // Create Stripe PaymentIntent
        String clientSecret = stripeService.createPaymentIntent(
                order.getTotalPrice(),
                requestDTO.getCurrency(),
                order.getId(),
                userId
        );

        return ResponseEntity.ok(Map.of("clientSecret", clientSecret));
    }


    // ---------------- DELETE ----------------
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Void> deleteById(@PathVariable int id) {
        // Service now handles the ResourceNotFoundException
        paymentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/payment-statuses")
    @PreAuthorize("hasRole('OWNER')")
    public List<String> getPaymentStatuses() {
        return Arrays.stream(PaymentStatus.values())
                .map(Enum::name)
                .toList();
    }
}