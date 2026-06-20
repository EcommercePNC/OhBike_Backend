package com.example.OhBike.controller;

import com.example.OhBike.dto.request.CouponRequest;
import com.example.OhBike.dto.response.GeneralResponse;
import com.example.OhBike.services.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;

    @PostMapping
    public ResponseEntity<GeneralResponse> createCoupon(@Valid @RequestBody CouponRequest request) {
        return buildResponse("Cupón creado exitosamente", HttpStatus.CREATED, couponService.createCoupon(request));
    }

    @GetMapping
    public ResponseEntity<GeneralResponse> getAllCoupons() {
        return buildResponse("Cupones encontrados", HttpStatus.OK, couponService.getAllCoupons());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getByIdCoupon(@PathVariable UUID id) {
        return buildResponse("Cupón se ha encontrado", HttpStatus.OK, couponService.getByIdCoupon(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeneralResponse> updateCoupon(@Valid @RequestBody CouponRequest request, @PathVariable UUID id) {
        return buildResponse("Cupón se ha actualizado exitosamente", HttpStatus.OK, couponService.updateCoupon(request, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> deleteCoupon(@PathVariable UUID id) {
        return buildResponse("Cupón se ha eliminado exitosamente", HttpStatus.OK, couponService.deleteCoupon(id));
    }

    private ResponseEntity<GeneralResponse> buildResponse(String message, HttpStatus status, Object data) {
        String uri = ServletUriComponentsBuilder.fromCurrentRequestUri().build().getPath();
        return ResponseEntity.status(status)
                .body(GeneralResponse.builder()
                        .uri(uri)
                        .message(message)
                        .status(status.value())
                        .time(LocalDateTime.now())
                        .data(data)
                        .build());
    }
}