# Online Art Gallery

## Concept

An online art gallery where users can browse, order, and purchase artworks through a simple and secure checkout process.

---

## Database

### Schema

![Database Schema](./docs/images/schema.png)

---

## Backend

### Order and Payment Flow

The order and payment system follows a clear, status-based flow to ensure consistency and reliability.

- **Order Placed**  
  When a user places an order, an **Order** is created with status `PENDING`.  
  A corresponding **Payment** record is also created with status `PENDING`.

- **Payment Success**  
  When the payment status transitions to `SUCCESS`, the associated **Order** automatically transitions to `CONFIRMED`.

- **Payment Failed**  
  If the payment fails, the **Order** remains `PENDING`, allowing the user to retry the payment.  
  If payment is not completed after a certain period, the order may transition to `CANCELLED`.

- **Order Cancelled**  
  If an order is cancelled after a successful payment, a **refund should be triggered**.

---
