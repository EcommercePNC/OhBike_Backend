package com.example.OhBike.config;

import com.example.OhBike.entities.Role;
import com.example.OhBike.repositories.RoleRepository;
import com.example.OhBike.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;

@Configuration
public class DataLoaderConfig {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, RoleRepository roleRepository, JdbcTemplate jdbcTemplate) {
        return args -> {

            Role clienteRole;
            if (roleRepository.count() == 0) {
                clienteRole = Role.builder().name("ROLE_CLIENTE").build();
                clienteRole = roleRepository.save(clienteRole);
                System.out.println("Rol ROLE_CLIENTE creado");
            } else {
                clienteRole = roleRepository.findAll().stream()
                        .filter(r -> r.getName().equals("ROLE_CLIENTE"))
                        .findFirst()
                        .orElseThrow(); // Tomamos el que ya existe
            }

            // 2. Creamos el Usuario Mock
            UUID mockId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

            if (!userRepository.existsById(mockId)) {

                // Usamos SQL nativo para forzar el INSERT y evitar que Hibernate
                // se confunda con el UUID manual.
                String sql = "INSERT INTO users (user_id, name, email, password, phone, address, rol_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

                jdbcTemplate.update(sql,
                        mockId,
                        "Usuario Prueba",
                        "test@ohbike.com",
                        "123456", // Password sin encriptar
                        "7777-8888",
                        "Calle Principal 123",
                        clienteRole.getId());

                System.out.println("Usuario Mock creado exitosamente con ID: " + mockId);
            }
        };
    }
}