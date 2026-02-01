package com.utsav.arts.controllers;

import com.utsav.arts.dtos.artworkDTO.ArtworkRequestDTO;
import com.utsav.arts.dtos.artworkDTO.ArtworkResponseDTO;
import com.utsav.arts.exceptions.ResourceNotFoundException;
import com.utsav.arts.mappers.ArtworkMapper;
import com.utsav.arts.models.Artwork;
import com.utsav.arts.services.ArtworkService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/artworks")
public class ArtworkController {

    private final ArtworkService artworkService;

    public ArtworkController(ArtworkService artworkService) {
        this.artworkService = artworkService;
    }

    // ---------------- CREATE ----------------
    @PostMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ArtworkResponseDTO> save(
            @Valid @RequestBody ArtworkRequestDTO requestDTO
    ) {
        Artwork artwork = ArtworkMapper.toEntity(requestDTO);
        Artwork savedArtwork = artworkService.save(artwork);

        return new ResponseEntity<>(
                ArtworkMapper.toResponseDTO(savedArtwork),
                HttpStatus.CREATED
        );
    }

    // ---------------- UPDATE ----------------
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ArtworkResponseDTO> update(
            @PathVariable int id,
            @Valid @RequestBody ArtworkRequestDTO requestDTO
    ) {
        Artwork artwork = ArtworkMapper.toEntity(requestDTO);
        Artwork updatedArtwork = artworkService.update(id, artwork);

        return ResponseEntity.ok(
                ArtworkMapper.toResponseDTO(updatedArtwork)
        );
    }

    // ---------------- READ ----------------
    @GetMapping("/{id}")
    public ResponseEntity<ArtworkResponseDTO> findById(@PathVariable int id) {
        Artwork artwork = artworkService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artwork not found with id: " + id));

        return ResponseEntity.ok(ArtworkMapper.toResponseDTO(artwork));
    }

    @GetMapping
    public ResponseEntity<List<ArtworkResponseDTO>> findAll() {
        List<ArtworkResponseDTO> artworks = artworkService.findAll()
                .stream()
                .map(ArtworkMapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(artworks);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ArtworkResponseDTO>> findByCategory(
            @PathVariable String category
    ) {
        List<ArtworkResponseDTO> artworks = artworkService.findByCategory(category)
                .stream()
                .map(ArtworkMapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(artworks);
    }

    // ---------------- DELETE ----------------
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Void> deleteById(@PathVariable int id) {
        // The service already throws ResourceNotFoundException if ID doesn't exist
        artworkService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}