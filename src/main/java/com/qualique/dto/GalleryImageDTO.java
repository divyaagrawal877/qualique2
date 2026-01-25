package com.qualique.dto;

import com.qualique.entity.GalleryImage.GalleryCategory;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GalleryImageDTO {
    
    private Long id;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    private String description;
    
    @NotBlank(message = "Image URL is required")
    private String imageUrl;
    
    private String thumbnailUrl;
    
    private GalleryCategory galleryCategory = GalleryCategory.GENERAL;
    
    private Integer displayOrder;
    
    private Boolean active = true;
}
