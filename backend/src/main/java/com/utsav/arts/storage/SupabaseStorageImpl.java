package com.utsav.arts.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

/**
 * Implementation of {@link FileStorageService} using Supabase Storage.
 *
 * <p>Provides functionality to upload and delete files in a Supabase storage bucket.
 * Files are stored with unique filenames to avoid collisions, and uploaded files
 * can be accessed via a public URL.
 */
@Service
public class SupabaseStorageImpl implements FileStorageService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.bucket}")
    private String bucketName;

    private final RestClient restClient;

    /**
     * Constructs a {@code SupabaseStorageImpl} instance with a {@link RestClient.Builder}.
     *
     * @param builder the RestClient builder used to create a RestClient instance
     */
    public SupabaseStorageImpl(RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    /**
     * Uploads a file to Supabase storage.
     *
     * <p>The file is given a unique filename by prepending a UUID to the original filename
     * to avoid collisions. The method returns a public URL to access the uploaded file.
     *
     * @param file the {@link MultipartFile} to upload
     * @return the public URL of the uploaded file
     * @throws RuntimeException if the file cannot be read or uploaded
     */
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

    /**
     * Deletes a file from Supabase storage.
     *
     * <p>The method extracts the filename from the provided public URL and deletes
     * the corresponding object in the configured bucket.
     *
     * @param fileUrl the public URL of the file to delete
     */
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