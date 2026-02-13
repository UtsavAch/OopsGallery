/**
 * PaymentStatus
 *
 * Represents the current state of a payment transaction.
 *
 * Possible values:
 * - "PENDING" → Payment has been initiated but not completed
 * - "SUCCESS" → Payment successfully processed
 * - "FAILED"  → Payment attempt failed
 *
 * Used to track the lifecycle of a payment.
 */
export type PaymentStatus = "PENDING" | "SUCCESS" | "FAILED";

/**
 * PaymentResponse
 *
 * Represents a payment record returned from the backend.
 * This reflects the persisted payment entity associated with an order.
 *
 * Contains:
 * - id: Unique identifier of the payment
 * - orderId: Associated order ID
 * - userId: ID of the user who made the payment
 * - amount: Total payment amount
 * - currency: Currency used for the transaction (e.g., "USD", "EUR")
 * - method: Payment method used (e.g., "CARD", "STRIPE")
 * - status: Current status of the payment
 * - transactionId: External transaction reference (e.g., Stripe payment ID)
 * - createdAt: Timestamp when the payment was created (ISO string format)
 *
 * Note:
 * - amount is mapped from Java BigDecimal to TypeScript number.
 * - createdAt is serialized from Java LocalDateTime to ISO string.
 */
export interface PaymentResponse {
  id: number;
  orderId: number;
  userId: number;
  amount: number; // BigDecimal in Java maps to number in TS
  currency: string;
  method: string;
  status: PaymentStatus;
  transactionId: string;
  createdAt: string; // ISO string from LocalDateTime
}

/**
 * CreatePaymentIntentRequest
 *
 * Represents the payload sent from the frontend
 * when requesting a new payment intent from the backend.
 *
 * Input:
 * - orderId: ID of the order being paid for
 * - currency: Currency in which the payment should be processed
 *
 * Typically used before confirming a card payment
 * (e.g., when integrating with Stripe).
 */
export interface CreatePaymentIntentRequest {
  orderId: number;
  currency: string; // e.g., "USD" or "EUR"
}

/**
 * PaymentIntentResponse
 *
 * Represents the response returned after creating
 * a payment intent with the payment provider.
 *
 * Contains:
 * - clientSecret: Secret key required by the frontend
 *   to securely complete the payment confirmation process.
 *
 * This value is typically used with Stripe's client SDK.
 */
export interface PaymentIntentResponse {
  clientSecret: string;
}
