package com.qualique.controller;

import com.qualique.dto.ApiResponse;
import com.qualique.dto.ContactInquiryDTO;
import com.qualique.service.ContactInquiryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ContactController {
    
    private final ContactInquiryService contactInquiryService;
    
    @PostMapping("/contact")
    public ResponseEntity<ApiResponse<ContactInquiryDTO>> submitInquiry(@Valid @RequestBody ContactInquiryDTO dto) {
        ContactInquiryDTO saved = contactInquiryService.createInquiry(dto);
        return ResponseEntity.ok(ApiResponse.success("Thank you for your inquiry! We will get back to you soon.", saved));
    }
}
