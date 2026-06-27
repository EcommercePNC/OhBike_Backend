package com.example.OhBike.repository;

import com.example.OhBike.entity.Order;
import com.example.OhBike.entity.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findByUser_Id(UUID userId);
    List<Order> findByStatus(OrderStatus status);

    @Query("SELECT COUNT(o) > 0 FROM Order o " +
            "JOIN o.details d " +
            "WHERE o.user.id = :userId " +
            "AND d.variant.product.id = :productId " +
            "AND o.status = :status")
    boolean existsByUserIdAndProductIdAndStatus(@Param("userId") UUID userId,
                                                @Param("productId") UUID productId,
                                                @Param("status") OrderStatus status);
}