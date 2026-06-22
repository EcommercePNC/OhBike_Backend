package com.example.OhBike.repository;

import com.example.OhBike.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, java.util.UUID> {
}

