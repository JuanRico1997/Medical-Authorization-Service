package com.meditrack.authorization.domain.ports.out;

import com.meditrack.authorization.domain.enums.UserRole;

import java.util.UUID;

/**
 * Puerto de salida: Servicio de JWT
 * Define las operaciones de JWT que necesita el dominio
 */
public interface JwtServicePort {

    /**
     * Genera un token JWT para un usuario
     *
     * @param userId ID del usuario
     * @param username Nombre de usuario
     * @param role Rol del usuario
     * @return Token JWT generado
     */
    String generateToken(UUID userId, String username, UserRole role);

    /**
     * Extrae el userId de un token JWT
     *
     * @param token Token JWT
     * @return ID del usuario
     */
    UUID extractUserId(String token);

    /**
     * Valida si un token JWT es válido
     *
     * @param token Token JWT
     * @param userId ID del usuario
     * @return true si el token es válido
     */
    boolean isTokenValid(String token, UUID userId);
}