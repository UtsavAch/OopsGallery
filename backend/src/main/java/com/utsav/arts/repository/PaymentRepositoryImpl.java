package com.utsav.arts.repository;

import com.utsav.arts.models.Payment;
import com.utsav.arts.models.PaymentStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * JPA-based implementation of {@link PaymentRepository}.
 *
 * <p>
 * Uses {@link EntityManager} to perform persistence operations
 * and JPQL queries for {@link Payment} entities.
 * </p>
 */
@Repository
@Transactional
public class PaymentRepositoryImpl implements PaymentRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Payment save(Payment payment) {
        entityManager.persist(payment);
        return payment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Payment update(Payment payment) {
        return entityManager.merge(payment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Payment> findById(int id) {
        return Optional.ofNullable(entityManager.find(Payment.class, id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Payment> findByTransactionId(String transactionId) {
        try {
            Payment payment = entityManager.createQuery(
                            "SELECT p FROM Payment p WHERE p.transactionId = :txId",
                            Payment.class
                    )
                    .setParameter("txId", transactionId)
                    .getSingleResult();

            return Optional.of(payment);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Payment> findByUserId(int userId) {
        return entityManager.createQuery(
                        "SELECT p FROM Payment p WHERE p.user.id = :userId ORDER BY p.createdAt DESC",
                        Payment.class
                )
                .setParameter("userId", userId)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Payment> findByOrderId(int orderId) {
        return entityManager.createQuery(
                        "SELECT p FROM Payment p WHERE p.order.id = :orderId",
                        Payment.class
                )
                .setParameter("orderId", orderId)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Payment> findByStatus(PaymentStatus status) {
        return entityManager.createQuery(
                        "SELECT p FROM Payment p WHERE p.status = :status ORDER BY p.createdAt DESC",
                        Payment.class
                )
                .setParameter("status", status)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Payment> findAll() {
        return entityManager.createQuery(
                "SELECT p FROM Payment p ORDER BY p.createdAt DESC",
                Payment.class
        ).getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteById(int id) {
        Payment payment = entityManager.find(Payment.class, id);
        if (payment != null) {
            entityManager.remove(payment);
        }
    }
}
