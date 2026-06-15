package com.example.OhBike.services;

import com.example.OhBike.dtos.GeneralResponse;
import com.example.OhBike.dtos.request.PaymentMethodRequest;
import com.example.OhBike.dtos.response.PaymentMethodResponse;
import com.example.OhBike.entities.PaymentMethod;
import com.example.OhBike.repositories.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class PaymentMethodService {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    public GeneralResponse create(PaymentMethodRequest request) {
        PaymentMethod entity = PaymentMethod.builder()
                .name(request.name())
                .description(request.description())
                .build();
        PaymentMethod saved = paymentMethodRepository.save(entity);
        // Instanciación directa del Record
        return new GeneralResponse("Método de pago registrado exitosamente.", convertToResponse(saved));
    }

    public GeneralResponse getAll() {
        List<PaymentMethodResponse> list = paymentMethodRepository.findAll().stream()
                .map(this::convertToResponse).toList();
        return new GeneralResponse("Métodos de pagos de pago recuperados exitosamente", list);
    }

    public GeneralResponse getById(UUID id) {
        PaymentMethod entity = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Método de pago no encontrado con ID: " + id));
        return new GeneralResponse("Método de pago no encontrado.", convertToResponse(entity));
    }

    public GeneralResponse update(UUID id, PaymentMethodRequest request) {
        PaymentMethod entity = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Método de pago no encontrado con ID: " + id));
        entity.setName(request.name());
        entity.setDescription(request.description());
        PaymentMethod updated = paymentMethodRepository.save(entity);
        return new GeneralResponse("Método de pago actualizado exitosamente.", convertToResponse(updated));
    }

    public GeneralResponse delete(UUID id) {
        PaymentMethod entity = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Método de pago no encontrado con ID: " + id));
        paymentMethodRepository.delete(entity);
        return new GeneralResponse("Método de pago eliminado exitosamente.", null);
    }

    private PaymentMethodResponse convertToResponse(PaymentMethod entity) {
        return new PaymentMethodResponse(entity.getId(), entity.getName(), entity.getDescription());
    }
}
