package com.example.OhBike.repository;

import com.example.OhBike.entities.Product;
import com.example.OhBike.entities.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByProductCategoryId(UUID id);
    boolean existsByNameIgnoreCase(String name);
    List<Product> findByProductCategoryAndAvailable(ProductCategory category, Boolean available);
}
