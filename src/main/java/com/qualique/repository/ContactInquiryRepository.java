package com.qualique.repository;

import com.qualique.entity.ContactInquiry;
import com.qualique.entity.ContactInquiry.InquiryStatus;
import com.qualique.entity.ContactInquiry.InquiryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactInquiryRepository extends JpaRepository<ContactInquiry, Long> {
    
    Page<ContactInquiry> findAllByOrderByCreatedAtDesc(Pageable pageable);
    
    List<ContactInquiry> findByStatus(InquiryStatus status);
    
    List<ContactInquiry> findByInquiryType(InquiryType inquiryType);
    
    long countByStatus(InquiryStatus status);
    
    List<ContactInquiry> findTop10ByOrderByCreatedAtDesc();
}
