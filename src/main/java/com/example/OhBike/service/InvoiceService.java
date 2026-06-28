package com.example.OhBike.service;

import com.example.OhBike.dto.response.InvoiceResponse;
import com.example.OhBike.entity.Order;
import com.example.OhBike.entity.enums.InvoiceFormat;

import java.util.UUID;

public interface InvoiceService {

    void generateInvoicesForPaidOrder(Order order);

    InvoiceResponse getInvoice(UUID orderId, InvoiceFormat format);

    String getInvoiceContent(UUID orderId, InvoiceFormat format);
}