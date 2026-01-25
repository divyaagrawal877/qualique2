package com.qualique.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "company_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyInfo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String companyName;
    
    @Column(length = 5000)
    private String aboutUs;
    
    @Column(length = 2000)
    private String tagline;
    
    @Column(length = 2000)
    private String mission;
    
    @Column(length = 2000)
    private String vision;
    
    private String logoUrl;
    
    private String faviconUrl;
    
    // Contact Information
    private String email;
    
    private String phone;
    
    private String alternatePhone;
    
    @Column(length = 1000)
    private String address;
    
    private String city;
    
    private String state;
    
    private String country;
    
    private String pincode;
    
    // Social Media Links
    private String facebookUrl;
    
    private String instagramUrl;
    
    private String linkedinUrl;
    
    private String twitterUrl;
    
    private String youtubeUrl;
    
    private String pinterestUrl;
    
    // Business Hours
    private String businessHours;
    
    // Google Maps Embed URL
    @Column(length = 1000)
    private String googleMapsEmbed;
    
    // Footer text
    @Column(length = 500)
    private String footerText;
    
    // Copyright text
    private String copyrightText;
    
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
}
