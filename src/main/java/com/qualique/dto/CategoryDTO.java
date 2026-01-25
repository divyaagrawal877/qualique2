package com.qualique.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {
    
    private Long id;
    
    @NotBlank(message = "Category name is required")
    private String name;
    
    private String description;
    
    private String imageUrl;
    
    private String iconClass;
    
    private Integer displayOrder;
    
    private Boolean active = true;
    
    private Long productCount;
}
