package com.utsav.arts.services;

import com.utsav.arts.exceptions.InvalidRequestException;
import com.utsav.arts.exceptions.ResourceNotFoundException;
import com.utsav.arts.models.Artwork;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing artworks.
 * Provides methods to create, update, retrieve, and delete artworks.
 */
public interface ArtworkService {

    /**
     * Saves a new artwork along with its image.
     *
     * @param artwork   The artwork entity to save
     * @param imageFile The image file to upload and associate with the artwork
     * @return The saved Artwork entity
     * @throws InvalidRequestException if the artwork title is empty or image file is missing
     */
    Artwork save(Artwork artwork, MultipartFile imageFile);

    /**
     * Updates an existing artwork by its ID.
     * Optionally replaces the image if a new image file is provided.
     *
     * @param id             The ID of the artwork to update
     * @param updatedArtwork The updated artwork data
     * @param imageFile      Optional new image file
     * @return The updated Artwork entity
     * @throws ResourceNotFoundException if the artwork with the given ID does not exist
     */
    Artwork update(int id, Artwork updatedArtwork, MultipartFile imageFile);

    /**
     * Finds an artwork by its ID.
     *
     * @param id The ID of the artwork
     * @return An Optional containing the artwork if found, or empty if not
     */
    Optional<Artwork> findById(int id);

    /**
     * Retrieves all artworks in the system.
     *
     * @return A list of all artworks
     */
    List<Artwork> findAll();

    /**
     * Retrieves artworks filtered by category.
     *
     * @param category The category name to filter by
     * @return A list of artworks matching the category
     */
    List<Artwork> findByCategory(String category);

    /**
     * Deletes an artwork by its ID.
     * Also deletes the associated image from storage.
     *
     * @param id The ID of the artwork to delete
     * @throws ResourceNotFoundException if the artwork with the given ID does not exist
     */
    void deleteById(int id);
}
