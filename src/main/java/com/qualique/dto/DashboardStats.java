package com.qualique.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStats {
    
    private long totalProducts;
    private long totalCategories;
    private long totalBrands;
    private long totalInquiries;
    private long newInquiries;
    private long pendingInquiries;
}
