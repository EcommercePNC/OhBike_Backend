package com.example.OhBike.repository;

import com.example.OhBike.entity.Invoice;
import com.example.OhBike.entity.enums.InvoiceFormat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

    Optional<Invoice> findByOrderIdAndFormat(UUID orderId, InvoiceFormat format);

    boolean existsByOrderIdAndFormat(UUID orderId, InvoiceFormat format);
}