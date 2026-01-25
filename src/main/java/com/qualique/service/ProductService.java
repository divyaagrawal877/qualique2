package com.qualique.service;

import com.qualique.dto.ProductDTO;
import com.qualique.entity.Brand;
import com.qualique.entity.Category;
import com.qualique.entity.Product;
import com.qualique.repository.BrandRepository;
import com.qualique.repository.CategoryRepository;
import com.qualique.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    
    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ProductDTO> getActiveProducts() {
        return productRepository.findByActiveTrueOrderByDisplayOrderAsc()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<ProductDTO> getFeaturedProducts() {
        return productRepository.findByFeaturedTrueAndActiveTrueOrderByDisplayOrderAsc()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryIdAndActiveTrueOrderByDisplayOrderAsc(categoryId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<ProductDTO> getProductsByBrand(Long brandId) {
        return productRepository.findByBrandIdAndActiveTrueOrderByDisplayOrderAsc(brandId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public Page<ProductDTO> searchProducts(String keyword, Pageable pageable) {
        return productRepository.searchProducts(keyword, pageable)
                .map(this::toDTO);
    }
    
    public ProductDTO getProductById(Long id) {
        return productRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }
    
    @Transactional
    public ProductDTO createProduct(ProductDTO dto) {
        Product product = toEntity(dto);
        return toDTO(productRepository.save(product));
    }
    
    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setSpecifications(dto.getSpecifications());
        product.setImageUrl(dto.getImageUrl());
        product.setModelNumber(dto.getModelNumber());
        product.setPartNumber(dto.getPartNumber());
        product.setFeatured(dto.getFeatured());
        product.setActive(dto.getActive());
        product.setDisplayOrder(dto.getDisplayOrder());
        
        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }
        
        if (dto.getBrandId() != null) {
            Brand brand = brandRepository.findById(dto.getBrandId())
                    .orElseThrow(() -> new RuntimeException("Brand not found"));
            product.setBrand(brand);
        }
        
        return toDTO(productRepository.save(product));
    }
    
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }
        productRepository.deleteById(id);
    }
    
    private ProductDTO toDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .specifications(product.getSpecifications())
                .imageUrl(product.getImageUrl())
                .modelNumber(product.getModelNumber())
                .partNumber(product.getPartNumber())
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .brandId(product.getBrand() != null ? product.getBrand().getId() : null)
                .brandName(product.getBrand() != null ? product.getBrand().getName() : null)
                .featured(product.getFeatured())
                .active(product.getActive())
                .displayOrder(product.getDisplayOrder())
                .build();
    }
    
    private Product toEntity(ProductDTO dto) {
        Product product = Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .specifications(dto.getSpecifications())
                .imageUrl(dto.getImageUrl())
                .modelNumber(dto.getModelNumber())
                .partNumber(dto.getPartNumber())
                .featured(dto.getFeatured() != null ? dto.getFeatured() : false)
                .active(dto.getActive() != null ? dto.getActive() : true)
                .displayOrder(dto.getDisplayOrder())
                .build();
        
        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }
        
        if (dto.getBrandId() != null) {
            Brand brand = brandRepository.findById(dto.getBrandId())
                    .orElseThrow(() -> new RuntimeException("Brand not found"));
            product.setBrand(brand);
        }
        
        return product;
    }
}
