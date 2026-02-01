package com.utsav.arts.controllers;

import com.utsav.arts.models.ArtCategory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/meta")
public class ArtMetadataController {

    @GetMapping("/art-categories")
    public List<String> getArtCategories() {
        return Arrays.stream(ArtCategory.values())
                .map(Enum::name)
                .toList();
    }
}
