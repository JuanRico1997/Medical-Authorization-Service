package com.meditrack.authorization.infrastructure.adapters.out.security;

import com.meditrack.authorization.domain.enums.UserRole;
import com.meditrack.authorization.domain.ports.out.JwtServicePort;
import com.meditrack.authorization.infrastructure.security.JwtService;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Adaptador de servicio JWT
 * Implementa el puerto OUT JwtServicePort usando JwtService de infraestructura
 */
@Component
public class JwtServiceAdapter implements JwtServicePort {

    private final JwtService jwtService;

    public JwtServiceAdapter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public String generateToken(UUID userId, String username, UserRole role) {
        return jwtService.generateToken(userId, username, role);
    }

    @Override
    public UUID extractUserId(String token) {
        return jwtService.extractUserId(token);
    }

    @Override
    public boolean isTokenValid(String token, UUID userId) {
        return jwtService.isTokenValid(token, userId);
    }
}