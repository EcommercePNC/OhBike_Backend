package com.example.OhBike.repository;

import com.example.OhBike.entity.Order;
import com.example.OhBike.entity.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findByUser_Id(UUID userId);

    List<Order> findByStatus(OrderStatus status);
}