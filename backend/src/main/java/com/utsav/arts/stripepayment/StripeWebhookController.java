package com.utsav.arts.stripepayment;

import com.stripe.exception.EventDataObjectDeserializationException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Charge;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.utsav.arts.models.*;
import com.utsav.arts.services.OrdersService;
import com.utsav.arts.services.PaymentService;
import com.utsav.arts.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/stripe")
public class StripeWebhookController {

    private static final Logger logger = LoggerFactory.getLogger(StripeWebhookController.class);

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
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader
    ) {
        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            logger.error("Webhook signature verification failed", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Webhook signature verification failed");
        } catch (Exception e) {
            logger.error("Error parsing webhook payload", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error parsing webhook payload");
        }

        try {
            switch (event.getType()) {

                case "payment_intent.succeeded" -> {
                    EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();

                    PaymentIntent intent = deserializer.getObject()
                            .map(obj -> (PaymentIntent) obj)
                            .orElseGet(() -> {
                                try {
                                    logger.warn("Using unsafe deserialization for event {}", event.getId());
                                    return (PaymentIntent) deserializer.deserializeUnsafe();
                                } catch (EventDataObjectDeserializationException e) {
                                    throw new RuntimeException("Failed to deserialize PaymentIntent", e);
                                }
                            });

                    handleSuccess(intent);
                }

                case "payment_intent.payment_failed" -> {
                    PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer()
                            .getObject().orElse(null);
                    if (intent != null) {
                        handleFailure(intent);
                    } else {
                        logger.warn("Failed to deserialize PaymentIntent for failed event");
                    }
                }

                default -> logger.info("Unhandled event type: {}", event.getType());
            }

            return ResponseEntity.ok("Webhook received");

        } catch (Exception e) {
            logger.error("Error handling webhook event", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error handling webhook event");
        }
    }

    private void handleSuccess(PaymentIntent intent) {
        logger.info("Processing payment_intent.succeeded for {}", intent.getId());

        if (intent.getMetadata() == null ||
                !intent.getMetadata().containsKey("order_id") ||
                !intent.getMetadata().containsKey("user_id")) {
            logger.warn("PaymentIntent missing metadata: order_id or user_id");
            return;
        }

        String orderIdStr = intent.getMetadata().get("order_id");
        String userIdStr = intent.getMetadata().get("user_id");

        if (orderIdStr == null || userIdStr == null) {
            logger.error("Missing metadata in PaymentIntent: order_id={}, user_id={}", orderIdStr, userIdStr);
            return; // Stop processing instead of throwing an exception that causes a 500
        }

        int orderId = Integer.parseInt(orderIdStr);
        int userId = Integer.parseInt(userIdStr);

        Orders order = ordersService.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        // Idempotency check
        if (paymentService.findByTransactionId(intent.getId()).isPresent()) {
            logger.info("Payment already processed: {}", intent.getId());
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

        logger.info("Payment succeeded and saved for order {}", orderId);
    }

    private void handleFailure(PaymentIntent intent) {
        paymentService.findByTransactionId(intent.getId())
                .ifPresent(payment -> {
                    payment.setStatus(PaymentStatus.FAILED);
                    paymentService.save(payment);
                    logger.info("Payment failed for transaction {}", intent.getId());
                });
    }
}