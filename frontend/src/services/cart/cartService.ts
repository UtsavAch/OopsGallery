import api from "../api";
import type {
  CartRequest,
  CartResponse,
  CartItemRequest,
  CartItemResponse,
} from "./cart.types";

export const cartService = {
  // ==========================================
  // CART ENDPOINTS (/api/carts)
  // ==========================================

  /**
   * Creates a new cart for a user.
   *
   * @see {@link CartResponse}
   */
  async createCart(userId: number): Promise<CartResponse> {
    const payload: CartRequest = { userId };
    const response = await api.post<CartResponse>("/carts", payload);
    return response.data;
  },

  /**
   * Retrieves a cart by its ID.
   *
   * @see {@link CartResponse}
   */
  async getCartById(id: number): Promise<CartResponse> {
    const response = await api.get<CartResponse>(`/carts/${id}`);
    return response.data;
  },

  /**
   * Retrieves a cart by the user ID.
   *
   * @see {@link CartResponse}
   */
  async getCartByUserId(userId: number): Promise<CartResponse> {
    const response = await api.get<CartResponse>(`/carts/user/${userId}`);
    return response.data;
  },

  /**
   * Deletes a cart by its ID.
   */
  async deleteCart(id: number): Promise<void> {
    await api.delete(`/carts/${id}`);
  },

  // ==========================================
  // CART ITEM ENDPOINTS (/api/cart-items)
  // ==========================================

  /**
   * Adds a new item to a cart.
   *
   * @see {@link CartItemRequest}
   * @see {@link CartItemResponse}
   */
  async addItemToCart(data: CartItemRequest): Promise<CartItemResponse> {
    const response = await api.post<CartItemResponse>("/cart-items", data);
    return response.data;
  },

  /**
   * Updates an existing cart item (quantity or artwork) by ID.
   *
   * @see {@link CartItemRequest}
   * @see {@link CartItemResponse}
   */
  async updateItem(
    id: number,
    data: CartItemRequest,
  ): Promise<CartItemResponse | null> {
    const response = await api.put<CartItemResponse>(`/cart-items/${id}`, data);
    // Returns 204 No Content if updatedItem is null in backend, so handle empty response
    return response.data || null;
  },

  /**
   * Retrieves a cart item by ID.
   *
   * @see {@link CartItemResponse}
   */
  async getItemById(id: number): Promise<CartItemResponse> {
    const response = await api.get<CartItemResponse>(`/cart-items/${id}`);
    return response.data;
  },

  /**
   * Retrieves all items for a given cart (id).
   *
   * @see {@link CartItemResponse}
   */
  async getItemsByCartId(cartId: number): Promise<CartItemResponse[]> {
    const response = await api.get<CartItemResponse[]>(
      `/cart-items/cart/${cartId}`,
    );
    return response.data;
  },

  /**
   * Deletes a cart item by ID.
   */
  async removeItem(id: number): Promise<void> {
    await api.delete(`/cart-items/${id}`);
  },

  /**
   * Deletes all items in a cart.
   */
  async clearCart(cartId: number): Promise<void> {
    await api.delete(`/cart-items/cart/${cartId}`);
  },

  /**
   * Increases the quantity of a cart item by 1.
   */
  async increaseQuantity(id: number): Promise<void> {
    await api.patch(`/cart-items/${id}/increase`);
  },

  /**
   * Decreases the quantity of a cart item by 1.
   */
  async decreaseQuantity(id: number): Promise<void> {
    await api.patch(`/cart-items/${id}/decrease`);
  },
};
