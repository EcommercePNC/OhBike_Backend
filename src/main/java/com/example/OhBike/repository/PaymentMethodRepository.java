package com.example.OhBike.repository;

import com.example.OhBike.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, UUID> {
}
