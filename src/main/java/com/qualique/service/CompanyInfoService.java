package com.qualique.service;

import com.qualique.dto.CompanyInfoDTO;
import com.qualique.entity.CompanyInfo;
import com.qualique.repository.CompanyInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyInfoService {
    
    private final CompanyInfoRepository companyInfoRepository;
    
    public CompanyInfoDTO getCompanyInfo() {
        return companyInfoRepository.getCompanyInfo()
                .map(this::toDTO)
                .orElse(getDefaultCompanyInfo());
    }
    
    @Transactional
    public CompanyInfoDTO saveCompanyInfo(CompanyInfoDTO dto) {
        CompanyInfo companyInfo = companyInfoRepository.getCompanyInfo()
                .orElse(new CompanyInfo());
        
        updateEntity(companyInfo, dto);
        return toDTO(companyInfoRepository.save(companyInfo));
    }
    
    private CompanyInfoDTO getDefaultCompanyInfo() {
        return CompanyInfoDTO.builder()
                .companyName("Your Company Name")
                .aboutUs("Welcome to our company!")
                .email("info@example.com")
                .phone("+91 1234567890")
                .build();
    }
    
    private void updateEntity(CompanyInfo entity, CompanyInfoDTO dto) {
        entity.setCompanyName(dto.getCompanyName());
        entity.setAboutUs(dto.getAboutUs());
        entity.setTagline(dto.getTagline());
        entity.setMission(dto.getMission());
        entity.setVision(dto.getVision());
        entity.setLogoUrl(dto.getLogoUrl());
        entity.setFaviconUrl(dto.getFaviconUrl());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setAlternatePhone(dto.getAlternatePhone());
        entity.setAddress(dto.getAddress());
        entity.setCity(dto.getCity());
        entity.setState(dto.getState());
        entity.setCountry(dto.getCountry());
        entity.setPincode(dto.getPincode());
        entity.setFacebookUrl(dto.getFacebookUrl());
        entity.setInstagramUrl(dto.getInstagramUrl());
        entity.setLinkedinUrl(dto.getLinkedinUrl());
        entity.setTwitterUrl(dto.getTwitterUrl());
        entity.setYoutubeUrl(dto.getYoutubeUrl());
        entity.setPinterestUrl(dto.getPinterestUrl());
        entity.setBusinessHours(dto.getBusinessHours());
        entity.setGoogleMapsEmbed(dto.getGoogleMapsEmbed());
        entity.setFooterText(dto.getFooterText());
        entity.setCopyrightText(dto.getCopyrightText());
    }
    
    private CompanyInfoDTO toDTO(CompanyInfo entity) {
        return CompanyInfoDTO.builder()
                .id(entity.getId())
                .companyName(entity.getCompanyName())
                .aboutUs(entity.getAboutUs())
                .tagline(entity.getTagline())
                .mission(entity.getMission())
                .vision(entity.getVision())
                .logoUrl(entity.getLogoUrl())
                .faviconUrl(entity.getFaviconUrl())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .alternatePhone(entity.getAlternatePhone())
                .address(entity.getAddress())
                .city(entity.getCity())
                .state(entity.getState())
                .country(entity.getCountry())
                .pincode(entity.getPincode())
                .facebookUrl(entity.getFacebookUrl())
                .instagramUrl(entity.getInstagramUrl())
                .linkedinUrl(entity.getLinkedinUrl())
                .twitterUrl(entity.getTwitterUrl())
                .youtubeUrl(entity.getYoutubeUrl())
                .pinterestUrl(entity.getPinterestUrl())
                .businessHours(entity.getBusinessHours())
                .googleMapsEmbed(entity.getGoogleMapsEmbed())
                .footerText(entity.getFooterText())
                .copyrightText(entity.getCopyrightText())
                .build();
    }
}
