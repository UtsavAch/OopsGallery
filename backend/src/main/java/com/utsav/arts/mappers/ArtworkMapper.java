package com.utsav.arts.mappers;

import com.utsav.arts.dtos.artworkDTO.ArtworkRequestDTO;
import com.utsav.arts.dtos.artworkDTO.ArtworkResponseDTO;
import com.utsav.arts.models.Artwork;

public class ArtworkMapper {

    public static Artwork toEntity(ArtworkRequestDTO dto) {
        Artwork artwork = new Artwork();
        artwork.setTitle(dto.getTitle());
        artwork.setDescription(dto.getDescription());
        artwork.setCategory(dto.getCategory());
        artwork.setLabel(dto.getLabel());
        artwork.setPrice(dto.getPrice());
        artwork.setImgUrl(dto.getImgUrl());
        return artwork;
    }

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