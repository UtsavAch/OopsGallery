package com.utsav.arts.controllers;

import com.utsav.arts.dtos.artworkDTO.ArtworkRequestDTO;
import com.utsav.arts.dtos.artworkDTO.ArtworkResponseDTO;
import com.utsav.arts.exceptions.ResourceNotFoundException;
import com.utsav.arts.mappers.ArtworkMapper;
import com.utsav.arts.models.Artwork;
import com.utsav.arts.services.ArtworkService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for managing artworks.
 *
 * <p>Provides CRUD operations for artworks, including creation, update, retrieval, and deletion.
 * Certain operations are restricted to users with the OWNER role.
 *
 * <p>Endpoints:
 * <ul>
 *     <li>POST /api/artworks → Create a new artwork (OWNER only)</li>
 *     <li>PUT /api/artworks/{id} → Update an artwork (OWNER only)</li>
 *     <li>GET /api/artworks/{id} → Get artwork by ID</li>
 *     <li>GET /api/artworks → Get all artworks</li>
 *     <li>GET /api/artworks/category/{category} → Get artworks by category</li>
 *     <li>DELETE /api/artworks/{id} → Delete artwork by ID (OWNER only)</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/artworks")
public class ArtworkController {

    private final ArtworkService artworkService;

    public ArtworkController(ArtworkService artworkService) {
        this.artworkService = artworkService;
    }

    // ---------------- CREATE ----------------
    /**
     * Creates a new artwork with optional image upload.
     *
     * @param requestDTO Artwork data
     * @param image Image file
     * @return Created artwork as ArtworkResponseDTO
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ArtworkResponseDTO> save(
            @RequestPart("data") @Valid ArtworkRequestDTO requestDTO, // JSON part
            @RequestPart("image") MultipartFile image                 // File part
    ) {
        Artwork artwork = ArtworkMapper.toEntity(requestDTO);
        // Pass image to service
        Artwork savedArtwork = artworkService.save(artwork, image);

        return new ResponseEntity<>(
                ArtworkMapper.toResponseDTO(savedArtwork),
                HttpStatus.CREATED
        );
    }

    // ---------------- UPDATE ----------------
    /**
     * Updates an existing artwork by ID. Image upload is optional.
     *
     * @param id Artwork ID
     * @param requestDTO Updated artwork data
     * @param image Optional image file
     * @return Updated artwork as ArtworkResponseDTO
     */
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ArtworkResponseDTO> update(
            @PathVariable int id,
            @RequestPart("data") @Valid ArtworkRequestDTO requestDTO,
            @RequestPart(value = "image", required = false) MultipartFile image // Image is optional on update
    ) {
        Artwork artwork = ArtworkMapper.toEntity(requestDTO);
        Artwork updatedArtwork = artworkService.update(id, artwork, image);

        return ResponseEntity.ok(
                ArtworkMapper.toResponseDTO(updatedArtwork)
        );
    }

    // ---------------- READ ----------------
    /**
     * Retrieves artwork by its ID.
     *
     * @param id Artwork ID
     * @return ArtworkResponseDTO if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<ArtworkResponseDTO> findById(@PathVariable int id) {
        Artwork artwork = artworkService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artwork not found with id: " + id));

        return ResponseEntity.ok(ArtworkMapper.toResponseDTO(artwork));
    }

    /**
     * Retrieves all artworks.
     *
     * @return List of ArtworkResponseDTO
     */
    @GetMapping
    public ResponseEntity<List<ArtworkResponseDTO>> findAll() {
        List<ArtworkResponseDTO> artworks = artworkService.findAll()
                .stream()
                .map(ArtworkMapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(artworks);
    }

    /**
     * Retrieves artworks by category.
     *
     * @param category Category name
     * @return List of ArtworkResponseDTO filtered by category
     */
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
    /**
     * Deletes artwork by its ID.
     *
     * @param id Artwork ID
     * @return HTTP 204 No Content on success
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Void> deleteById(@PathVariable int id) {
        // The service already throws ResourceNotFoundException if ID doesn't exist
        artworkService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}