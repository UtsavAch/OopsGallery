import api from "../api";
import type {
  OrderStatus,
  OrdersRequest,
  OrdersResponse,
} from "./orders.types";

export const ordersService = {
  /**
   * Places a new order using the current user's cart items.
   *
   * @see {@link OrdersRequest}
   * @see {@link OrdersResponse}
   */
  async placeOrder(data: OrdersRequest): Promise<OrdersResponse> {
    const response = await api.post<OrdersResponse>("/orders", data);
    return response.data;
  },

  /**
   * Retrieves a specific order by ID.
   *
   * @see {@link OrdersResponse}
   */
  async getOrderById(id: number): Promise<OrdersResponse> {
    const response = await api.get<OrdersResponse>(`/orders/${id}`);
    return response.data;
  },

  /**
   * Retrieves all orders (Admin/Owner only).
   *
   * @see {@link OrdersResponse}
   */
  async getAllOrders(): Promise<OrdersResponse[]> {
    const response = await api.get<OrdersResponse[]>("/orders");
    return response.data;
  },

  /**
   * Retrieves all orders for a specific user.
   *
   * @see {@link OrdersResponse}
   */
  async getOrdersByUserId(userId: number): Promise<OrdersResponse[]> {
    const response = await api.get<OrdersResponse[]>(`/orders/user/${userId}`);
    return response.data;
  },

  /**
   * Retrieves orders filtered by status (Admin/Owner only).
   *
   * @see {@link OrdersStatus}
   * @see {@link OrdersResponse}
   */
  async getOrdersByStatus(status: OrderStatus): Promise<OrdersResponse[]> {
    const response = await api.get<OrdersResponse[]>(
      `/orders/status/${status}`,
    );
    return response.data;
  },

  // ---------------- STATUS UPDATES ----------------

  /**
   * Confirm an order. Can be called by OWNER only.
   *
   * @see {@link OrdersResponse}
   */
  async confirmOrder(id: number): Promise<OrdersResponse> {
    const response = await api.post<OrdersResponse>(`/orders/${id}/confirm`);
    return response.data;
  },

  /**
   * Ship an order. Can be called by OWNER only.
   *
   * @see {@link OrdersResponse}
   */
  async shipOrder(id: number): Promise<OrdersResponse> {
    const response = await api.post<OrdersResponse>(`/orders/${id}/ship`);
    return response.data;
  },

  /**
   * Deliver an order. Can be called by OWNER only.
   *
   * @see {@link OrdersResponse}
   */
  async deliverOrder(id: number): Promise<OrdersResponse> {
    const response = await api.post<OrdersResponse>(`/orders/${id}/deliver`);
    return response.data;
  },

  /**
   * Cancels an order. Can be called by OWNER or the order owner.
   *
   * @see {@link OrdersResponse}
   */
  async cancelOrder(id: number): Promise<OrdersResponse> {
    const response = await api.post<OrdersResponse>(`/orders/${id}/cancel`);
    return response.data;
  },

  /**
   * Deletes an order record (Admin/Owner only).
   */
  async deleteOrder(id: number): Promise<void> {
    await api.delete(`/orders/${id}`);
  },
};
