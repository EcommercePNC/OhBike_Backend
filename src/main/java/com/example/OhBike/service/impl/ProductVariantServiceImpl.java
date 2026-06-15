package com.example.OhBike.service.impl;

import com.example.OhBike.dto.request.ProductVariantRequestDTO;
import com.example.OhBike.dto.response.ProductVariantResponseDTO;
import com.example.OhBike.entities.Product;
import com.example.OhBike.entities.ProductVariant;
import com.example.OhBike.exception.ResourceNotFoundException;
import com.example.OhBike.repositories.ProductRepository;
import com.example.OhBike.repositories.ProductVariantRepository;
import com.example.OhBike.service.ProductVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {

    private final ProductVariantRepository variantRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public ProductVariantResponseDTO create(ProductVariantRequestDTO request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto", request.getProductId()));

        if (variantRepository.findBySku(request.getSku()).isPresent()) {
            throw new IllegalArgumentException("El SKU '" + request.getSku() + "' ya está en uso.");
        }

        ProductVariant variant = ProductVariant.builder()
                .product(product)
                .size(request.getSize())
                .color(request.getColor())
                .sku(request.getSku())
                .price(request.getPrice())
                .stock(request.getStock())
                .minStock(request.getMinStock())
                .active(true)
                .build();

        return toResponse(variantRepository.save(variant));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductVariantResponseDTO getById(UUID id) {
        return toResponse(findActiveVariant(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductVariantResponseDTO> getByProduct(UUID productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Producto", productId);
        }
        return variantRepository.findByProductIdAndActiveTrue(productId)
                .stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductVariantResponseDTO> getLowStock() {
        return variantRepository.findLowStockVariants()
                .stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public ProductVariantResponseDTO update(UUID id, ProductVariantRequestDTO request) {
        ProductVariant variant = findActiveVariant(id);

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto", request.getProductId()));

        if (variantRepository.existsBySkuAndIdNot(request.getSku(), id)) {
            throw new IllegalArgumentException("El SKU '" + request.getSku() + "' ya está en uso por otra variante.");
        }

        variant.setProduct(product);
        variant.setSize(request.getSize());
        variant.setColor(request.getColor());
        variant.setSku(request.getSku());
        variant.setPrice(request.getPrice());
        variant.setStock(request.getStock());
        variant.setMinStock(request.getMinStock());

        return toResponse(variantRepository.save(variant));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        ProductVariant variant = findActiveVariant(id);
        variant.setActive(false);
        variantRepository.save(variant);
    }


    private ProductVariant findActiveVariant(UUID id) {
        return variantRepository.findById(id)
                .filter(ProductVariant::getActive)
                .orElseThrow(() -> new ResourceNotFoundException("Variante", id));
    }

    private ProductVariantResponseDTO toResponse(ProductVariant v) {
        return ProductVariantResponseDTO.builder()
                .id(v.getId())
                .productId(v.getProduct().getId())
                .productName(v.getProduct().getName())
                .size(v.getSize())
                .color(v.getColor())
                .sku(v.getSku())
                .price(v.getPrice())
                .stock(v.getStock())
                .minStock(v.getMinStock())
                .active(v.getActive())
                .lowStock(v.getStock() <= v.getMinStock())
                .createdAt(v.getCreatedAt())
                .updatedAt(v.getUpdatedAt())
                .build();
    }
}