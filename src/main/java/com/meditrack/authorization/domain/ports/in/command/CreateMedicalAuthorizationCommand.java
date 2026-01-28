package com.meditrack.authorization.domain.ports.in.command;

import com.meditrack.authorization.domain.enums.ServiceType;

import java.util.UUID;

/**
 * Command: Datos para crear una autorización médica
 */
public class CreateMedicalAuthorizationCommand {

    private final UUID patientId;
    private final ServiceType serviceType;
    private final String description;
    private final UUID requestedBy;

    public CreateMedicalAuthorizationCommand(
            UUID patientId,
            ServiceType serviceType,
            String description,
            UUID requestedBy) {

        // Validaciones
        if (patientId == null) {
            throw new IllegalArgumentException("El ID del paciente es obligatorio");
        }

        if (serviceType == null) {
            throw new IllegalArgumentException("El tipo de servicio es obligatorio");
        }

        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción es obligatoria");
        }

        if (description.trim().length() < 10) {
            throw new IllegalArgumentException(
                    "La descripción debe tener al menos 10 caracteres"
            );
        }

        if (requestedBy == null) {
            throw new IllegalArgumentException("El ID del solicitante es obligatorio");
        }

        this.patientId = patientId;
        this.serviceType = serviceType;
        this.description = description.trim();
        this.requestedBy = requestedBy;
    }

    // Getters
    public UUID getPatientId() {
        return patientId;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public String getDescription() {
        return description;
    }

    public UUID getRequestedBy() {
        return requestedBy;
    }
}