package com.qualique.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contact_inquiries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactInquiry {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String email;
    
    private String phone;
    
    private String company;
    
    private String subject;
    
    @Column(nullable = false, length = 5000)
    private String message;
    
    @Enumerated(EnumType.STRING)
    private InquiryType inquiryType = InquiryType.GENERAL;
    
    @Enumerated(EnumType.STRING)
    private InquiryStatus status = InquiryStatus.NEW;
    
    private String adminNotes;
    
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum InquiryType {
        GENERAL, QUOTE, DEMO, SUPPORT
    }
    
    public enum InquiryStatus {
        NEW, IN_PROGRESS, RESOLVED, CLOSED
    }
}
