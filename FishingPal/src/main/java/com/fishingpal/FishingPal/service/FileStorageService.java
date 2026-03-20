package com.fishingpal.FishingPal.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final Path UPLOAD_DIR = Paths.get("uploads", "community");

    public FileStorageService() throws IOException {
        Files.createDirectories(UPLOAD_DIR);
    }

    public String store(MultipartFile file) throws IOException {
        String originalName = file.getOriginalFilename();
        String ext = "";
        if (originalName != null && originalName.contains(".")) {
            ext = originalName.substring(originalName.lastIndexOf('.'));
        }
        String filename = UUID.randomUUID() + ext;
        Files.copy(file.getInputStream(), UPLOAD_DIR.resolve(filename));
        return filename;
    }

    public void delete(String filename) throws IOException {
        Files.deleteIfExists(UPLOAD_DIR.resolve(filename));
    }
}
