package com.utsav.arts.controllers;

import com.utsav.arts.dtos.paymentDTO.PaymentRequestDTO;
import com.utsav.arts.dtos.paymentDTO.PaymentResponseDTO;
import com.utsav.arts.mappers.PaymentMapper;
import com.utsav.arts.models.Orders;
import com.utsav.arts.models.Payment;
import com.utsav.arts.models.PaymentStatus;
import com.utsav.arts.models.User;
import com.utsav.arts.services.OrdersService;
import com.utsav.arts.services.PaymentService;
import com.utsav.arts.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<PaymentResponseDTO> save(@RequestBody PaymentRequestDTO requestDTO) {
        User user = userService.findById(requestDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Orders order = ordersService.findById(requestDTO.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        Payment payment = PaymentMapper.toEntity(requestDTO, order, user);
        Payment savedPayment = paymentService.save(payment);

        return new ResponseEntity<>(PaymentMapper.toResponseDTO(savedPayment), HttpStatus.CREATED);
    }

    // ---------------- READ ----------------
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('OWNER') or @paymentService.isPaymentOwner(#id, authentication.principal.id)")
    public ResponseEntity<PaymentResponseDTO> findById(@PathVariable int id) {
        return paymentService.findById(id)
                .map(payment -> ResponseEntity.ok(PaymentMapper.toResponseDTO(payment)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('OWNER') or #userId == authentication.principal.id")
    public ResponseEntity<List<PaymentResponseDTO>> findByUserId(@PathVariable int userId) {
        List<PaymentResponseDTO> payments = paymentService.findByUserId(userId)
                .stream()
                .map(PaymentMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasRole('OWNER') or @ordersService.isOrderOwner(#orderId, authentication.principal.id)")
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
            return ResponseEntity.badRequest().build();
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

    // ---------------- DELETE ----------------
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Void> deleteById(@PathVariable int id) {
        paymentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}