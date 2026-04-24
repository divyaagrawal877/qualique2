package com.qualique.controller;

import com.qualique.dto.ApiResponse;
import com.qualique.entity.StoredFile;
import com.qualique.repository.StoredFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class FileUploadController {

    private final StoredFileRepository storedFileRepository;

    @PostMapping("/api/admin/upload")
    public ResponseEntity<ApiResponse<String>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            log.info("Uploading file: {}, size: {}, type: {}", file.getOriginalFilename(), file.getSize(), file.getContentType());

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

            log.info("File saved to database with filename: {}", newFilename);

            // Return the URL to access the file
            String fileUrl = "/uploads/" + newFilename;

            return ResponseEntity.ok(ApiResponse.success("File uploaded successfully", fileUrl));

        } catch (Exception e) {
            log.error("Failed to upload file: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to upload file: " + e.getMessage()));
        }
    }

    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<byte[]> serveFile(@PathVariable String filename) {
        try {
            log.info("Serving file: {}", filename);
            return storedFileRepository.findByFilename(filename)
                    .map(file -> {
                        log.info("File found: {}, size: {}", file.getFilename(), file.getFileSize());
                        return ResponseEntity.ok()
                                .contentType(MediaType.parseMediaType(file.getContentType()))
                                .cacheControl(CacheControl.maxAge(30, TimeUnit.DAYS).cachePublic())
                                .body(file.getData());
                    })
                    .orElseGet(() -> {
                        log.warn("File not found: {}", filename);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            log.error("Error serving file {}: {}", filename, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
