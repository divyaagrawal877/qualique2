package com.qualique.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/public")
public class ImageProxyController {
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    /**
     * Proxy endpoint to fetch Google Drive images server-side
     * This bypasses browser CORS/tracking restrictions
     */
    @GetMapping("/gdrive-image")
    public ResponseEntity<byte[]> getGoogleDriveImage(@RequestParam String id) {
        try {
            // Construct Google Drive direct download URL
            String googleDriveUrl = "https://drive.google.com/uc?export=download&id=" + id;
            
            // Fetch the image
            ResponseEntity<byte[]> response = restTemplate.exchange(
                googleDriveUrl,
                HttpMethod.GET,
                createRequestEntity(),
                byte[].class
            );
            
            // Get content type from response or default to jpeg
            MediaType contentType = response.getHeaders().getContentType();
            if (contentType == null) {
                contentType = MediaType.IMAGE_JPEG;
            }
            
            // Return the image with proper headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(contentType);
            headers.setCacheControl(CacheControl.maxAge(java.time.Duration.ofDays(7)));
            
            return new ResponseEntity<>(response.getBody(), headers, HttpStatus.OK);
            
        } catch (Exception e) {
            // Return a placeholder image or 404
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Convert Google Drive share link to proxy URL
     */
    @GetMapping("/gdrive-convert")
    public ResponseEntity<String> convertGoogleDriveLink(@RequestParam String url) {
        String fileId = extractFileId(url);
        if (fileId != null) {
            return ResponseEntity.ok("/api/public/gdrive-image?id=" + fileId);
        }
        return ResponseEntity.badRequest().body("Invalid Google Drive URL");
    }
    
    private String extractFileId(String url) {
        // Pattern: https://drive.google.com/file/d/FILE_ID/view
        Pattern pattern1 = Pattern.compile("drive\\.google\\.com/file/d/([a-zA-Z0-9_-]+)");
        Matcher matcher1 = pattern1.matcher(url);
        if (matcher1.find()) {
            return matcher1.group(1);
        }
        
        // Pattern: https://drive.google.com/open?id=FILE_ID
        Pattern pattern2 = Pattern.compile("drive\\.google\\.com/open\\?id=([a-zA-Z0-9_-]+)");
        Matcher matcher2 = pattern2.matcher(url);
        if (matcher2.find()) {
            return matcher2.group(1);
        }
        
        return null;
    }
    
    private HttpEntity<String> createRequestEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
        return new HttpEntity<>(headers);
    }
}
