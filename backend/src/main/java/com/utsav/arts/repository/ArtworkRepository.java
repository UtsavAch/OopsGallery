package com.utsav.arts.repository;

import com.utsav.arts.models.Artwork;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on {@link Artwork} entities.
 * <p>
 * Defines methods for saving, updating, retrieving, and deleting artworks.
 */
public interface ArtworkRepository {

    /**
     * Saves a new artwork entity in the database.
     *
     * @param artwork Artwork entity to save
     * @return The saved Artwork entity with generated ID
     */
    Artwork save(Artwork artwork);

    /**
     * Updates an existing artwork entity in the database.
     *
     * @param artwork Artwork entity with updated fields
     * @return The updated Artwork entity
     */
    Artwork update(Artwork artwork);

    /**
     * Finds an artwork by its ID.
     *
     * @param id The ID of the artwork to retrieve
     * @return Optional containing the Artwork if found, or empty if not found
     */
    Optional<Artwork> findById(int id);

    /**
     * Retrieves all artworks from the database.
     *
     * @return List of all Artwork entities
     */
    List<Artwork> findAll();

    /**
     * Finds artworks belonging to a specific category.
     *
     * @param category The category to filter artworks by
     * @return List of Artwork entities in the given category
     */
    List<Artwork> findByCategory(String category);

    /**
     * Deletes an artwork by its ID.
     *
     * @param id The ID of the artwork to delete
     */
    void deleteById(int id);
}
