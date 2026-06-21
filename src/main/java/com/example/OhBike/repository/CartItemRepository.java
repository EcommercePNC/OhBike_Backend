package com.example.OhBike.repository;

import com.example.OhBike.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {

    Optional<CartItem> findByCart_IdAndVariant_Id(UUID cartId, UUID variantId);
}