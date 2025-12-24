package com.utsav.arts.services;

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
        if (artwork.getOwner() == null) {
            throw new IllegalArgumentException("Artwork must have an owner");
        }

        if (artwork.getTitle() == null || artwork.getTitle().isBlank()) {
            throw new IllegalArgumentException("Artwork title cannot be empty");
        }

        // Default value
        artwork.setLikes(0);

        return artworkRepository.save(artwork);
    }

    @Override
    public Artwork update(int id, Artwork updatedArtwork) {
        Artwork existingArtwork = artworkRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Artwork not found"));

        existingArtwork.setTitle(updatedArtwork.getTitle());
        existingArtwork.setDescription(updatedArtwork.getDescription());
        existingArtwork.setCategory(updatedArtwork.getCategory());
        existingArtwork.setLabel(updatedArtwork.getLabel());
        existingArtwork.setPrice(updatedArtwork.getPrice());
        existingArtwork.setImgUrl(updatedArtwork.getImgUrl());

        // Note: Owner should generally NOT be changed

        return artworkRepository.update(existingArtwork);
    }

    @Override
    public Optional<Artwork> findById(int id) {
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
    public List<Artwork> findByOwnerId(int ownerId) {
        return artworkRepository.findByOwnerId(ownerId);
    }

    @Override
    public void deleteById(int id) {
        if (artworkRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Artwork not found");
        }
        artworkRepository.deleteById(id);
    }
}
