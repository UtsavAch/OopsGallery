package com.utsav.arts.repository;

import com.utsav.arts.models.Artwork;

import java.util.List;
import java.util.Optional;

public interface ArtworkRepository {

    Artwork save(Artwork artwork);

    Artwork update(Artwork artwork);

    Optional<Artwork> findById(int id);

    List<Artwork> findAll();

    List<Artwork> findByCategory(String category);

    void deleteById(int id);
}
