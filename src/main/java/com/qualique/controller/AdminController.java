package com.qualique.controller;

import com.qualique.dto.*;
import com.qualique.entity.ContactInquiry.InquiryStatus;
import com.qualique.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    
    private final CategoryService categoryService;
    private final ProductService productService;
    private final BrandService brandService;
    private final GalleryService galleryService;
    private final CompanyInfoService companyInfoService;
    private final ContactInquiryService contactInquiryService;
    private final DashboardService dashboardService;
    
    // Dashboard
    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DashboardStats>> getDashboardStats() {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getStats()));
    }
    
    // Categories
    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> getAllCategories() {
        return ResponseEntity.ok(ApiResponse.success(categoryService.getAllCategories()));
    }
    
    @PostMapping("/categories")
    public ResponseEntity<ApiResponse<CategoryDTO>> createCategory(@Valid @RequestBody CategoryDTO dto) {
        return ResponseEntity.ok(ApiResponse.success("Category created successfully", 
                categoryService.createCategory(dto)));
    }
    
    @PutMapping("/categories/{id}")
    public ResponseEntity<ApiResponse<CategoryDTO>> updateCategory(@PathVariable Long id, 
                                                                    @Valid @RequestBody CategoryDTO dto) {
        return ResponseEntity.ok(ApiResponse.success("Category updated successfully", 
                categoryService.updateCategory(id, dto)));
    }
    
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success("Category deleted successfully", null));
    }
    
    // Products
    @GetMapping("/products")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllProducts() {
        return ResponseEntity.ok(ApiResponse.success(productService.getAllProducts()));
    }
    
    @PostMapping("/products")
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(@Valid @RequestBody ProductDTO dto) {
        return ResponseEntity.ok(ApiResponse.success("Product created successfully", 
                productService.createProduct(dto)));
    }
    
    @PutMapping("/products/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(@PathVariable Long id, 
                                                                  @Valid @RequestBody ProductDTO dto) {
        return ResponseEntity.ok(ApiResponse.success("Product updated successfully", 
                productService.updateProduct(id, dto)));
    }
    
    @DeleteMapping("/products/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Product deleted successfully", null));
    }
    
    // Brands
    @GetMapping("/brands")
    public ResponseEntity<ApiResponse<List<BrandDTO>>> getAllBrands() {
        return ResponseEntity.ok(ApiResponse.success(brandService.getAllBrands()));
    }
    
    @PostMapping("/brands")
    public ResponseEntity<ApiResponse<BrandDTO>> createBrand(@Valid @RequestBody BrandDTO dto) {
        return ResponseEntity.ok(ApiResponse.success("Brand created successfully", 
                brandService.createBrand(dto)));
    }
    
    @PutMapping("/brands/{id}")
    public ResponseEntity<ApiResponse<BrandDTO>> updateBrand(@PathVariable Long id, 
                                                              @Valid @RequestBody BrandDTO dto) {
        return ResponseEntity.ok(ApiResponse.success("Brand updated successfully", 
                brandService.updateBrand(id, dto)));
    }
    
    @DeleteMapping("/brands/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBrand(@PathVariable Long id) {
        brandService.deleteBrand(id);
        return ResponseEntity.ok(ApiResponse.success("Brand deleted successfully", null));
    }
    
    // Gallery
    @GetMapping("/gallery")
    public ResponseEntity<ApiResponse<List<GalleryImageDTO>>> getAllGalleryImages() {
        return ResponseEntity.ok(ApiResponse.success(galleryService.getAllImages()));
    }
    
    @PostMapping("/gallery")
    public ResponseEntity<ApiResponse<GalleryImageDTO>> createGalleryImage(@Valid @RequestBody GalleryImageDTO dto) {
        return ResponseEntity.ok(ApiResponse.success("Gallery image added successfully", 
                galleryService.createImage(dto)));
    }
    
    @PutMapping("/gallery/{id}")
    public ResponseEntity<ApiResponse<GalleryImageDTO>> updateGalleryImage(@PathVariable Long id, 
                                                                            @Valid @RequestBody GalleryImageDTO dto) {
        return ResponseEntity.ok(ApiResponse.success("Gallery image updated successfully", 
                galleryService.updateImage(id, dto)));
    }
    
    @DeleteMapping("/gallery/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteGalleryImage(@PathVariable Long id) {
        galleryService.deleteImage(id);
        return ResponseEntity.ok(ApiResponse.success("Gallery image deleted successfully", null));
    }
    
    // Company Info
    @GetMapping("/company")
    public ResponseEntity<ApiResponse<CompanyInfoDTO>> getCompanyInfo() {
        return ResponseEntity.ok(ApiResponse.success(companyInfoService.getCompanyInfo()));
    }
    
    @PutMapping("/company")
    public ResponseEntity<ApiResponse<CompanyInfoDTO>> updateCompanyInfo(@RequestBody CompanyInfoDTO dto) {
        return ResponseEntity.ok(ApiResponse.success("Company info updated successfully", 
                companyInfoService.saveCompanyInfo(dto)));
    }
    
    // Inquiries
    @GetMapping("/inquiries")
    public ResponseEntity<ApiResponse<Page<ContactInquiryDTO>>> getInquiries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.success(contactInquiryService.getAllInquiries(pageable)));
    }
    
    @GetMapping("/inquiries/recent")
    public ResponseEntity<ApiResponse<List<ContactInquiryDTO>>> getRecentInquiries() {
        return ResponseEntity.ok(ApiResponse.success(contactInquiryService.getRecentInquiries()));
    }
    
    @GetMapping("/inquiries/{id}")
    public ResponseEntity<ApiResponse<ContactInquiryDTO>> getInquiry(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(contactInquiryService.getInquiryById(id)));
    }
    
    @PutMapping("/inquiries/{id}/status")
    public ResponseEntity<ApiResponse<ContactInquiryDTO>> updateInquiryStatus(
            @PathVariable Long id,
            @RequestParam InquiryStatus status,
            @RequestParam(required = false) String adminNotes) {
        return ResponseEntity.ok(ApiResponse.success("Inquiry status updated", 
                contactInquiryService.updateInquiryStatus(id, status, adminNotes)));
    }
    
    @DeleteMapping("/inquiries/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteInquiry(@PathVariable Long id) {
        contactInquiryService.deleteInquiry(id);
        return ResponseEntity.ok(ApiResponse.success("Inquiry deleted successfully", null));
    }
}
