package com.example.OhBike.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CartSummaryResponse {

    private BigDecimal subtotal;
    private BigDecimal discount;     // descuento si aplica, si no: 0
    private BigDecimal shipping;     // costo de envío estimado
    private BigDecimal total;
    private int totalItems;
}