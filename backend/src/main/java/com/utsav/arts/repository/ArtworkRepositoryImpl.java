package com.utsav.arts.repository;

import com.utsav.arts.models.Artwork;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link ArtworkRepository} using JPA {@link EntityManager}.
 * <p>
 * Provides concrete CRUD operations for {@link Artwork} entities in the database.
 */
@Repository
@Transactional
public class ArtworkRepositoryImpl implements ArtworkRepository {

    /**
     * Injected EntityManager to perform JPA operations.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public Artwork save(Artwork artwork) {
        entityManager.persist(artwork);
        return artwork;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Artwork update(Artwork artwork) {
        return entityManager.merge(artwork);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Artwork> findById(int id) {
        return Optional.ofNullable(entityManager.find(Artwork.class, id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Artwork> findAll() {
        return entityManager.createQuery(
                "SELECT a FROM Artwork a",
                Artwork.class
        ).getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Artwork> findByCategory(String category) {
        return entityManager.createQuery(
                        "SELECT a FROM Artwork a WHERE a.category = :category",
                        Artwork.class
                )
                .setParameter("category", category)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteById(int id) {
        Artwork artwork = entityManager.find(Artwork.class, id);
        if (artwork != null) {
            entityManager.remove(artwork);
        }
    }
}
