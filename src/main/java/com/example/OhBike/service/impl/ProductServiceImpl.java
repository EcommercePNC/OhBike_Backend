package com.example.OhBike.service.impl;

import com.example.OhBike.common.mappers.ProductMapper;
import com.example.OhBike.dto.request.ProductRequest;
import com.example.OhBike.dto.request.UpdateProductRequest;
import com.example.OhBike.dto.response.ProductResponse;
import com.example.OhBike.entity.Product;
import com.example.OhBike.entity.ProductCategory;
import com.example.OhBike.exception.BusinessRuleException;
import com.example.OhBike.exception.ResourceNotFoundException;
import com.example.OhBike.repository.ProductCategoryRepository;
import com.example.OhBike.repository.ProductRepository;
import com.example.OhBike.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final ProductCategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        if (productRepository.existsByNameIgnoreCase(request.getName())) {
            throw new BusinessRuleException("Product name already exists");
        }
        ProductCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        Product product = productMapper.toEntityCreate(request, category);
        Product savedProduct = productRepository.save(product);

        return productMapper.toDto(savedProduct);
    }
    @Override
    public List<ProductResponse> getAllProducts(UUID categoryId) {
        List<Product> products;
        if (categoryId != null) {
            if (!categoryRepository.existsById(categoryId)) {
                throw new ResourceNotFoundException("Category not found with id: " + categoryId);
            }
            products = productRepository.findByProductCategoryId(categoryId);
        } else {
            products = productRepository.findAll();
        }

        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No registered products were found");
        }

        return products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductResponse getProductById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        return productMapper.toDto(product);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(UpdateProductRequest request, UUID id) {
        if (request.getName() == null &&
                request.getDescription() == null &&
                request.getBasePrice() == null &&
                request.getCategoryId() == null) {
            throw new BusinessRuleException("You must provide at least one field to update");
        }

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        if (request.getName() != null && !request.getName().isBlank() &&
                !existingProduct.getName().equalsIgnoreCase(request.getName())) {
            if (productRepository.existsByNameIgnoreCase(request.getName())) {
                throw new BusinessRuleException("Product name is already in use: " + request.getName());
            }
        }
        ProductCategory categoryToUpdate = null;
        if(request.getCategoryId() != null){
            categoryToUpdate = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: "));
        }
        Product updatedProduct = productMapper.toEntityUpdate(request, existingProduct, categoryToUpdate);
        return productMapper.toDto(productRepository.save(updatedProduct));
    }

    @Override
    @Transactional
    public ProductResponse deleteProduct(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        ProductResponse response = productMapper.toDto(product);
        productRepository.deleteById(id);

        return response;
    }
}
