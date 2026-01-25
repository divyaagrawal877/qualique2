package com.qualique.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    
    private Long id;
    
    @NotBlank(message = "Product name is required")
    private String name;
    
    private String description;
    
    private String specifications;
    
    private String imageUrl;
    
    private String modelNumber;
    
    private String partNumber;
    
    private Long categoryId;
    
    private String categoryName;
    
    private Long brandId;
    
    private String brandName;
    
    private Boolean featured = false;
    
    private Boolean active = true;
    
    private Integer displayOrder;
}
