package com.example.OhBike.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Table(name = "coupon")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    @Min(value = 0, message = "Debe ser mayor a 0")
    private Integer maxUses;

    @Column(nullable = false)
    @Min(value = 0, message = "Debe ser mayor a 0")
    private Integer usedCount;

    @Column(nullable = false)
    private LocalDate expirationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id", nullable = false)
    private Discount discount;

}