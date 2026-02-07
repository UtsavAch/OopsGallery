package com.utsav.arts.repository;

import com.utsav.arts.models.Cart;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public class CartRepositoryImpl implements CartRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Cart save(Cart cart) {
        if (cart.getId() == 0) {
            entityManager.persist(cart); // new cart
            return cart;
        } else {
            return entityManager.merge(cart); // existing cart
        }
    }

    @Override
    public Optional<Cart> findById(int id) {
        try {
            Cart cart = entityManager.createQuery(
                            """
                            SELECT DISTINCT c
                            FROM Cart c
                            LEFT JOIN FETCH c.items i
                            LEFT JOIN FETCH i.artwork
                            WHERE c.id = :id
                            """,
                            Cart.class
                    )
                    .setParameter("id", id)
                    .getSingleResult();

            return Optional.of(cart);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

@Override
public Optional<Cart> findByUserId(int userId) {
    try {
        Cart cart = entityManager.createQuery(
                        """
                        SELECT DISTINCT c
                        FROM Cart c
                        LEFT JOIN FETCH c.items i
                        LEFT JOIN FETCH i.artwork
                        WHERE c.user.id = :userId
                        """,
                        Cart.class
                )
                .setParameter("userId", userId)
                .getSingleResult();

        return Optional.of(cart);
    } catch (NoResultException e) {
        return Optional.empty();
    }
}

    @Override
    public void deleteById(int id) {
        Cart cart = entityManager.find(Cart.class, id);
        if (cart != null) {
            entityManager.remove(cart);
        }
    }
}
