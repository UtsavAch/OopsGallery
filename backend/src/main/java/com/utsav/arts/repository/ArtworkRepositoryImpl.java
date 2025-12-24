package com.utsav.arts.repository;

import com.utsav.arts.models.Artwork;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class ArtworkRepositoryImpl implements ArtworkRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Artwork save(Artwork artwork) {
        entityManager.persist(artwork);
        return artwork;
    }

    @Override
    public Artwork update(Artwork artwork) {
        return entityManager.merge(artwork);
    }

    @Override
    public Optional<Artwork> findById(int id) {
        return Optional.ofNullable(entityManager.find(Artwork.class, id));
    }

    @Override
    public List<Artwork> findAll() {
        return entityManager.createQuery(
                "SELECT a FROM Artwork a",
                Artwork.class
        ).getResultList();
    }

    @Override
    public List<Artwork> findByCategory(String category) {
        return entityManager.createQuery(
                        "SELECT a FROM Artwork a WHERE a.category = :category",
                        Artwork.class
                )
                .setParameter("category", category)
                .getResultList();
    }

    @Override
    public List<Artwork> findByOwnerId(int ownerId) {
        return entityManager.createQuery(
                        "SELECT a FROM Artwork a WHERE a.owner.id = :ownerId",
                        Artwork.class
                )
                .setParameter("ownerId", ownerId)
                .getResultList();
    }

    @Override
    public void deleteById(int id) {
        Artwork artwork = entityManager.find(Artwork.class, id);
        if (artwork != null) {
            entityManager.remove(artwork);
        }
    }
}
