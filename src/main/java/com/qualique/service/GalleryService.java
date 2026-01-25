package com.qualique.service;

import com.qualique.dto.GalleryImageDTO;
import com.qualique.entity.GalleryImage;
import com.qualique.entity.GalleryImage.GalleryCategory;
import com.qualique.repository.GalleryImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GalleryService {
    
    private final GalleryImageRepository galleryImageRepository;
    
    public List<GalleryImageDTO> getAllImages() {
        return galleryImageRepository.findAllByOrderByDisplayOrderAsc()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<GalleryImageDTO> getActiveImages() {
        return galleryImageRepository.findByActiveTrueOrderByDisplayOrderAsc()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<GalleryImageDTO> getImagesByCategory(GalleryCategory category) {
        return galleryImageRepository.findByGalleryCategoryAndActiveTrueOrderByDisplayOrderAsc(category)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public GalleryImageDTO getImageById(Long id) {
        return galleryImageRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Gallery image not found"));
    }
    
    @Transactional
    public GalleryImageDTO createImage(GalleryImageDTO dto) {
        GalleryImage image = toEntity(dto);
        return toDTO(galleryImageRepository.save(image));
    }
    
    @Transactional
    public GalleryImageDTO updateImage(Long id, GalleryImageDTO dto) {
        GalleryImage image = galleryImageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gallery image not found"));
        
        image.setTitle(dto.getTitle());
        image.setDescription(dto.getDescription());
        image.setImageUrl(dto.getImageUrl());
        image.setThumbnailUrl(dto.getThumbnailUrl());
        image.setGalleryCategory(dto.getGalleryCategory());
        image.setDisplayOrder(dto.getDisplayOrder());
        image.setActive(dto.getActive());
        
        return toDTO(galleryImageRepository.save(image));
    }
    
    @Transactional
    public void deleteImage(Long id) {
        if (!galleryImageRepository.existsById(id)) {
            throw new RuntimeException("Gallery image not found");
        }
        galleryImageRepository.deleteById(id);
    }
    
    private GalleryImageDTO toDTO(GalleryImage image) {
        return GalleryImageDTO.builder()
                .id(image.getId())
                .title(image.getTitle())
                .description(image.getDescription())
                .imageUrl(image.getImageUrl())
                .thumbnailUrl(image.getThumbnailUrl())
                .galleryCategory(image.getGalleryCategory())
                .displayOrder(image.getDisplayOrder())
                .active(image.getActive())
                .build();
    }
    
    private GalleryImage toEntity(GalleryImageDTO dto) {
        return GalleryImage.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .imageUrl(dto.getImageUrl())
                .thumbnailUrl(dto.getThumbnailUrl())
                .galleryCategory(dto.getGalleryCategory())
                .displayOrder(dto.getDisplayOrder())
                .active(dto.getActive() != null ? dto.getActive() : true)
                .build();
    }
}
