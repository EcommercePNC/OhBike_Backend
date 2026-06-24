package com.example.OhBike.repository;

import com.example.OhBike.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID> {
}