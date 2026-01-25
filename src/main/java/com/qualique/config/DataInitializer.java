package com.qualique.config;

import com.qualique.entity.AdminUser;
import com.qualique.entity.CompanyInfo;
import com.qualique.repository.AdminUserRepository;
import com.qualique.repository.CompanyInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final AdminUserRepository adminUserRepository;
    private final CompanyInfoRepository companyInfoRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) {
        // Create default admin user if not exists
        if (!adminUserRepository.existsByUsername("admin")) {
            AdminUser admin = AdminUser.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .name("Administrator")
                    .email("admin@qualique.com")
                    .role(AdminUser.Role.SUPER_ADMIN)
                    .active(true)
                    .mustChangePassword(true)  // Force password change on first login
                    .failedLoginAttempts(0)
                    .build();
            adminUserRepository.save(admin);
            log.info("Default admin user created: username=admin, password=admin123");
            log.warn("SECURITY: Please change the default password immediately after first login!");
        }
        
        // Create default company info if not exists
        if (companyInfoRepository.count() == 0) {
            CompanyInfo companyInfo = CompanyInfo.builder()
                    .companyName("Qualique Corporation")
                    .aboutUs("Welcome to Qualique Corporation! We are a leading supplier of industrial products and services.")
                    .tagline("Quality Products, Quality Service")
                    .email("info@qualique.com")
                    .phone("+91 1234567890")
                    .address("123 Business Street")
                    .city("Mumbai")
                    .state("Maharashtra")
                    .country("India")
                    .copyrightText("© 2026 Qualique Corporation. All Rights Reserved.")
                    .build();
            companyInfoRepository.save(companyInfo);
            log.info("Default company info created");
        }
    }
}
