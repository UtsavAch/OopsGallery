package com.utsav.arts.controllers;

import com.utsav.arts.dtos.artworkDTO.ArtworkRequestDTO;
import com.utsav.arts.dtos.artworkDTO.ArtworkResponseDTO;
import com.utsav.arts.mappers.ArtworkMapper;
import com.utsav.arts.models.Artwork;
import com.utsav.arts.models.User;
import com.utsav.arts.services.ArtworkService;
import com.utsav.arts.services.UserService;
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
    private final UserService userService;

    public ArtworkController(ArtworkService artworkService,
                             UserService userService) {
        this.artworkService = artworkService;
        this.userService = userService;
    }

    // ---------------- CREATE ----------------
    @PostMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ArtworkResponseDTO> save(
            @RequestBody ArtworkRequestDTO requestDTO
    ) {
        User owner = userService.findById(requestDTO.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));

        Artwork artwork = ArtworkMapper.toEntity(requestDTO, owner);
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
            @RequestBody ArtworkRequestDTO requestDTO
    ) {
        User owner = userService.findById(requestDTO.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));

        Artwork artwork = ArtworkMapper.toEntity(requestDTO, owner);
        Artwork updatedArtwork = artworkService.update(id, artwork);

        return ResponseEntity.ok(
                ArtworkMapper.toResponseDTO(updatedArtwork)
        );
    }

    // ---------------- READ ----------------
    @GetMapping("/{id}")
    public ResponseEntity<ArtworkResponseDTO> findById(@PathVariable int id) {
        return artworkService.findById(id)
                .map(a -> ResponseEntity.ok(ArtworkMapper.toResponseDTO(a)))
                .orElse(ResponseEntity.notFound().build());
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

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<ArtworkResponseDTO>> findByOwnerId(
            @PathVariable int ownerId
    ) {
        List<ArtworkResponseDTO> artworks = artworkService.findByOwnerId(ownerId)
                .stream()
                .map(ArtworkMapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(artworks);
    }

    // ---------------- DELETE ----------------
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Void> deleteById(@PathVariable int id) {
        artworkService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
