package com.example.OhBike.service;

import com.example.OhBike.dto.request.LoginRequest;
import com.example.OhBike.dto.request.RegisterRequest;
import com.example.OhBike.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}