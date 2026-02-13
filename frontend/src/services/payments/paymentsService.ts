import api from "../api";
import type {
  PaymentResponse,
  CreatePaymentIntentRequest,
  PaymentIntentResponse,
  PaymentStatus,
} from "./payments.types";

export const paymentService = {
  // ---------------- AUTHENTICATED USER ENDPOINTS ----------------

  /**
   * Initiates a Stripe payment by creating a PaymentIntent.
   * Returns the clientSecret used by Stripe Elements on the frontend.
   *
   * @see {@link CreatePaymentIntentRequest}
   * @see {@link PaymentIntentResponse}
   */
  async createPaymentIntent(
    data: CreatePaymentIntentRequest,
  ): Promise<PaymentIntentResponse> {
    const response = await api.post<PaymentIntentResponse>(
      "/payments/intent",
      data,
    );
    return response.data;
  },

  /**
   * Retrieves a payment by its ID.
   *
   * @see {@link PaymentResponse}
   */
  async getPaymentById(id: number): Promise<PaymentResponse> {
    const response = await api.get<PaymentResponse>(`/payments/${id}`);
    return response.data;
  },

  /**
   * Retrieves all payments associated with a specific user.
   *
   * @see {@link PaymentResponse}
   */
  async getPaymentsByUserId(userId: number): Promise<PaymentResponse[]> {
    const response = await api.get<PaymentResponse[]>(
      `/payments/user/${userId}`,
    );
    return response.data;
  },

  /**
   * Retrieves all payments associated with a specific order.
   *
   * @see {@link PaymentResponse}
   */
  async getPaymentsByOrderId(orderId: number): Promise<PaymentResponse[]> {
    const response = await api.get<PaymentResponse[]>(
      `/payments/order/${orderId}`,
    );
    return response.data;
  },

  // ---------------- OWNER / ADMIN ENDPOINTS ----------------

  /**
   * Retrieves all payments in the system.
   *
   * @see {@link PaymentResponse}
   */
  async getAllPayments(): Promise<PaymentResponse[]> {
    const response = await api.get<PaymentResponse[]>("/payments");
    return response.data;
  },

  /**
   * Retrieves all payments filtered by payment status.
   *
   * @see {@link PaymentStatus}
   * @see {@link PaymentResponse}
   */
  async getPaymentsByStatus(status: PaymentStatus): Promise<PaymentResponse[]> {
    const response = await api.get<PaymentResponse[]>(
      `/payments/status/${status}`,
    );
    return response.data;
  },

  /**
   * Deletes a payment by ID.
   */
  async deletePayment(id: number): Promise<void> {
    await api.delete(`/payments/${id}`);
  },

  /**
   * Retrieves all valid string values for PaymentStatus.
   */
  async getPaymentStatuses(): Promise<string[]> {
    const response = await api.get<string[]>("/payments/payment-statuses");
    return response.data;
  },
};
