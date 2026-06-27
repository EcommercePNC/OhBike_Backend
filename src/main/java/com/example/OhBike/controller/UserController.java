package com.example.OhBike.controller;

import com.example.OhBike.dto.request.UserUpdateRequest;
import com.example.OhBike.dto.response.UserResponse;
import com.example.OhBike.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyProfile(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(userService.getMe(email));
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMyProfile(
            Authentication authentication,
            @Valid @RequestBody UserUpdateRequest request) {

        String email = authentication.getName();
        return ResponseEntity.ok(userService.updateMe(email, request));
    }
}