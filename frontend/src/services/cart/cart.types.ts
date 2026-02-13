/**
 * CartItemRequest
 *
 * Represents the payload sent from the frontend to the backend
 * when adding or updating an item in a cart.
 *
 * Input:
 * - cartId: ID of the cart where the item should be added
 * - artworkId: ID of the artwork to add
 * - quantity: Number of units of the artwork
 *
 * This interface is used in POST/PUT requests.
 */
export interface CartItemRequest {
  cartId: number;
  artworkId: number;
  quantity: number;
}

/**
 * CartItemResponse
 *
 * Represents a cart item returned by the backend.
 * This reflects the persisted cart item entity.
 *
 * Contains:
 * - id: Unique cart item identifier
 * - cartId: Associated cart ID
 * - artworkId: Associated artwork ID
 * - quantity: Number of units in the cart
 *
 * This interface is used in API responses.
 */
export interface CartItemResponse {
  id: number;
  cartId: number;
  artworkId: number;
  quantity: number;
}

/**
 * CartRequest
 *
 * Represents the payload used to create a new cart.
 *
 * Input:
 * - userId: ID of the user who owns the cart
 *
 * Typically used when initializing a cart for a user.
 */
export interface CartRequest {
  userId: number;
}

/**
 * CartResponse
 *
 * Represents the full cart object returned from the backend.
 * Includes all cart items and computed totals.
 *
 * Contains:
 * - id: Cart identifier
 * - userId: Owner of the cart
 * - cartItems: List of items inside the cart
 * - totalItems: Total quantity of all items
 * - totalPrice: Total price of the cart
 *
 * Note:
 * - totalPrice is mapped from Java BigDecimal to TypeScript number.
 */
export interface CartResponse {
  id: number;
  userId: number;
  cartItems: CartItemResponse[];
  totalItems: number;
  totalPrice: number; // Stays as a number in TS, mapped from BigDecimal in Java
}
