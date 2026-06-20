package com.example.OhBike.repositories;

import com.example.OhBike.entities.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
import java.util.UUID;

@EnableJpaRepositories
public interface DiscountRepository extends JpaRepository<Discount, UUID> {
    List<Discount> findByActiveTrue();
}
