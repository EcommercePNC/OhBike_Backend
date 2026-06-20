package com.example.OhBike.services;

import com.example.OhBike.dto.request.CouponRequest;
import com.example.OhBike.dto.response.CouponResponse;
import com.example.OhBike.dtos.GeneralResponse;
import com.example.OhBike.entities.Coupon;
import com.example.OhBike.entities.Discount;
import com.example.OhBike.repositories.CouponRepository;
import com.example.OhBike.repositories.DiscountRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final DiscountRepository discountRepository;

    public GeneralResponse create(CouponRequest request) {
        Discount discount = discountRepository.findById(request.getDiscountId())
                .orElseThrow(() -> new RuntimeException("Descuento no encontrado"));
        Coupon entity = Coupon.builder()
                .code(request.getCode())
                .expirationDate(request.getExpirationDate())
                .maxUses(request.getMaxUses())
                .usedCount(0)
                .discount(discount)
                .build();

        Coupon saved = couponRepository.save(entity);

        return new GeneralResponse("Cupón creado exitosamente.", convertToResponse(saved));
    }

    public GeneralResponse update(UUID id, CouponRequest request) {
        Coupon entity = couponRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cupon no encontrado con ID: " + id));
        entity.setCode(request.getCode());
        entity.setMaxUses(request.getMaxUses());
        entity.setExpirationDate(request.getExpirationDate());
        Coupon updated = couponRepository.save(entity);
        return new GeneralResponse("Cupón actualizado exitosamente.", convertToResponse(updated));
    }

    public GeneralResponse delete(UUID id) {
        Coupon entity = couponRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cupon no encontrado con ID: " + id));
        couponRepository.delete(entity);
        return new GeneralResponse("Cupon eliminado exitosamente.", null);
    }

    public Page<CouponResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Coupon> couponPage = couponRepository.findAll(pageable);
        return couponPage.map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public CouponResponse getById(UUID id) {
        return couponRepository.findById(id)
                .map(this::convertToResponse)
                .orElseThrow(() -> new NoSuchElementException("Cupón no encontrado con ID: " + id));
    }

    //validar si el cupon esta activo
    public boolean isCouponValid(UUID id) {
        Coupon entity = couponRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cupón no encontrado"));
        return entity.getExpirationDate().isAfter(LocalDate.now())
                && entity.getUsedCount() < entity.getMaxUses();
    }

    //incrementa el contador de usos del cupon
    @Transactional
    public void incrementUsedCount(UUID id) {
        Coupon entity = couponRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cupón no encontrado"));

        if (entity.getUsedCount() >= entity.getMaxUses()) {
            throw new IllegalStateException("El cupón ya alcanzó su límite de usos");
        }

        entity.setUsedCount(entity.getUsedCount() + 1);
        couponRepository.save(entity);
    }

    private CouponResponse convertToResponse(Coupon entity) {
        return CouponResponse.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .maxUses(entity.getMaxUses())
                .usedCount(entity.getUsedCount())
                .expirationDate(entity.getExpirationDate())
                .discountId(entity.getDiscount().getId())
                .build();
    }
}
