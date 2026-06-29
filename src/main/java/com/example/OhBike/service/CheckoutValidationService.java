package com.example.OhBike.service;

import com.example.OhBike.dto.request.CheckoutRequest;
import com.example.OhBike.dto.response.CheckoutValidationResponse;

public interface CheckoutValidationService {
    CheckoutValidationResponse validate(CheckoutRequest request, String email);
}