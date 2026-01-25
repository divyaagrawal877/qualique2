package com.qualique.service;

import com.qualique.dto.ContactInquiryDTO;
import com.qualique.entity.ContactInquiry;
import com.qualique.entity.ContactInquiry.InquiryStatus;
import com.qualique.repository.ContactInquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactInquiryService {
    
    private final ContactInquiryRepository contactInquiryRepository;
    
    public Page<ContactInquiryDTO> getAllInquiries(Pageable pageable) {
        return contactInquiryRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(this::toDTO);
    }
    
    public List<ContactInquiryDTO> getRecentInquiries() {
        return contactInquiryRepository.findTop10ByOrderByCreatedAtDesc()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public ContactInquiryDTO getInquiryById(Long id) {
        return contactInquiryRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Inquiry not found"));
    }
    
    @Transactional
    public ContactInquiryDTO createInquiry(ContactInquiryDTO dto) {
        ContactInquiry inquiry = ContactInquiry.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .company(dto.getCompany())
                .subject(dto.getSubject())
                .message(dto.getMessage())
                .inquiryType(dto.getInquiryType())
                .status(InquiryStatus.NEW)
                .build();
        
        return toDTO(contactInquiryRepository.save(inquiry));
    }
    
    @Transactional
    public ContactInquiryDTO updateInquiryStatus(Long id, InquiryStatus status, String adminNotes) {
        ContactInquiry inquiry = contactInquiryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inquiry not found"));
        
        inquiry.setStatus(status);
        if (adminNotes != null) {
            inquiry.setAdminNotes(adminNotes);
        }
        
        return toDTO(contactInquiryRepository.save(inquiry));
    }
    
    @Transactional
    public void deleteInquiry(Long id) {
        if (!contactInquiryRepository.existsById(id)) {
            throw new RuntimeException("Inquiry not found");
        }
        contactInquiryRepository.deleteById(id);
    }
    
    public long countNewInquiries() {
        return contactInquiryRepository.countByStatus(InquiryStatus.NEW);
    }
    
    public long countPendingInquiries() {
        return contactInquiryRepository.countByStatus(InquiryStatus.IN_PROGRESS);
    }
    
    private ContactInquiryDTO toDTO(ContactInquiry inquiry) {
        return ContactInquiryDTO.builder()
                .id(inquiry.getId())
                .name(inquiry.getName())
                .email(inquiry.getEmail())
                .phone(inquiry.getPhone())
                .company(inquiry.getCompany())
                .subject(inquiry.getSubject())
                .message(inquiry.getMessage())
                .inquiryType(inquiry.getInquiryType())
                .status(inquiry.getStatus())
                .adminNotes(inquiry.getAdminNotes())
                .createdAt(inquiry.getCreatedAt())
                .build();
    }
}
