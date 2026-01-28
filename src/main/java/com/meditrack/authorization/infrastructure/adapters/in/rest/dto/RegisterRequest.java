package com.meditrack.authorization.infrastructure.adapters.in.rest.dto;

import com.meditrack.authorization.domain.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * DTO: Request para registrar un usuario
 */
@Schema(description = "Datos para registrar un nuevo usuario")
public class RegisterRequest {

    @Schema(description = "Nombre de usuario único", example = "juan_lopez", required = true)
    @NotBlank(message = "El username es obligatorio")
    @Size(min = 3, max = 50, message = "El username debe tener entre 3 y 50 caracteres")
    private String username;

    @Schema(description = "Email del usuario", example = "juan.lopez@example.com", required = true)
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    private String email;

    @Schema(description = "Contraseña (mínimo 6 caracteres)", example = "password123", required = true)
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @Schema(description = "Rol del usuario en el sistema", example = "ROLE_PACIENTE", required = true)
    @NotNull(message = "El rol es obligatorio")
    private UserRole role;

    @Schema(description = "ID del paciente (solo para ROLE_PACIENTE)", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID patientId; // Opcional

    // Constructores
    public RegisterRequest() {
    }

    public RegisterRequest(String username, String email, String password, UserRole role, UUID patientId) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.patientId = patientId;
    }

    // Getters y Setters
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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