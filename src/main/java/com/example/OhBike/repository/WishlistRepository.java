package com.example.OhBike.repository;

import com.example.OhBike.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WishlistRepository extends JpaRepository<Wishlist, UUID> {
    List<Wishlist> findByUser_Id(UUID userId);
    boolean existsByUser_IdAndProduct_Id(UUID userId, UUID productId);
    void deleteByUser_IdAndProduct_Id(UUID userId, UUID productId);
}
