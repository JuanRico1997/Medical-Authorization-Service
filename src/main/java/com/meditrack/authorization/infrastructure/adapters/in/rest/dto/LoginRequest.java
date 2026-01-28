package com.meditrack.authorization.infrastructure.adapters.in.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO: Request para iniciar sesión
 */
@Schema(description = "Credenciales para iniciar sesión")
public class LoginRequest {

    @Schema(description = "Nombre de usuario", example = "admin", required = true)
    @NotBlank(message = "El username es obligatorio")
    private String username;

    @Schema(description = "Contraseña", example = "admin123", required = true)
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    // Constructores
    public LoginRequest() {
    }

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters y Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}