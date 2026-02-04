package com.utsav.arts.storage;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    /**
     * Uploads a file and returns the public URL.
     */
    String upload(MultipartFile file);

    /**
     * Deletes a file given its URL (or filename).
     */
    void delete(String fileUrl);
}