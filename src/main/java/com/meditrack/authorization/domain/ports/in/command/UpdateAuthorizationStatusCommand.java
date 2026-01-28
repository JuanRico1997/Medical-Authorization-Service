package com.meditrack.authorization.domain.ports.in.command;

import com.meditrack.authorization.domain.enums.AuthorizationStatus;

import java.util.UUID;

/**
 * Command: Datos para actualizar el estado de una autorización
 */
public class UpdateAuthorizationStatusCommand {

    private final UUID authorizationId;
    private final AuthorizationStatus newStatus;

    public UpdateAuthorizationStatusCommand(UUID authorizationId, AuthorizationStatus newStatus) {

        if (authorizationId == null) {
            throw new IllegalArgumentException("El ID de la autorización es obligatorio");
        }

        if (newStatus == null) {
            throw new IllegalArgumentException("El nuevo estado es obligatorio");
        }

        this.authorizationId = authorizationId;
        this.newStatus = newStatus;
    }

    // Getters
    public UUID getAuthorizationId() {
        return authorizationId;
    }

    public AuthorizationStatus getNewStatus() {
        return newStatus;
    }
}