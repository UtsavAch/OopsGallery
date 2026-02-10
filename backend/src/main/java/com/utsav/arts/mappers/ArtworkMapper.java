package com.utsav.arts.mappers;

import com.utsav.arts.dtos.artworkDTO.ArtworkRequestDTO;
import com.utsav.arts.dtos.artworkDTO.ArtworkResponseDTO;
import com.utsav.arts.models.Artwork;

/**
 * Mapper class for converting between Artwork entity and DTOs.
 *
 * <p>Provides methods to map:
 * <ul>
 *   <li>ArtworkRequestDTO → Artwork entity</li>
 *   <li>Artwork entity → ArtworkResponseDTO</li>
 * </ul>
 */
public class ArtworkMapper {

    /**
     * Converts an ArtworkRequestDTO to an Artwork entity.
     * @param dto ArtworkRequestDTO containing input data
     * @return Artwork entity with fields populated from dto
     */
    public static Artwork toEntity(ArtworkRequestDTO dto) {
        Artwork artwork = new Artwork();
        artwork.setTitle(dto.getTitle());
        artwork.setDescription(dto.getDescription());
        artwork.setCategory(dto.getCategory());
        artwork.setLabel(dto.getLabel());
        artwork.setPrice(dto.getPrice());
        return artwork;
    }

    /**
     * Converts an Artwork entity to an ArtworkResponseDTO.
     * @param artwork Artwork entity to convert
     * @return ArtworkResponseDTO with fields populated from entity
     */
    public static ArtworkResponseDTO toResponseDTO(Artwork artwork) {
        ArtworkResponseDTO dto = new ArtworkResponseDTO();
        dto.setId(artwork.getId());
        dto.setTitle(artwork.getTitle());
        dto.setDescription(artwork.getDescription());
        dto.setCategory(artwork.getCategory());
        dto.setLabel(artwork.getLabel());
        dto.setPrice(artwork.getPrice());
        dto.setImgUrl(artwork.getImgUrl());
        return dto;
    }
}