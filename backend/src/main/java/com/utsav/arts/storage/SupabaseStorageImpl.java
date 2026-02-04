package com.utsav.arts.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
public class SupabaseStorageImpl implements FileStorageService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.bucket}")
    private String bucketName;

    private final RestClient restClient;

    public SupabaseStorageImpl(RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    @Override
    public String upload(MultipartFile file) {
        try {
            // Generate a unique filename to avoid collisions
            String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();

            // Supabase Storage API Endpoint
            String url = supabaseUrl + "/storage/v1/object/" + bucketName + "/" + filename;

            restClient.post()
                    .uri(url)
                    .header("Authorization", "Bearer " + supabaseKey)
                    .header("Content-Type", Objects.requireNonNull(file.getContentType()))
                    .body(file.getBytes())
                    .retrieve()
                    .toBodilessEntity();

            // Return the public URL
            return supabaseUrl + "/storage/v1/object/public/" + bucketName + "/" + filename;

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image to Supabase", e);
        }
    }

    @Override
    public void delete(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) return;

        // Extract filename from URL
        //URL format: .../public/bucketName/filename
        String filename = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);

        String url = supabaseUrl + "/storage/v1/object/" + bucketName + "/" + filename;

        restClient.delete()
                .uri(url)
                .header("Authorization", "Bearer " + supabaseKey)
                .retrieve()
                .toBodilessEntity();
    }
}