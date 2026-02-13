/**
 * OrderStatus
 *
 * Represents the possible states of an order in the system.
 *
 * Possible values:
 * - "PENDING"   → Order created but not yet confirmed
 * - "CONFIRMED" → Order confirmed by the system
 * - "SHIPPED"   → Order dispatched to the customer
 * - "DELIVERED" → Order successfully delivered
 * - "CANCELLED" → Order cancelled by user or system
 *
 * Used to track the lifecycle of an order.
 */
export type OrderStatus =
  | "PENDING"
  | "CONFIRMED"
  | "SHIPPED"
  | "DELIVERED"
  | "CANCELLED";

/**
 * OrderItemResponse
 *
 * Represents a single item within an order.
 * Returned by the backend as part of an order response.
 *
 * Contains:
 * - id: Unique identifier of the order item
 * - artworkId: ID of the purchased artwork
 * - artworkTitle: Title of the artwork at time of purchase
 * - artworkImgUrl: Image URL of the artwork
 * - quantity: Number of units purchased
 * - priceAtPurchase: Price per unit at the time the order was placed
 *
 * Note:
 * - priceAtPurchase ensures historical price consistency,
 *   even if artwork price changes later.
 */
export interface OrderItemResponse {
  id: number;
  artworkId: number;
  artworkTitle: string;
  artworkImgUrl: string;
  quantity: number;
  priceAtPurchase: number;
}

/**
 * OrdersRequest
 *
 * Represents the payload sent from the frontend
 * when creating a new order.
 *
 * Input:
 * - address: Shipping address where the order should be delivered
 *
 * The backend typically derives the userId from authentication (JWT),
 * so it is not included here.
 */
export interface OrdersRequest {
  address: string;
}

/**
 * OrdersResponse
 *
 * Represents the complete order object returned from the backend.
 *
 * Contains:
 * - id: Unique identifier of the order
 * - userId: ID of the user who placed the order
 * - items: List of purchased items
 * - totalPrice: Total cost of the order
 * - address: Shipping address
 * - status: Current order status
 * - orderedAt: Timestamp of when the order was placed (ISO string format)
 *
 * Note:
 * - orderedAt is serialized from Java LocalDateTime to ISO string.
 */
export interface OrdersResponse {
  id: number;
  userId: number;
  items: OrderItemResponse[];
  totalPrice: number;
  address: string;
  status: OrderStatus;
  orderedAt: string; // ISO string from LocalDateTime
}
