package com.utsav.arts.services;

import com.utsav.arts.models.Artwork;

import java.util.List;
import java.util.Optional;

public interface ArtworkService {

    Artwork save(Artwork artwork);

    Artwork update(int id, Artwork updatedArtwork);

    Optional<Artwork> findById(int id);

    List<Artwork> findAll();

    List<Artwork> findByCategory(String category);

    void deleteById(int id);
}
