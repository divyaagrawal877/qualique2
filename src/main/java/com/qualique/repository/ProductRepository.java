package com.qualique.repository;

import com.qualique.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    List<Product> findByActiveTrueOrderByDisplayOrderAsc();
    
    List<Product> findByCategoryIdAndActiveTrueOrderByDisplayOrderAsc(Long categoryId);
    
    List<Product> findByBrandIdAndActiveTrueOrderByDisplayOrderAsc(Long brandId);
    
    List<Product> findByFeaturedTrueAndActiveTrueOrderByDisplayOrderAsc();
    
    Page<Product> findByActiveTrue(Pageable pageable);
    
    Page<Product> findByCategoryIdAndActiveTrue(Long categoryId, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.active = true AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.modelNumber) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Product> searchProducts(@Param("keyword") String keyword, Pageable pageable);
    
    long countByActiveTrue();
    
    long countByCategoryId(Long categoryId);
}
