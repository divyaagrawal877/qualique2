package com.qualique.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "gallery_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GalleryImage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    private String description;
    
    @Column(nullable = false)
    private String imageUrl;
    
    private String thumbnailUrl;
    
    @Enumerated(EnumType.STRING)
    private GalleryCategory galleryCategory = GalleryCategory.GENERAL;
    
    private Integer displayOrder;
    
    private Boolean active = true;
    
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum GalleryCategory {
        GENERAL, PRODUCTS, OFFICE, TEAM, EVENTS, CERTIFICATES
    }
}
