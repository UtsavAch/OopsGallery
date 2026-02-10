package com.utsav.arts.stripepayment;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Service for interacting with Stripe APIs to manage payments.
 *
 * <p>Currently supports creating Stripe PaymentIntents with automatic payment methods enabled.
 *
 * <p>Stripe API key is injected from application properties and initialized after construction.
 */
@Service
public class StripeService {

    @Value("${stripe.api.key}")
    private String secretKey;

    /**
     * Initializes the Stripe API client with the secret key.
     * <p>This method is called automatically after the service is constructed.
     */
    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    /**
     * Creates a Stripe PaymentIntent for a given order and user.
     *
     * <p>The amount is converted to the smallest currency unit (e.g., cents) and
     * automatic payment methods are enabled with no redirects.
     * Metadata includes order ID and user ID for tracking purposes.
     *
     * @param amount the payment amount in standard units (e.g., 10.50 for €10.50)
     * @param currency the 3-letter ISO currency code (e.g., EUR, USD)
     * @param orderId the ID of the order being paid
     * @param userId the ID of the user making the payment
     * @return the client secret of the created PaymentIntent
     * @throws IllegalArgumentException if the amount is zero or negative
     * @throws Exception if Stripe API call fails
     */
    public String createPaymentIntent(
            BigDecimal amount,
            String currency,
            int orderId,
            int userId
    ) throws Exception {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid payment amount");
        }

        long amountInCents = amount
                .movePointRight(2)
                .setScale(0, RoundingMode.HALF_UP)
                .longValueExact();

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(amountInCents)
                        .setCurrency(currency.trim().toLowerCase())
                        .setAutomaticPaymentMethods(
                                PaymentIntentCreateParams.AutomaticPaymentMethods
                                        .builder()
                                        .setEnabled(true)
                                        .setAllowRedirects(PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER)
                                        .build()
                        )
                        .putMetadata("order_id", String.valueOf(orderId))
                        .putMetadata("user_id", String.valueOf(userId))
                        .build();

        PaymentIntent intent = PaymentIntent.create(params);
        return intent.getClientSecret();
    }

}