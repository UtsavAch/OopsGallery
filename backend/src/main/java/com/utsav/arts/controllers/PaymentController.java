package com.utsav.arts.controllers;

import com.utsav.arts.dtos.paymentDTO.PaymentRequestDTO;
import com.utsav.arts.dtos.paymentDTO.PaymentResponseDTO;
import com.utsav.arts.exceptions.InvalidRequestException;
import com.utsav.arts.exceptions.ResourceNotFoundException;
import com.utsav.arts.mappers.PaymentMapper;
import com.utsav.arts.models.Orders;
import com.utsav.arts.models.Payment;
import com.utsav.arts.models.PaymentStatus;
import com.utsav.arts.models.User;
import com.utsav.arts.services.OrdersService;
import com.utsav.arts.services.PaymentService;
import com.utsav.arts.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
@PreAuthorize("hasAnyRole('USER', 'OWNER')")
public class PaymentController {

    private final PaymentService paymentService;
    private final UserService userService;
    private final OrdersService ordersService;

    public PaymentController(PaymentService paymentService, UserService userService, OrdersService ordersService) {
        this.paymentService = paymentService;
        this.userService = userService;
        this.ordersService = ordersService;
    }

    // ---------------- CREATE ----------------
    @PostMapping
    @PreAuthorize("hasRole('OWNER') or #requestDTO.userId == authentication.principal.id")
    public ResponseEntity<PaymentResponseDTO> save(@Valid @RequestBody PaymentRequestDTO requestDTO) {
        // Use ResourceNotFoundException for consistent 404 messaging
        User user = userService.findById(requestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment failed: User not found with id: " + requestDTO.getUserId()));

        Orders order = ordersService.findById(requestDTO.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment failed: Order not found with id: " + requestDTO.getOrderId()));

        Payment payment = PaymentMapper.toEntity(requestDTO, order, user);
        Payment savedPayment = paymentService.save(payment);

        return new ResponseEntity<>(PaymentMapper.toResponseDTO(savedPayment), HttpStatus.CREATED);
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

    @PostMapping("/{id}/success")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<PaymentResponseDTO> markSuccess(@PathVariable int id) {
        return ResponseEntity.ok(
                PaymentMapper.toResponseDTO(paymentService.markSuccess(id))
        );
    }


    @PostMapping("/{id}/failed")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<PaymentResponseDTO> markFailed(@PathVariable int id) {
        return ResponseEntity.ok(
                PaymentMapper.toResponseDTO(paymentService.markFailed(id))
        );
    }

    @PostMapping("/{id}/refund")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<PaymentResponseDTO> refund(@PathVariable int id) {
        return ResponseEntity.ok(
                PaymentMapper.toResponseDTO(paymentService.refund(id))
        );
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<PaymentResponseDTO> cancel(@PathVariable int id) {
        return ResponseEntity.ok(
                PaymentMapper.toResponseDTO(paymentService.cancel(id))
        );
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