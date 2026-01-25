package com.qualique.controller;

import com.qualique.dto.*;
import com.qualique.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicController {
    
    private final CategoryService categoryService;
    private final ProductService productService;
    private final BrandService brandService;
    private final GalleryService galleryService;
    private final CompanyInfoService companyInfoService;
    
    // Categories
    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> getCategories() {
        return ResponseEntity.ok(ApiResponse.success(categoryService.getActiveCategories()));
    }
    
    @GetMapping("/categories/{id}")
    public ResponseEntity<ApiResponse<CategoryDTO>> getCategory(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(categoryService.getCategoryById(id)));
    }
    
    // Products
    @GetMapping("/products")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getProducts() {
        return ResponseEntity.ok(ApiResponse.success(productService.getActiveProducts()));
    }
    
    @GetMapping("/products/featured")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getFeaturedProducts() {
        return ResponseEntity.ok(ApiResponse.success(productService.getFeaturedProducts()));
    }
    
    @GetMapping("/products/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(productService.getProductById(id)));
    }
    
    @GetMapping("/products/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getProductsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(ApiResponse.success(productService.getProductsByCategory(categoryId)));
    }
    
    @GetMapping("/products/brand/{brandId}")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getProductsByBrand(@PathVariable Long brandId) {
        return ResponseEntity.ok(ApiResponse.success(productService.getProductsByBrand(brandId)));
    }
    
    // Brands
    @GetMapping("/brands")
    public ResponseEntity<ApiResponse<List<BrandDTO>>> getBrands() {
        return ResponseEntity.ok(ApiResponse.success(brandService.getActiveBrands()));
    }
    
    @GetMapping("/brands/{id}")
    public ResponseEntity<ApiResponse<BrandDTO>> getBrand(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(brandService.getBrandById(id)));
    }
    
    // Gallery
    @GetMapping("/gallery")
    public ResponseEntity<ApiResponse<List<GalleryImageDTO>>> getGallery() {
        return ResponseEntity.ok(ApiResponse.success(galleryService.getActiveImages()));
    }
    
    // Company Info
    @GetMapping("/company")
    public ResponseEntity<ApiResponse<CompanyInfoDTO>> getCompanyInfo() {
        return ResponseEntity.ok(ApiResponse.success(companyInfoService.getCompanyInfo()));
    }
}
