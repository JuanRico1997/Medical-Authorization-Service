package com.meditrack.authorization.infrastructure.adapters.out.security;

import com.meditrack.authorization.domain.ports.out.CurrentUserPort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Adaptador de usuario actual
 * Implementa el puerto OUT CurrentUserPort usando SecurityContext de Spring Security
 */
@Component
public class CurrentUserAdapter implements CurrentUserPort {

    @Override
    public UUID getCurrentUserId() {
        // Obtener la autenticación del contexto de seguridad
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verificar que esté autenticado
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No hay usuario autenticado");
        }

        // El principal es el UUID del usuario que estableció JwtAuthenticationFilter
        Object principal = authentication.getPrincipal();

        // Si es "anonymousUser" significa que no está autenticado
        if (principal instanceof String && principal.equals("anonymousUser")) {
            throw new IllegalStateException("No hay usuario autenticado");
        }

        // Convertir a UUID
        if (principal instanceof UUID) {
            return (UUID) principal;
        }

        throw new IllegalStateException("Principal no es un UUID válido");
    }
}