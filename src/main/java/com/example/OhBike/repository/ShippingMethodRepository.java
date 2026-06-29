package com.example.OhBike.repository;

import com.example.OhBike.entity.ShippingMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ShippingMethodRepository extends JpaRepository<ShippingMethod, UUID> {
}
