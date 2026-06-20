package com.example.OhBike.repositories;

import com.example.OhBike.entities.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.UUID;

@EnableJpaRepositories
public interface CouponRepository extends JpaRepository<Coupon, UUID> {
    boolean existsByCode(String code);
}
