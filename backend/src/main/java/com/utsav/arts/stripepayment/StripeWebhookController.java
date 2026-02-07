package com.utsav.arts.stripepayment;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.utsav.arts.models.*;
import com.utsav.arts.services.OrdersService;
import com.utsav.arts.services.PaymentService;
import com.utsav.arts.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/stripe")
public class StripeWebhookController {

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    private final PaymentService paymentService;
    private final OrdersService ordersService;
    private final UserService userService;

    public StripeWebhookController(
            PaymentService paymentService,
            OrdersService ordersService,
            UserService userService
    ) {
        this.paymentService = paymentService;
        this.ordersService = ordersService;
        this.userService = userService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader
    ) {
        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.ok().build();
        }

        switch (event.getType()) {

            case "payment_intent.succeeded" -> {
                PaymentIntent intent = event.getDataObjectDeserializer()
                        .getObject().map(obj -> (PaymentIntent) obj).orElseThrow();

                handleSuccess(intent);
            }

            case "payment_intent.payment_failed" -> {
                PaymentIntent intent = event.getDataObjectDeserializer()
                        .getObject().map(obj -> (PaymentIntent) obj).orElseThrow();

                handleFailure(intent);
            }
        }

        return ResponseEntity.ok().build();
    }

    private void handleSuccess(PaymentIntent intent) {

        if (intent.getMetadata() == null ||
                !intent.getMetadata().containsKey("order_id") ||
                !intent.getMetadata().containsKey("user_id")) {
            return;
        }

        int orderId = Integer.parseInt(intent.getMetadata().get("order_id"));
        int userId  = Integer.parseInt(intent.getMetadata().get("user_id"));

        Orders order = ordersService.findById(orderId)
                .orElseThrow();

        User user = userService.findById(userId)
                .orElseThrow();

        if (order.getStatus() != OrderStatus.PENDING) {
            return;
        }

        // Idempotency check
        if (paymentService.findByTransactionId(intent.getId()).isPresent()) {
            return;
        }

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setUser(user);
        payment.setAmount(BigDecimal.valueOf(intent.getAmount()).movePointLeft(2));
        payment.setCurrency(intent.getCurrency().toUpperCase());
        payment.setMethod(
                intent.getPaymentMethodTypes().isEmpty()
                        ? "UNKNOWN"
                        : intent.getPaymentMethodTypes().getFirst().toUpperCase()
        );
        payment.setTransactionId(intent.getId());
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setCreatedAt(LocalDateTime.now());

        paymentService.save(payment);
    }

    private void handleFailure(PaymentIntent intent) {
        paymentService.findByTransactionId(intent.getId())
                .ifPresent(payment -> {
                    payment.setStatus(PaymentStatus.FAILED);
                    paymentService.save(payment);
                });
    }
}

