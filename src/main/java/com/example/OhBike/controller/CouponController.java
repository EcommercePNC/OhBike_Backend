package com.example.OhBike.controller;

import com.example.OhBike.dto.request.CouponRequest;
import com.example.OhBike.dto.request.CouponValidationRequest;
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
        return buildResponse("Coupon created successfully", HttpStatus.CREATED, couponService.createCoupon(request));
    }

    @GetMapping
    public ResponseEntity<GeneralResponse> getAllCoupons() {
        return buildResponse("Coupons retrieved successfully", HttpStatus.OK, couponService.getAllCoupons());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getByIdCoupon(@PathVariable UUID id) {
        return buildResponse("Coupon found successfully", HttpStatus.OK, couponService.getByIdCoupon(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeneralResponse> updateCoupon(@Valid @RequestBody CouponRequest request, @PathVariable UUID id) {
        return buildResponse("Coupon updated successfully", HttpStatus.OK, couponService.updateCoupon(request, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> deleteCoupon(@PathVariable UUID id) {
        return buildResponse("Coupon deleted successfully", HttpStatus.OK, couponService.deleteCoupon(id));
    }

    @PostMapping("/validate")
    public ResponseEntity<GeneralResponse> validateCoupon(@RequestBody CouponValidationRequest request) {
        return buildResponse("Validation successful", HttpStatus.OK,
                couponService.validateCoupon(request.getCode(), request.getPurchaseAmount()));
    }

    @PatchMapping("/{code}/redeem")
    public ResponseEntity<GeneralResponse> redeemCoupon(@PathVariable String code) {
        return buildResponse("Coupon redeemed successfully", HttpStatus.OK, couponService.redeemCoupon(code));
    }

    @PatchMapping("/{code}/cancel")
    public ResponseEntity<GeneralResponse> cancelCoupon(@PathVariable String code) {
        return buildResponse("Coupon cancelled successfully", HttpStatus.OK, couponService.cancelCoupon(code));
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