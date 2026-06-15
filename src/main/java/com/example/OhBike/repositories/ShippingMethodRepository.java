package com.example.OhBike.repositories;

import com.example.OhBike.entities.ShippingMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ShippingMethodRepository extends JpaRepository<ShippingMethod, UUID> {
}
