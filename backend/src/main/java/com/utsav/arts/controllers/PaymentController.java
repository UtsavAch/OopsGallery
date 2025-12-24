package com.utsav.arts.controllers;

import com.utsav.arts.dtos.paymentDTO.PaymentRequestDTO;
import com.utsav.arts.dtos.paymentDTO.PaymentResponseDTO;
import com.utsav.arts.mappers.PaymentMapper;
import com.utsav.arts.models.Orders;
import com.utsav.arts.models.Payment;
import com.utsav.arts.models.User;
import com.utsav.arts.services.OrdersService;
import com.utsav.arts.services.PaymentService;
import com.utsav.arts.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final UserService userService;
    private final OrdersService ordersService;

    public PaymentController(
            PaymentService paymentService,
            UserService userService,
            OrdersService ordersService
    ) {
        this.paymentService = paymentService;
        this.userService = userService;
        this.ordersService = ordersService;
    }

    // ---------------- CREATE ----------------
    @PostMapping
    public ResponseEntity<PaymentResponseDTO> save(
            @RequestBody PaymentRequestDTO requestDTO
    ) {
        User user = userService.findById(requestDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Orders order = ordersService.findById(requestDTO.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        Payment payment =
                PaymentMapper.toEntity(requestDTO, order, user);

        Payment savedPayment = paymentService.save(payment);

        return new ResponseEntity<>(
                PaymentMapper.toResponseDTO(savedPayment),
                HttpStatus.CREATED
        );
    }

    // ---------------- UPDATE ----------------
    @PutMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> update(
            @PathVariable int id,
            @RequestBody PaymentRequestDTO requestDTO
    ) {
        User user = userService.findById(requestDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Orders order = ordersService.findById(requestDTO.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        Payment payment =
                PaymentMapper.toEntity(requestDTO, order, user);
        payment.setId(id);

        Payment updatedPayment = paymentService.update(payment);

        return ResponseEntity.ok(
                PaymentMapper.toResponseDTO(updatedPayment)
        );
    }

    // ---------------- READ ----------------
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> findById(@PathVariable int id) {
        return paymentService.findById(id)
                .map(p -> ResponseEntity.ok(
                        PaymentMapper.toResponseDTO(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<PaymentResponseDTO> findByTransactionId(
            @PathVariable String transactionId
    ) {
        return paymentService.findByTransactionId(transactionId)
                .map(p -> ResponseEntity.ok(
                        PaymentMapper.toResponseDTO(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentResponseDTO>> findByUserId(
            @PathVariable int userId
    ) {
        List<PaymentResponseDTO> payments =
                paymentService.findByUserId(userId)
                        .stream()
                        .map(PaymentMapper::toResponseDTO)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(payments);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentResponseDTO>> findByOrderId(
            @PathVariable int orderId
    ) {
        List<PaymentResponseDTO> payments =
                paymentService.findByOrderId(orderId)
                        .stream()
                        .map(PaymentMapper::toResponseDTO)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(payments);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PaymentResponseDTO>> findByStatus(
            @PathVariable String status
    ) {
        List<PaymentResponseDTO> payments =
                paymentService.findByStatus(status)
                        .stream()
                        .map(PaymentMapper::toResponseDTO)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(payments);
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponseDTO>> findAll() {
        List<PaymentResponseDTO> payments =
                paymentService.findAll()
                        .stream()
                        .map(PaymentMapper::toResponseDTO)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(payments);
    }

    // ---------------- DELETE ----------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable int id) {
        paymentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
