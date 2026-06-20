package com.example.OhBike.entities;

import com.example.OhBike.entities.enums.DiscountType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Table(name = "discount")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiscountType discountType;

    @Column(nullable = false)
    @Min(value = 0, message = "Debe ser mayor a 0")
    private Float value;

    @Column(nullable = false)
    private Boolean active;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @OneToMany(mappedBy = "discount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Coupon> coupons;
}