package com.example.OhBike.repository;

import com.example.OhBike.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;
import java.util.UUID;

@EnableJpaRepositories
public interface CouponRepository extends JpaRepository<Coupon, UUID> {
    Optional<Coupon> findByCode(String code);
    boolean existsByCode(String code);
}
