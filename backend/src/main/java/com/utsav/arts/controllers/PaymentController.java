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

/**
 * Controller for managing payments.
 *
 * <p>Supports viewing payments, creating Stripe payment intents, deleting payments,
 * and retrieving available payment statuses.
 *
 * <p>Endpoints:
 * <ul>
 *     <li>GET /api/payments → Get all payments (OWNER only)</li>
 *     <li>GET /api/payments/{id} → Get payment by ID</li>
 *     <li>GET /api/payments/user/{userId} → Get payments for a user</li>
 *     <li>GET /api/payments/order/{orderId} → Get payments for an order</li>
 *     <li>GET /api/payments/status/{status} → Get payments by status (OWNER only)</li>
 *     <li>POST /api/payments/intent → Create a Stripe PaymentIntent</li>
 *     <li>DELETE /api/payments/{id} → Delete payment (OWNER only)</li>
 *     <li>GET /api/payments/payment-statuses → Get all payment status options (OWNER only)</li>
 * </ul>
 */
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
    /**
     * Retrieves a payment by its ID.
     *
     * @param id Payment ID
     * @return {@link PaymentResponseDTO} for the requested payment
     * @throws ResourceNotFoundException if payment does not exist
     * @apiNote Accessible by OWNER or the payment owner
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('OWNER') or @paymentService.isPaymentOwner(#id, authentication.principal.id)")
    public ResponseEntity<PaymentResponseDTO> findById(@PathVariable int id) {
        Payment payment = paymentService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment record not found with id: " + id));

        return ResponseEntity.ok(PaymentMapper.toResponseDTO(payment));
    }

    /**
     * Retrieves all payments associated with a specific user.
     *
     * @param userId ID of the user
     * @return List of {@link PaymentResponseDTO} for the user
     * @throws ResourceNotFoundException if the user does not exist
     * @apiNote Accessible by OWNER or the user themselves
     */
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

    /**
     * Retrieves all payments associated with a specific order.
     *
     * @param orderId ID of the order
     * @return List of {@link PaymentResponseDTO} for the order
     * @apiNote Accessible by OWNER or the order owner
     */
    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasRole('OWNER') or @ordersService.isOwner(#orderId, authentication.principal.id)")
    public ResponseEntity<List<PaymentResponseDTO>> findByOrderId(@PathVariable int orderId) {
        List<PaymentResponseDTO> payments = paymentService.findByOrderId(orderId)
                .stream()
                .map(PaymentMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(payments);
    }

    /**
     * Retrieves all payments filtered by payment status.
     *
     * @param status Payment status as a string (case-insensitive)
     * @return List of {@link PaymentResponseDTO} with the requested status
     * @throws InvalidRequestException if the status is invalid
     * @apiNote Accessible by OWNER only
     */
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

    /**
     * Retrieves all payments in the system.
     *
     * @return List of all {@link PaymentResponseDTO}
     * @apiNote Accessible by OWNER only
     */
    @GetMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<List<PaymentResponseDTO>> findAll() {
        List<PaymentResponseDTO> payments = paymentService.findAll()
                .stream()
                .map(PaymentMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(payments);
    }

    // ---------------- CREATE PAYMENT INTENT ----------------
    /**
     * Creates a Stripe PaymentIntent for a given order.
     *
     * <p>Verifies that the authenticated user owns the order.
     *
     * @param requestDTO DTO containing order ID and currency
     * @param authentication Authentication object with current user details
     * @return Map containing the Stripe {@code clientSecret} for frontend payment processing
     * @throws ResourceNotFoundException if the order does not exist
     * @apiNote Accessible by OWNER or USER
     */
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
    /**
     * Deletes a payment by ID.
     *
     * @param id Payment ID
     * @return {@code 204 No Content} on successful deletion
     * @apiNote Accessible by OWNER only
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Void> deleteById(@PathVariable int id) {
        // Service now handles the ResourceNotFoundException
        paymentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves all available payment status values.
     *
     * @return List of payment status names as strings
     * @apiNote Accessible by OWNER only
     */
    @GetMapping("/payment-statuses")
    @PreAuthorize("hasRole('OWNER')")
    public List<String> getPaymentStatuses() {
        return Arrays.stream(PaymentStatus.values())
                .map(Enum::name)
                .toList();
    }
}