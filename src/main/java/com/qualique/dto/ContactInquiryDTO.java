package com.qualique.dto;

import com.qualique.entity.ContactInquiry.InquiryStatus;
import com.qualique.entity.ContactInquiry.InquiryType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactInquiryDTO {
    
    private Long id;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email")
    private String email;
    
    private String phone;
    
    private String company;
    
    private String subject;
    
    @NotBlank(message = "Message is required")
    private String message;
    
    private InquiryType inquiryType = InquiryType.GENERAL;
    
    private InquiryStatus status;
    
    private String adminNotes;
    
    private LocalDateTime createdAt;
}
