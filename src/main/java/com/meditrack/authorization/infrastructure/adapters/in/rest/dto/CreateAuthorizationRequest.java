package com.meditrack.authorization.infrastructure.adapters.in.rest.dto;

import com.meditrack.authorization.domain.enums.ServiceType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.UUID;

/**
 * DTO: Request para crear una autorización médica
 */
@Schema(description = "Datos para crear una autorización médica")
@Builder
public class CreateAuthorizationRequest {

    @Schema(description = "ID del paciente", example = "550e8400-e29b-41d4-a716-446655440000", required = true)
    @NotNull(message = "El ID del paciente es obligatorio")
    private UUID patientId;

    @Schema(description = "Tipo de servicio médico", example = "CONSULTA", required = true)
    @NotNull(message = "El tipo de servicio es obligatorio")
    private ServiceType serviceType;

    @Schema(description = "Descripción detallada del servicio", example = "Consulta de seguimiento por dolor de espalda crónico", required = true)
    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 10, max = 500, message = "La descripción debe tener entre 10 y 500 caracteres")
    private String description;

    // Constructores
    public CreateAuthorizationRequest() {
    }

    public CreateAuthorizationRequest(UUID patientId, ServiceType serviceType, String description) {
        this.patientId = patientId;
        this.serviceType = serviceType;
        this.description = description;
    }

    // Getters y Setters
    public UUID getPatientId() {
        return patientId;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}