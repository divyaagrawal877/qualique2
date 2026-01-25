package com.qualique.service;

import com.qualique.dto.BrandDTO;
import com.qualique.entity.Brand;
import com.qualique.repository.BrandRepository;
import com.qualique.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrandService {
    
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;
    
    public List<BrandDTO> getAllBrands() {
        return brandRepository.findAllByOrderByDisplayOrderAsc()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<BrandDTO> getActiveBrands() {
        return brandRepository.findByActiveTrueOrderByDisplayOrderAsc()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public BrandDTO getBrandById(Long id) {
        return brandRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Brand not found"));
    }
    
    @Transactional
    public BrandDTO createBrand(BrandDTO dto) {
        if (brandRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Brand with this name already exists");
        }
        Brand brand = toEntity(dto);
        return toDTO(brandRepository.save(brand));
    }
    
    @Transactional
    public BrandDTO updateBrand(Long id, BrandDTO dto) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brand not found"));
        
        brand.setName(dto.getName());
        brand.setDescription(dto.getDescription());
        brand.setLogoUrl(dto.getLogoUrl());
        brand.setWebsiteUrl(dto.getWebsiteUrl());
        brand.setDisplayOrder(dto.getDisplayOrder());
        brand.setActive(dto.getActive());
        
        return toDTO(brandRepository.save(brand));
    }
    
    @Transactional
    public void deleteBrand(Long id) {
        if (!brandRepository.existsById(id)) {
            throw new RuntimeException("Brand not found");
        }
        brandRepository.deleteById(id);
    }
    
    private BrandDTO toDTO(Brand brand) {
        return BrandDTO.builder()
                .id(brand.getId())
                .name(brand.getName())
                .description(brand.getDescription())
                .logoUrl(brand.getLogoUrl())
                .websiteUrl(brand.getWebsiteUrl())
                .displayOrder(brand.getDisplayOrder())
                .active(brand.getActive())
                .productCount((long) brand.getProducts().size())
                .build();
    }
    
    private Brand toEntity(BrandDTO dto) {
        return Brand.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .logoUrl(dto.getLogoUrl())
                .websiteUrl(dto.getWebsiteUrl())
                .displayOrder(dto.getDisplayOrder())
                .active(dto.getActive() != null ? dto.getActive() : true)
                .build();
    }
}
