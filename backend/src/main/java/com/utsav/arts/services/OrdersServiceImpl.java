package com.utsav.arts.services;

import com.utsav.arts.models.*;
import com.utsav.arts.repository.CartRepository;
import com.utsav.arts.repository.OrdersRepository;
import com.utsav.arts.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service("ordersService")
@Transactional
public class OrdersServiceImpl implements OrdersService {

    private final OrdersRepository ordersRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartItemService cartItemService;

    public OrdersServiceImpl(OrdersRepository ordersRepository,
                             CartRepository cartRepository,
                             UserRepository userRepository,
                             CartItemService cartItemService) {
        this.ordersRepository = ordersRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.cartItemService = cartItemService;
    }

    @Override
    public Orders placeOrder(int userId, String address) {

        // 1. Fetch User
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 2. Fetch Cart
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cannot place order: Cart is empty");
        }

        Orders order = new Orders();
        order.setUser(user);
        order.setAddress(address);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderedAt(LocalDateTime.now());

        int total = 0;

        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setArtwork(cartItem.getArtwork());
            orderItem.setQuantity(cartItem.getQuantity());

            BigDecimal price = cartItem.getArtwork().getPrice();
            orderItem.setPriceAtPurchase(price);

            order.addOrderItem(orderItem);
            total += price.multiply(BigDecimal.valueOf(cartItem.getQuantity())).intValue();
        }

        order.setTotalPrice(BigDecimal.valueOf(total));

        Orders saved = ordersRepository.save(order);

        cartItemService.deleteByCartId(cart.getId());

        return saved;
    }

    @Override
    public Orders updateStatus(int orderId, OrderStatus status) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        order.setStatus(status);
        return ordersRepository.update(order);
    }

    @Override
    public Optional<Orders> findById(int id) {
        return ordersRepository.findById(id);
    }

    @Override
    public List<Orders> findAll() {
        return ordersRepository.findAll();
    }

    @Override
    public List<Orders> findByUserId(int userId) {
        return ordersRepository.findByUserId(userId);
    }

    @Override
    public List<Orders> findByStatus(OrderStatus status) {
        return ordersRepository.findByStatus(status);
    }

    @Override
    public void deleteById(int id) {
        ordersRepository.deleteById(id);
    }

    @Override
    public boolean isOwner(int orderId, int userId) {
        return ordersRepository.findById(orderId)
                .map(order -> order.getUser().getId() == userId)
                .orElse(false);
    }
}