package com.utsav.arts.repository;

import com.utsav.arts.models.CartItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class CartItemRepositoryImpl implements CartItemRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public CartItem save(CartItem cartItem) {
        entityManager.persist(cartItem);
        return cartItem;
    }

    @Override
    public CartItem update(CartItem cartItem) {
        return entityManager.merge(cartItem);
    }

    @Override
    public Optional<CartItem> findById(int id) {
        return Optional.ofNullable(entityManager.find(CartItem.class, id));
    }

    @Override
    public List<CartItem> findByCartId(int cartId) {
        return entityManager.createQuery(
                        "SELECT ci FROM CartItem ci WHERE ci.cart.id = :cartId",
                        CartItem.class
                )
                .setParameter("cartId", cartId)
                .getResultList();
    }

    @Override
    public Optional<CartItem> findByCartIdAndArtworkId(int cartId, int artworkId) {
        try {
            CartItem item = entityManager.createQuery(
                            "SELECT ci FROM CartItem ci WHERE ci.cart.id = :cartId AND ci.artwork.id = :artworkId",
                            CartItem.class
                    )
                    .setParameter("cartId", cartId)
                    .setParameter("artworkId", artworkId)
                    .getSingleResult();

            return Optional.of(item);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(int id) {
        CartItem cartItem = entityManager.find(CartItem.class, id);
        if (cartItem != null) {
            entityManager.remove(cartItem);
        }
    }

    @Override
    public void deleteByCartId(int cartId) {
        entityManager.createQuery(
                        "DELETE FROM CartItem ci WHERE ci.cart.id = :cartId"
                )
                .setParameter("cartId", cartId)
                .executeUpdate();
    }
}
