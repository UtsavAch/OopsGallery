package com.utsav.arts.services;

import com.utsav.arts.models.Artwork;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ArtworkService {

    Artwork save(Artwork artwork, MultipartFile imageFile);

    Artwork update(int id, Artwork updatedArtwork, MultipartFile imageFile);

    Optional<Artwork> findById(int id);

    List<Artwork> findAll();

    List<Artwork> findByCategory(String category);

    void deleteById(int id);
}
