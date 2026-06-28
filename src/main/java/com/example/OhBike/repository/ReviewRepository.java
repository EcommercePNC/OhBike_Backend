package com.example.OhBike.repository;

import com.example.OhBike.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
    boolean existsByUser_IdAndProduct_Id(UUID userId, UUID productId);
    List<Review> findByProduct_Id(UUID productId);
}