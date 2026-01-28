package com.meditrack.authorization.infrastructure.adapters.in.rest.dto;

import com.meditrack.authorization.domain.enums.UserRole;
import com.meditrack.authorization.domain.models.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

/**
 * DTO: Response de autenticación
 */
@Schema(description = "Respuesta de autenticación con token JWT y datos del usuario")
public class AuthResponse {

    @Schema(description = "Token JWT para autenticación", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "ID único del usuario", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID userId;

    @Schema(description = "Nombre de usuario", example = "admin")
    private String username;

    @Schema(description = "Email del usuario", example = "admin@meditrack.com")
    private String email;

    @Schema(description = "Rol del usuario", example = "ROLE_ADMIN")
    private UserRole role;

    @Schema(description = "ID del paciente asociado (solo para ROLE_PACIENTE)", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID patientId;

    // Constructores
    public AuthResponse() {
    }

    public AuthResponse(String token, UUID userId, String username, String email, UserRole role, UUID patientId) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role = role;
        this.patientId = patientId;
    }

    /**
     * Crea un AuthResponse desde un User y un token
     */
    public static AuthResponse fromUser(User user, String token) {
        return new AuthResponse(
                token,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getPatientId()
        );
    }

    // Getters y Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public UUID getPatientId() {
        return patientId;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }
}