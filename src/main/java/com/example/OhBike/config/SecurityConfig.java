package com.example.OhBike.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Desactivar protección CSRF para poder hacer POST/PUT desde Insomnia sin tokens
                .csrf(csrf -> csrf.disable())

                // 2. Configurar los permisos de las URLs de la tienda
                .authorizeHttpRequests(auth -> auth
                        // Dar acceso libre y público total a tus catálogos del Sprint 1
                        .requestMatchers("/api/v1/payment-methods/**").permitAll()
                        .requestMatchers("/api/v1/shipping-methods/**").permitAll()

                        // Permitir que cualquier otra ruta pase de momento (para no bloquear a tus compañeros)
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}
