package com.example.OhBike.service.impl;

import com.example.OhBike.dto.response.InvoiceResponse;
import com.example.OhBike.entity.Invoice;
import com.example.OhBike.entity.Order;
import com.example.OhBike.entity.OrderDetail;
import com.example.OhBike.entity.enums.InvoiceFormat;
import com.example.OhBike.entity.enums.OrderStatus;
import com.example.OhBike.exception.BusinessRuleException;
import com.example.OhBike.exception.ResourceNotFoundException;
import com.example.OhBike.repository.InvoiceRepository;
import com.example.OhBike.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;

    @Override
    public void generateInvoicesForPaidOrder(Order order) {
        if (order.getStatus() != OrderStatus.PAID) {
            throw new BusinessRuleException("Invoices can only be generated for paid orders");
        }

        createInvoiceIfNotExists(order, InvoiceFormat.PDF, buildPdfContent(order));
        createInvoiceIfNotExists(order, InvoiceFormat.XML, buildXmlContent(order));
    }

    @Override
    public InvoiceResponse getInvoice(UUID orderId, InvoiceFormat format) {
        return toResponse(findInvoice(orderId, format));
    }

    @Override
    public String getInvoiceContent(UUID orderId, InvoiceFormat format) {
        return findInvoice(orderId, format).getContent();
    }

    private void createInvoiceIfNotExists(Order order, InvoiceFormat format, String content) {
        if (invoiceRepository.existsByOrderIdAndFormat(order.getId(), format)) {
            return;
        }

        Invoice invoice = Invoice.builder()
                .order(order)
                .invoiceNumber(generateInvoiceNumber(order, format))
                .format(format)
                .total(order.getTotal())
                .content(content)
                .build();

        invoiceRepository.save(invoice);
    }

    private Invoice findInvoice(UUID orderId, InvoiceFormat format) {
        return invoiceRepository.findByOrderIdAndFormat(orderId, format)
                .orElseThrow(() -> new ResourceNotFoundException(
                        format + " invoice not found for order: " + orderId
                ));
    }

    private String generateInvoiceNumber(Order order, InvoiceFormat format) {
        return "INV-" + format + "-" + order.getId().toString().substring(0, 8).toUpperCase();
    }

    private String buildPdfContent(Order order) {
        StringBuilder builder = new StringBuilder();

        builder.append("OHBIKE ELECTRONIC INVOICE\n");
        builder.append("Order ID: ").append(order.getId()).append("\n");
        builder.append("Customer: ").append(order.getUser().getName()).append("\n");
        builder.append("Email: ").append(order.getUser().getEmail()).append("\n");
        builder.append("Payment Method: ").append(order.getPaymentMethod().getName()).append("\n");
        builder.append("Shipping Method: ").append(order.getShippingMethod().getName()).append("\n\n");

        builder.append("ITEMS\n");
        for (OrderDetail detail : order.getDetails()) {
            builder.append("- ")
                    .append(detail.getVariant().getProduct().getName())
                    .append(" | SKU: ").append(detail.getVariant().getSku())
                    .append(" | Qty: ").append(detail.getQuantity())
                    .append(" | Unit Price: ").append(detail.getUnitPrice())
                    .append(" | Subtotal: ").append(detail.getSubtotal())
                    .append("\n");
        }

        builder.append("\nSubtotal: ").append(order.getSubtotal()).append("\n");
        builder.append("Discount: ").append(order.getDiscountAmount()).append("\n");
        builder.append("Shipping: ").append(order.getShippingCost()).append("\n");
        builder.append("Total: ").append(order.getTotal()).append("\n");

        return builder.toString();
    }

    private String buildXmlContent(Order order) {
        StringBuilder builder = new StringBuilder();

        builder.append("<invoice>");
        builder.append("<orderId>").append(order.getId()).append("</orderId>");
        builder.append("<customer>");
        builder.append("<name>").append(order.getUser().getName()).append("</name>");
        builder.append("<email>").append(order.getUser().getEmail()).append("</email>");
        builder.append("</customer>");
        builder.append("<paymentMethod>").append(order.getPaymentMethod().getName()).append("</paymentMethod>");
        builder.append("<shippingMethod>").append(order.getShippingMethod().getName()).append("</shippingMethod>");
        builder.append("<items>");

        for (OrderDetail detail : order.getDetails()) {
            builder.append("<item>");
            builder.append("<productName>").append(detail.getVariant().getProduct().getName()).append("</productName>");
            builder.append("<sku>").append(detail.getVariant().getSku()).append("</sku>");
            builder.append("<quantity>").append(detail.getQuantity()).append("</quantity>");
            builder.append("<unitPrice>").append(detail.getUnitPrice()).append("</unitPrice>");
            builder.append("<subtotal>").append(detail.getSubtotal()).append("</subtotal>");
            builder.append("</item>");
        }

        builder.append("</items>");
        builder.append("<subtotal>").append(order.getSubtotal()).append("</subtotal>");
        builder.append("<discount>").append(order.getDiscountAmount()).append("</discount>");
        builder.append("<shipping>").append(order.getShippingCost()).append("</shipping>");
        builder.append("<total>").append(order.getTotal()).append("</total>");
        builder.append("</invoice>");

        return builder.toString();
    }

    private InvoiceResponse toResponse(Invoice invoice) {
        return InvoiceResponse.builder()
                .invoiceId(invoice.getId())
                .orderId(invoice.getOrder().getId())
                .invoiceNumber(invoice.getInvoiceNumber())
                .format(invoice.getFormat())
                .total(invoice.getTotal())
                .issuedAt(invoice.getIssuedAt())
                .build();
    }
}