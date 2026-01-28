package com.meditrack.authorization.infrastructure.adapters.in.rest.dto;

import com.meditrack.authorization.domain.enums.AuthorizationStatus;
import com.meditrack.authorization.domain.enums.ServiceType;
import com.meditrack.authorization.domain.models.MedicalAuthorization;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO: Response con información de una autorización médica
 */
@Schema(description = "Información de una autorización médica")
public class AuthorizationResponse {

    @Schema(description = "ID de la autorización", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "ID del paciente", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID patientId;

    @Schema(description = "Tipo de servicio", example = "CONSULTA")
    private ServiceType serviceType;

    @Schema(description = "Descripción del servicio", example = "Consulta de seguimiento")
    private String description;

    @Schema(description = "Fecha de solicitud", example = "2024-01-28T10:30:00")
    private LocalDateTime requestDate;

    @Schema(description = "Estado actual", example = "PENDIENTE")
    private AuthorizationStatus status;

    @Schema(description = "ID del solicitante", example = "660e8400-e29b-41d4-a716-446655440000")
    private UUID requestedBy;

    // Constructores
    public AuthorizationResponse() {
    }

    public AuthorizationResponse(UUID id, UUID patientId, ServiceType serviceType,
                                 String description, LocalDateTime requestDate,
                                 AuthorizationStatus status, UUID requestedBy) {
        this.id = id;
        this.patientId = patientId;
        this.serviceType = serviceType;
        this.description = description;
        this.requestDate = requestDate;
        this.status = status;
        this.requestedBy = requestedBy;
    }

    /**
     * Crea un AuthorizationResponse desde un MedicalAuthorization del dominio
     */
    public static AuthorizationResponse fromDomain(MedicalAuthorization authorization) {
        return new AuthorizationResponse(
                authorization.getId(),
                authorization.getPatientId(),
                authorization.getServiceType(),
                authorization.getDescription(),
                authorization.getRequestDate(),
                authorization.getStatus(),
                authorization.getRequestedBy()
        );
    }

    // Getters y Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public AuthorizationStatus getStatus() {
        return status;
    }

    public void setStatus(AuthorizationStatus status) {
        this.status = status;
    }

    public UUID getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(UUID requestedBy) {
        this.requestedBy = requestedBy;
    }
}