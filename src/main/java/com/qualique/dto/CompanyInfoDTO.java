package com.qualique.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyInfoDTO {
    
    private Long id;
    private String companyName;
    private String aboutUs;
    private String tagline;
    private String mission;
    private String vision;
    private String logoUrl;
    private String faviconUrl;
    private String email;
    private String phone;
    private String alternatePhone;
    private String address;
    private String city;
    private String state;
    private String country;
    private String pincode;
    private String facebookUrl;
    private String instagramUrl;
    private String linkedinUrl;
    private String twitterUrl;
    private String youtubeUrl;
    private String pinterestUrl;
    private String businessHours;
    private String googleMapsEmbed;
    private String footerText;
    private String copyrightText;
}
