package com.example.OhBike.common.mapper;

import com.example.OhBike.dto.request.ProductRequest;
import com.example.OhBike.dto.request.UpdateProductRequest;
import com.example.OhBike.dto.response.ProductResponse;
import com.example.OhBike.entity.Product;
import com.example.OhBike.entity.ProductCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class    ProductMapper {
    private final ProductCategoryMapper categoryMapper;

    public Product toEntityCreate(ProductRequest request, ProductCategory category) {
        return Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .basePrice(request.getBasePrice())
                .available(true)
                .productCategory(category)
                .build();
}
    public Product toEntityUpdate(UpdateProductRequest request, Product existingProduct, ProductCategory newCategory) {
        if (request.getName() != null && !request.getName().isBlank()) {
            existingProduct.setName(request.getName());
        }
        if (request.getDescription() != null) {
            existingProduct.setDescription(request.getDescription());
        }
        if (request.getBasePrice() != null) {
            existingProduct.setBasePrice(request.getBasePrice());
        }
        if (newCategory != null) {
            existingProduct.setProductCategory(newCategory);
        }
        return existingProduct;
    }

    public ProductResponse toDto(Product product) {
        if (product == null) {return null;}
            return ProductResponse.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .basePrice(product.getBasePrice())
                    .available(product.getAvailable())
                    .category(categoryMapper.toDto(product.getProductCategory()))
                    .sellerId(product.getSeller().getId())
                    .sellerName(product.getSeller().getName())
                    .build();
}}
