package com.utsav.arts.services;

import com.utsav.arts.exceptions.InvalidRequestException;
import com.utsav.arts.exceptions.ResourceNotFoundException;
import com.utsav.arts.models.Artwork;
import com.utsav.arts.repository.ArtworkRepository;
import com.utsav.arts.storage.FileStorageService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link ArtworkService}.
 * Handles business logic for creating, updating, retrieving, and deleting artworks,
 * including uploading and deleting artwork images from storage.
 */
@Service
@Transactional
public class ArtworkServiceImpl implements ArtworkService {

    private final ArtworkRepository artworkRepository;
    private final FileStorageService fileStorageService;

    /**
     * Constructs the ArtworkServiceImpl with required dependencies.
     *
     * @param artworkRepository  Repository for CRUD operations on Artwork
     * @param fileStorageService Service for handling image file storage
     */
    public ArtworkServiceImpl(ArtworkRepository artworkRepository, FileStorageService fileStorageService) {
        this.artworkRepository = artworkRepository;
        this.fileStorageService = fileStorageService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Artwork save(Artwork artwork, MultipartFile imageFile) {
        if (artwork.getTitle() == null || artwork.getTitle().isBlank()) {
            throw new InvalidRequestException("Artwork title cannot be empty");
        }
        // Upload Image
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = fileStorageService.upload(imageFile);
            artwork.setImgUrl(imageUrl);
        } else {
            throw new InvalidRequestException("Image file is required");
        }
        // Save Entity
        return artworkRepository.save(artwork);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Artwork update(int id, Artwork updatedArtwork, MultipartFile imageFile) {
        Artwork existingArtwork = artworkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artwork not found with id: " + id));

        // If a new image is provided, replace the old one
        if (imageFile != null && !imageFile.isEmpty()) {
            // Delete old image
            fileStorageService.delete(existingArtwork.getImgUrl());
            // Upload new image
            String newImageUrl = fileStorageService.upload(imageFile);
            existingArtwork.setImgUrl(newImageUrl);
        }
        // If imageFile is null, we keep the existing URL
        // Update other fields
        existingArtwork.setTitle(updatedArtwork.getTitle());
        existingArtwork.setDescription(updatedArtwork.getDescription());
        existingArtwork.setCategory(updatedArtwork.getCategory());
        existingArtwork.setLabel(updatedArtwork.getLabel());
        existingArtwork.setPrice(updatedArtwork.getPrice());

        return artworkRepository.update(existingArtwork);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Artwork> findById(int id) {
        // We return Optional here so the Controller can choose to throw the exception
        return artworkRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Artwork> findAll() {
        return artworkRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Artwork> findByCategory(String category) {
        return artworkRepository.findByCategory(category);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteById(int id) {
        Artwork artwork = artworkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot delete: Artwork not found with id: " + id));
        // Delete image from storage
        fileStorageService.delete(artwork.getImgUrl());
        // Delete from DB
        artworkRepository.deleteById(id);
    }
}