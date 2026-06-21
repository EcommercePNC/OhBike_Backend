package com.example.OhBike.repository;

import com.example.OhBike.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
import java.util.UUID;

@EnableJpaRepositories
public interface DiscountRepository extends JpaRepository<Discount, UUID> {
    List<Discount> findByActiveTrue();
}