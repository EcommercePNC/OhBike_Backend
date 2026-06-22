package com.example.OhBike.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable; // <-- Importación clave
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@Table(name = "order_details")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail implements Persistable<UUID> {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id", nullable = false)
    private ProductVariant productVariant;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Transient
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

