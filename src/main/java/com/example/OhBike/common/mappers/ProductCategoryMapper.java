package com.example.OhBike.common.mappers;

import com.example.OhBike.dto.request.ProductCategoryRequest;
import com.example.OhBike.dto.request.UpdateProductCategoryRequest;
import com.example.OhBike.dto.response.ProductCategoryResponse;
import com.example.OhBike.entity.ProductCategory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProductCategoryMapper {
    public ProductCategory toCategoryCreate(ProductCategoryRequest request) {
        return ProductCategory.builder()
                .name(request.getName())
                .build();
    }
    public ProductCategory toCategoryUpdate(UpdateProductCategoryRequest request, UUID id) {
        return ProductCategory.builder()
                .id(id)
                .name(request.getName())
                .build();
    }
    public ProductCategoryResponse toDto(ProductCategory category) {
        return ProductCategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

}
