package com.example.OhBike.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable; // <-- Importación clave
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "orders")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Persistable<UUID> {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_method_id", nullable = false)
    private ShippingMethod shippingMethod;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "shipping_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal shippingCost;

    @Column(name = "discount_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountTotal;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> details;

    @Transient // Indica a JPA que esto no va a la base de datos
    private boolean isNew = true;

    @PrePersist
    @PostLoad
    protected void markNotNew() {
        this.isNew = false;
    }

    @Override
    public boolean isNew() {
        return this.isNew || id == null;
    }
}
