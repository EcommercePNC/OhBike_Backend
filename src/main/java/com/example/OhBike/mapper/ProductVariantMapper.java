package com.example.OhBike.mapper;

import com.example.OhBike.dto.request.ProductVariantRequest;
import com.example.OhBike.dto.request.UpdateProductVariantRequest;
import com.example.OhBike.dto.response.ProductVariantResponse;
import com.example.OhBike.entity.Product;
import com.example.OhBike.entity.ProductVariant;
import org.springframework.stereotype.Component;

@Component
public class ProductVariantMapper {

    public ProductVariant toEntityCreate(ProductVariantRequest request, Product product) {
        return ProductVariant.builder()
                .product(product)
                .size(request.getSize())
                .color(request.getColor())
                .sku(request.getSku())
                .price(request.getPrice())
                .stock(request.getStock())
                .minStock(request.getMinStock())
                .build();
    }

    public ProductVariant toEntityUpdate(UpdateProductVariantRequest request, ProductVariant existing) {
        if (request.getSize() != null && !request.getSize().isBlank()) {
            existing.setSize(request.getSize());
        }
        if (request.getColor() != null && !request.getColor().isBlank()) {
            existing.setColor(request.getColor());
        }
        if (request.getSku() != null && !request.getSku().isBlank()) {
            existing.setSku(request.getSku());
        }
        if (request.getPrice() != null) {
            existing.setPrice(request.getPrice());
        }
        if (request.getStock() != null) {
            existing.setStock(request.getStock());
        }
        if (request.getMinStock() != null) {
            existing.setMinStock(request.getMinStock());
        }
        return existing;
    }

    public ProductVariantResponse toDto(ProductVariant variant) {
        return ProductVariantResponse.builder()
                .id(variant.getId())
                .productId(variant.getProduct().getId())
                .productName(variant.getProduct().getName())
                .size(variant.getSize())
                .color(variant.getColor())
                .sku(variant.getSku())
                .price(variant.getPrice())
                .stock(variant.getStock())
                .minStock(variant.getMinStock())
                .active(variant.getActive())
                .lowStock(variant.getStock() <= variant.getMinStock())
                .createdAt(variant.getCreatedAt())
                .updatedAt(variant.getUpdatedAt())
                .build();
    }
}