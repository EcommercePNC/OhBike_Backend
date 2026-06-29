package com.example.OhBike.repository;

import com.example.OhBike.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByProductCategoryId(UUID categoryId);
    boolean existsByNameIgnoreCase(String name);
    boolean existsByProductCategoryId(UUID categoryId);
    List<Product> findBySellerId(UUID sellerId);
}
