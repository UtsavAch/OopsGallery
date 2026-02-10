package com.utsav.arts.repository;

import com.utsav.arts.models.OrderStatus;
import com.utsav.arts.models.Orders;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * JPA implementation of {@link OrdersRepository}.
 * <p>
 * Uses {@link EntityManager} for persistence and explicitly fetches
 * order items to avoid lazy loading issues.
 */
@Repository
@Transactional
public class OrdersRepositoryImpl implements OrdersRepository {

    /**
     * JPA EntityManager used for database operations.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public Orders save(Orders order) {
        return entityManager.merge(order);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Orders update(Orders order) {
        return entityManager.merge(order);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Fetches order items eagerly to ensure the order is fully initialized.
     */
    @Override
    public Optional<Orders> findById(int id) {
        try {
            Orders order = entityManager.createQuery(
                            "SELECT o FROM Orders o LEFT JOIN FETCH o.orderItems WHERE o.id = :id",
                            Orders.class
                    ).setParameter("id", id)
                    .getSingleResult();
            return Optional.of(order);
        } catch (jakarta.persistence.NoResultException e) {
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Orders are returned in descending order of creation time.
     */
    @Override
    public List<Orders> findAll() {
        return entityManager.createQuery(
                "SELECT DISTINCT o FROM Orders o LEFT JOIN FETCH o.orderItems ORDER BY o.orderedAt DESC",
                Orders.class
        ).getResultList();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Fetches order items eagerly and sorts results by most recent orders first.
     */
    @Override
    public List<Orders> findByUserId(int userId) {
        return entityManager.createQuery(
                        "SELECT DISTINCT o FROM Orders o LEFT JOIN FETCH o.orderItems WHERE o.user.id = :userId ORDER BY o.orderedAt DESC",
                        Orders.class
                ).setParameter("userId", userId)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Orders> findByArtworkId(int artworkId) {
        // FIXED: Joined 'orderItems' because 'Orders' no longer has a direct 'artwork' field.
        // DISTINCT is used so we don't get duplicate Orders if the same order has the artwork listed twice (rare, but safe).
        return entityManager.createQuery(
                        "SELECT DISTINCT o FROM Orders o JOIN o.orderItems oi WHERE oi.artwork.id = :artworkId ORDER BY o.orderedAt DESC",
                        Orders.class
                )
                .setParameter("artworkId", artworkId)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Orders> findByStatus(OrderStatus status) {
        return entityManager.createQuery(
                        "SELECT o FROM Orders o WHERE o.status = :status ORDER BY o.orderedAt DESC",
                        Orders.class
                )
                .setParameter("status", status)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteById(int id) {
        Orders order = entityManager.find(Orders.class, id);
        if (order != null) {
            entityManager.remove(order);
        }
    }
}