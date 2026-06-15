package com.example.OhBike.repositories;

import com.example.OhBike.entities.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, UUID> {

    List<ProductVariant> findByProductIdAndActiveTrue(UUID productId);

    Optional<ProductVariant> findBySku(String sku);

    @Query("SELECT v FROM ProductVariant v WHERE v.stock <= v.minStock AND v.active = true")
    List<ProductVariant> findLowStockVariants();

    boolean existsBySkuAndIdNot(String sku, UUID id);
}