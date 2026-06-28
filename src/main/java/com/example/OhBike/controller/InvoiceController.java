package com.example.OhBike.controller;

import com.example.OhBike.dto.response.GeneralResponse;
import com.example.OhBike.entity.enums.InvoiceFormat;
import com.example.OhBike.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping("/orders/{orderId}/pdf")
    public ResponseEntity<String> downloadPdfInvoice(@PathVariable UUID orderId) {
        String content = invoiceService.getInvoiceContent(orderId, InvoiceFormat.PDF);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice-" + orderId + ".pdf")
                .contentType(MediaType.TEXT_PLAIN)
                .body(content);
    }

    @GetMapping("/orders/{orderId}/xml")
    public ResponseEntity<String> downloadXmlInvoice(@PathVariable UUID orderId) {
        String content = invoiceService.getInvoiceContent(orderId, InvoiceFormat.XML);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice-" + orderId + ".xml")
                .contentType(MediaType.APPLICATION_XML)
                .body(content);
    }

    @GetMapping("/orders/{orderId}/pdf/info")
    public ResponseEntity<GeneralResponse> getPdfInvoiceInfo(@PathVariable UUID orderId) {
        return buildResponse(
                "PDF invoice retrieved successfully",
                invoiceService.getInvoice(orderId, InvoiceFormat.PDF)
        );
    }

    @GetMapping("/orders/{orderId}/xml/info")
    public ResponseEntity<GeneralResponse> getXmlInvoiceInfo(@PathVariable UUID orderId) {
        return buildResponse(
                "XML invoice retrieved successfully",
                invoiceService.getInvoice(orderId, InvoiceFormat.XML)
        );
    }

    private ResponseEntity<GeneralResponse> buildResponse(String message, Object data) {
        String uri = ServletUriComponentsBuilder.fromCurrentRequestUri().build().getPath();

        return ResponseEntity.ok(
                GeneralResponse.builder()
                        .uri(uri)
                        .message(message)
                        .status(200)
                        .time(LocalDateTime.now())
                        .data(data)
                        .build()
        );
    }
}