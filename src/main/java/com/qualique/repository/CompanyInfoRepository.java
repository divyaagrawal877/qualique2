package com.qualique.repository;

import com.qualique.entity.CompanyInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyInfoRepository extends JpaRepository<CompanyInfo, Long> {
    
    default Optional<CompanyInfo> getCompanyInfo() {
        return findAll().stream().findFirst();
    }
}
