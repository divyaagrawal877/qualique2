package com.qualique.controller;

import com.qualique.dto.ApiResponse;
import com.qualique.dto.LoginRequest;
import com.qualique.dto.LoginResponse;
import com.qualique.entity.AdminUser;
import com.qualique.repository.AdminUserRepository;
import com.qualique.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    
    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final int LOCKOUT_DURATION_MINUTES = 15;
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login attempt for username: {}", request.getUsername());
        
        AdminUser user = adminUserRepository.findByUsername(request.getUsername())
                .orElse(null);
        
        if (user == null) {
            log.warn("Login failed: User not found - {}", request.getUsername());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid username or password"));
        }
        
        // Check if account is locked
        if (user.getLockoutTime() != null && 
            user.getLockoutTime().isAfter(LocalDateTime.now())) {
            long minutesRemaining = java.time.Duration.between(
                LocalDateTime.now(), user.getLockoutTime()).toMinutes() + 1;
            log.warn("Login failed: Account locked - {}", request.getUsername());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Account is locked. Try again in " + minutesRemaining + " minutes"));
        }
        
        // Check password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            // Increment failed attempts
            int attempts = (user.getFailedLoginAttempts() == null ? 0 : user.getFailedLoginAttempts()) + 1;
            user.setFailedLoginAttempts(attempts);
            
            if (attempts >= MAX_FAILED_ATTEMPTS) {
                user.setLockoutTime(LocalDateTime.now().plusMinutes(LOCKOUT_DURATION_MINUTES));
                log.warn("Account locked due to {} failed attempts - {}", attempts, request.getUsername());
            }
            
            adminUserRepository.save(user);
            
            int remainingAttempts = MAX_FAILED_ATTEMPTS - attempts;
            if (remainingAttempts > 0) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Invalid password. " + remainingAttempts + " attempts remaining"));
            } else {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Account locked for " + LOCKOUT_DURATION_MINUTES + " minutes"));
            }
        }
        
        if (!user.getActive()) {
            log.warn("Login failed: Account disabled - {}", request.getUsername());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Account is disabled. Contact administrator."));
        }
        
        // Reset failed attempts on successful login
        user.setFailedLoginAttempts(0);
        user.setLockoutTime(null);
        user.setLastLogin(LocalDateTime.now());
        adminUserRepository.save(user);
        
        String token = jwtUtil.generateToken(user);
        
        LoginResponse response = LoginResponse.builder()
                .token(token)
                .username(user.getUsername())
                .name(user.getName())
                .role(user.getRole().name())
                .mustChangePassword(user.getMustChangePassword() != null && user.getMustChangePassword())
                .build();
        
        log.info("Login successful for user: {}", request.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }
    
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Object>> changePassword(@RequestBody Map<String, String> request,
                                                               @RequestHeader("Authorization") String authHeader) {
        String currentPassword = request.get("currentPassword");
        String newPassword = request.get("newPassword");
        
        if (newPassword == null || newPassword.length() < 8) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Password must be at least 8 characters long"));
        }
        
        // Validate password strength
        if (!isPasswordStrong(newPassword)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"));
        }
        
        // Extract username from token
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(token);
        
        AdminUser user = adminUserRepository.findByUsername(username)
                .orElse(null);
        
        if (user == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("User not found"));
        }
        
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Current password is incorrect"));
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setMustChangePassword(false);
        user.setPasswordChangedAt(LocalDateTime.now());
        adminUserRepository.save(user);
        
        log.info("Password changed successfully for user: {}", username);
        return ResponseEntity.ok(ApiResponse.success("Password changed successfully", null));
    }
    
    private boolean isPasswordStrong(String password) {
        // At least 8 chars, 1 uppercase, 1 lowercase, 1 number, 1 special char
        return password.length() >= 8 &&
               password.matches(".*[A-Z].*") &&
               password.matches(".*[a-z].*") &&
               password.matches(".*[0-9].*") &&
               password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
    }
}
