package com.example.OhBike.service.impl;

import com.example.OhBike.dto.request.ProductVariantRequest;
import com.example.OhBike.dto.request.UpdateProductVariantRequest;
import com.example.OhBike.dto.response.ProductVariantResponse;
import com.example.OhBike.entity.Product;
import com.example.OhBike.entity.ProductVariant;
import com.example.OhBike.exception.BusinessRuleException;
import com.example.OhBike.exception.ResourceNotFoundException;
import com.example.OhBike.mapper.ProductVariantMapper;
import com.example.OhBike.repository.ProductRepository;
import com.example.OhBike.repository.ProductVariantRepository;
import com.example.OhBike.service.ProductVariantService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {

    private final ProductVariantRepository variantRepository;
    private final ProductRepository productRepository;
    private final ProductVariantMapper variantMapper;

    @Override
    @Transactional
    public ProductVariantResponse create(ProductVariantRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));

        if (variantRepository.findBySku(request.getSku()).isPresent()) {
            throw new BusinessRuleException("SKU '" + request.getSku() + "' is already in use");
        }

        ProductVariant variant = variantMapper.toEntityCreate(request, product);
        return variantMapper.toDto(variantRepository.save(variant));
    }

    @Override
    public ProductVariantResponse getById(UUID id) {
        return variantMapper.toDto(findActiveVariant(id));
    }

    @Override
    public List<ProductVariantResponse> getByProduct(UUID productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }
        List<ProductVariant> variants = variantRepository.findByProductIdAndActiveTrue(productId);
        if (variants.isEmpty()) {
            throw new ResourceNotFoundException("No variants found for product: " + productId);
        }
        return variants.stream().map(variantMapper::toDto).toList();
    }

    @Override
    public List<ProductVariantResponse> getLowStock() {
        List<ProductVariant> variants = variantRepository.findLowStockVariants();
        if (variants.isEmpty()) {
            throw new ResourceNotFoundException("No variants with low stock found");
        }
        return variants.stream().map(variantMapper::toDto).toList();
    }

    @Override
    @Transactional
    public ProductVariantResponse update(UUID id, UpdateProductVariantRequest request) {
        if (request.getSize() == null && request.getColor() == null &&
                request.getSku() == null && request.getPrice() == null &&
                request.getStock() == null && request.getMinStock() == null) {
            throw new BusinessRuleException("You must provide at least one field to update");
        }

        ProductVariant variant = findActiveVariant(id);

        if (request.getSku() != null && variantRepository.existsBySkuAndIdNot(request.getSku(), id)) {
            throw new BusinessRuleException("SKU '" + request.getSku() + "' is already in use by another variant");
        }

        ProductVariant updated = variantMapper.toEntityUpdate(request, variant);
        return variantMapper.toDto(variantRepository.save(updated));
    }

    @Override
    @Transactional
    public ProductVariantResponse delete(UUID id) {
        ProductVariant variant = findActiveVariant(id);
        variant.setActive(false);
        return variantMapper.toDto(variantRepository.save(variant));
    }

    private ProductVariant findActiveVariant(UUID id) {
        return variantRepository.findById(id)
                .filter(ProductVariant::getActive)
                .orElseThrow(() -> new ResourceNotFoundException("Variant not found with id: " + id));
    }
}