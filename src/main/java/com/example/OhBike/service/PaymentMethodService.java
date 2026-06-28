package com.example.OhBike.service;

import com.example.OhBike.dto.request.PaymentMethodRequest;
import com.example.OhBike.dto.response.PaymentMethodResponse;

import java.util.List;
import java.util.UUID;

public interface PaymentMethodService {
    List<PaymentMethodResponse> getAllPaymentMethods();
    PaymentMethodResponse getPaymentMethodById(UUID id);
    PaymentMethodResponse createPaymentMethod(PaymentMethodRequest request);
    PaymentMethodResponse updatePaymentMethod(UUID id, PaymentMethodRequest request);
    void deletePaymentMethod(UUID id);
}
