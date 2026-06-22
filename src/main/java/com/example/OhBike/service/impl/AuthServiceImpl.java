package com.example.OhBike.service.impl;

import com.example.OhBike.common.mapper.UserMapper;
import com.example.OhBike.repository.RoleRepository;
import com.example.OhBike.repository.UserRepository;
import com.example.OhBike.service.AuthService;
import com.example.OhBike.util.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.OhBike.dto.request.LoginRequest;
import com.example.OhBike.dto.request.RegisterRequest;
import com.example.OhBike.dto.response.AuthResponse;
import com.example.OhBike.entity.Role;
import com.example.OhBike.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final UserMapper userMapper;

    @Override
    public AuthResponse register(RegisterRequest request) {
        Role userRole = roleRepository.findByName("CLIENTE")
                .orElseThrow(() -> new RuntimeException("Error: Role not found."));

        User user = userMapper.toEntity(request, userRole, passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .message("User registered successfully")
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String jwtToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .message("Successfully logged in")
                .build();
    }
}

