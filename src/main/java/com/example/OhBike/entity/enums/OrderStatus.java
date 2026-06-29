package com.example.OhBike.entity.enums;

public enum OrderStatus {
    PENDING,   // Orden creada pero pendiente de pago
    PAID,      // Cliente realizó el pago
    SHIPPED    // Se confirmó envío
}