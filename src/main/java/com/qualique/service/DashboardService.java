package com.qualique.service;

import com.qualique.dto.DashboardStats;
import com.qualique.entity.ContactInquiry.InquiryStatus;
import com.qualique.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {
    
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ContactInquiryRepository contactInquiryRepository;
    
    public DashboardStats getStats() {
        return DashboardStats.builder()
                .totalProducts(productRepository.countByActiveTrue())
                .totalCategories(categoryRepository.count())
                .totalBrands(brandRepository.count())
                .totalInquiries(contactInquiryRepository.count())
                .newInquiries(contactInquiryRepository.countByStatus(InquiryStatus.NEW))
                .pendingInquiries(contactInquiryRepository.countByStatus(InquiryStatus.IN_PROGRESS))
                .build();
    }
}
