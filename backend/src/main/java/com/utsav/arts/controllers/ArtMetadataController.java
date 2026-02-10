package com.utsav.arts.controllers;

import com.utsav.arts.models.ArtCategory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * Controller that provides metadata endpoints related to artworks.
 *
 * <p>Currently, it provides a list of all available art categories.
 *
 * <p>Endpoints:
 * <ul>
 *     <li>GET /api/meta/art-categories → Returns list of all ArtCategory enum names</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/meta")
public class ArtMetadataController {

    /**
     * Retrieves all available art categories.
     *
     * @return List of art category names as strings
     */
    @GetMapping("/art-categories")
    public List<String> getArtCategories() {
        return Arrays.stream(ArtCategory.values())
                .map(Enum::name)
                .toList();
    }
}
