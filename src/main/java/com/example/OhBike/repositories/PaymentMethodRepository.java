package com.example.OhBike.repositories;

import com.example.OhBike.entities.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, UUID> {
}
