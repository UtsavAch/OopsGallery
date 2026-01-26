package com.utsav.arts.services;

import com.utsav.arts.models.Orders;
import com.utsav.arts.repository.OrdersRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("ordersService")
@Transactional
public class OrdersServiceImpl implements OrdersService {

    private final OrdersRepository ordersRepository;

    public OrdersServiceImpl(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    @Override
    public Orders save(Orders order) {
        // Set order timestamp if not already set
        if (order.getOrderedAt() == null) {
            order.setOrderedAt(java.time.LocalDateTime.now());
        }

        // Initial status if not set
        if (order.getStatus() == null || order.getStatus().isBlank()) {
            order.setStatus("PENDING");
        }

        return ordersRepository.save(order);
    }

    @Override
    public Orders update(Orders order) {
        ordersRepository.findById(order.getId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

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
    public List<Orders> findByArtworkId(int artworkId) {
        return ordersRepository.findByArtworkId(artworkId);
    }

    @Override
    public List<Orders> findByStatus(String status) {
        return ordersRepository.findByStatus(status);
    }

    @Override
    public void deleteById(int id) {
        if (ordersRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Order not found");
        }
        ordersRepository.deleteById(id);
    }

    @Override
    public boolean isOrderOwner(int orderId, String email) {
        return findById(orderId)
                .map(order -> order.getUser().getEmail().equals(email))
                .orElse(false); // If order doesn't exist, access is denied safely
    }
}
