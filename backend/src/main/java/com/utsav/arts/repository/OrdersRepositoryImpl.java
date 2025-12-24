package com.utsav.arts.repository;

import com.utsav.arts.models.Orders;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class OrdersRepositoryImpl implements OrdersRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Orders save(Orders order) {
        entityManager.persist(order);
        return order;
    }

    @Override
    public Orders update(Orders order) {
        return entityManager.merge(order);
    }

    @Override
    public Optional<Orders> findById(int id) {
        return Optional.ofNullable(entityManager.find(Orders.class, id));
    }

    @Override
    public List<Orders> findAll() {
        return entityManager.createQuery(
                "SELECT o FROM Orders o ORDER BY o.orderedAt DESC",
                Orders.class
        ).getResultList();
    }

    @Override
    public List<Orders> findByUserId(int userId) {
        return entityManager.createQuery(
                        "SELECT o FROM Orders o WHERE o.user.id = :userId ORDER BY o.orderedAt DESC",
                        Orders.class
                )
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Orders> findByArtworkId(int artworkId) {
        return entityManager.createQuery(
                        "SELECT o FROM Orders o WHERE o.artwork.id = :artworkId ORDER BY o.orderedAt DESC",
                        Orders.class
                )
                .setParameter("artworkId", artworkId)
                .getResultList();
    }

    @Override
    public List<Orders> findByStatus(String status) {
        return entityManager.createQuery(
                        "SELECT o FROM Orders o WHERE o.status = :status ORDER BY o.orderedAt DESC",
                        Orders.class
                )
                .setParameter("status", status)
                .getResultList();
    }

    @Override
    public void deleteById(int id) {
        Orders order = entityManager.find(Orders.class, id);
        if (order != null) {
            entityManager.remove(order);
        }
    }
}
