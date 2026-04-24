package com.qualique.controller;

import com.qualique.dto.ApiResponse;
import com.qualique.entity.StoredFile;
import com.qualique.repository.StoredFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
public class FileUploadController {

    private final StoredFileRepository storedFileRepository;

    @PostMapping("/api/admin/upload")
    public ResponseEntity<ApiResponse<String>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String newFilename = UUID.randomUUID().toString() + extension;

            // Store file in database
            StoredFile storedFile = StoredFile.builder()
                    .filename(newFilename)
                    .contentType(file.getContentType())
                    .fileSize(file.getSize())
                    .data(file.getBytes())
                    .build();
            storedFileRepository.save(storedFile);

            // Return the URL to access the file
            String fileUrl = "/uploads/" + newFilename;

            return ResponseEntity.ok(ApiResponse.success("File uploaded successfully", fileUrl));

        } catch (IOException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to upload file: " + e.getMessage()));
        }
    }

    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<byte[]> serveFile(@PathVariable String filename) {
        return storedFileRepository.findByFilename(filename)
                .map(file -> ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(file.getContentType()))
                        .cacheControl(CacheControl.maxAge(30, TimeUnit.DAYS).cachePublic())
                        .body(file.getData()))
                .orElse(ResponseEntity.notFound().build());
    }
}
