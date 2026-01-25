package com.qualique.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandDTO {
    
    private Long id;
    
    @NotBlank(message = "Brand name is required")
    private String name;
    
    private String description;
    
    private String logoUrl;
    
    private String websiteUrl;
    
    private Integer displayOrder;
    
    private Boolean active = true;
    
    private Long productCount;
}
