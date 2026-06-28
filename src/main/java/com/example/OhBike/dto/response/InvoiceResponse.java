package com.example.OhBike.dto.response;

import com.example.OhBike.entity.enums.InvoiceFormat;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class InvoiceResponse {

    private UUID invoiceId;
    private UUID orderId;
    private String invoiceNumber;
    private InvoiceFormat format;
    private BigDecimal total;
    private LocalDateTime issuedAt;
}