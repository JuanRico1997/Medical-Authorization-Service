package com.meditrack.authorization.infrastructure.adapters.in.rest.dto;

import com.meditrack.authorization.domain.enums.AuthorizationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * DTO: Request para actualizar el estado de una autorización
 */
@Schema(description = "Datos para actualizar el estado de una autorización")
public class UpdateAuthorizationStatusRequest {

    @Schema(description = "Nuevo estado de la autorización", example = "APROBADA", required = true)
    @NotNull(message = "El nuevo estado es obligatorio")
    private AuthorizationStatus newStatus;

    // Constructores
    public UpdateAuthorizationStatusRequest() {
    }

    public UpdateAuthorizationStatusRequest(AuthorizationStatus newStatus) {
        this.newStatus = newStatus;
    }

    // Getters y Setters
    public AuthorizationStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(AuthorizationStatus newStatus) {
        this.newStatus = newStatus;
    }
}