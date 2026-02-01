package com.utsav.arts.services;

import com.utsav.arts.exceptions.InvalidRequestException;
import com.utsav.arts.exceptions.ResourceNotFoundException;
import com.utsav.arts.models.Artwork;
import com.utsav.arts.repository.ArtworkRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ArtworkServiceImpl implements ArtworkService {

    private final ArtworkRepository artworkRepository;

    public ArtworkServiceImpl(ArtworkRepository artworkRepository) {
        this.artworkRepository = artworkRepository;
    }

    @Override
    public Artwork save(Artwork artwork) {
        // Use InvalidRequestException for validation errors
        // This will trigger a 400 Bad Request via your GlobalExceptionHandler
        if (artwork.getTitle() == null || artwork.getTitle().isBlank()) {
            throw new InvalidRequestException("Artwork title cannot be empty");
        }

        return artworkRepository.save(artwork);
    }

    @Override
    public Artwork update(int id, Artwork updatedArtwork) {
        // Use ResourceNotFoundException for a 404 response
        Artwork existingArtwork = artworkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artwork not found with id: " + id));

        existingArtwork.setTitle(updatedArtwork.getTitle());
        existingArtwork.setDescription(updatedArtwork.getDescription());
        existingArtwork.setCategory(updatedArtwork.getCategory());
        existingArtwork.setLabel(updatedArtwork.getLabel());
        existingArtwork.setPrice(updatedArtwork.getPrice());
        existingArtwork.setImgUrl(updatedArtwork.getImgUrl());

        return artworkRepository.update(existingArtwork);
    }

    @Override
    public Optional<Artwork> findById(int id) {
        // We return Optional here so the Controller can choose to throw the exception
        return artworkRepository.findById(id);
    }

    @Override
    public List<Artwork> findAll() {
        return artworkRepository.findAll();
    }

    @Override
    public List<Artwork> findByCategory(String category) {
        return artworkRepository.findByCategory(category);
    }

    @Override
    public void deleteById(int id) {
        // Ensure consistent 404 behavior for deletions
        if (artworkRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Cannot delete: Artwork not found with id: " + id);
        }
        artworkRepository.deleteById(id);
    }
}