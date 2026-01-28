package com.meditrack.authorization.domain.models;

import com.meditrack.authorization.domain.enums.AuthorizationStatus;
import com.meditrack.authorization.domain.enums.ServiceType;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad de dominio: Autorización Médica
 * Representa una solicitud de autorización para un servicio médico
 *
 * POJO puro sin dependencias de frameworks
 */
public class MedicalAuthorization {

    private UUID id;
    private UUID patientId;
    private ServiceType serviceType;
    private String description;
    private LocalDateTime requestDate;
    private AuthorizationStatus status;
    private UUID requestedBy; // ID del médico o admin que solicita
    private boolean deleted;

    // ==========================================
    // CONSTRUCTORES
    // ==========================================

    /**
     * Constructor para crear una nueva autorización médica
     */
    public MedicalAuthorization(
            UUID patientId,
            ServiceType serviceType,
            String description,
            UUID requestedBy) {

        // Validaciones de negocio
        validatePatientId(patientId);
        validateServiceType(serviceType);
        validateDescription(description);
        validateRequestedBy(requestedBy);

        this.id = UUID.randomUUID();
        this.patientId = patientId;
        this.serviceType = serviceType;
        this.description = description.trim();
        this.requestDate = LocalDateTime.now();
        this.status = AuthorizationStatus.PENDIENTE; // Estado inicial
        this.requestedBy = requestedBy;
        this.deleted = false;
    }

    /**
     * Constructor para reconstruir desde persistencia
     */
    public MedicalAuthorization(
            UUID id,
            UUID patientId,
            ServiceType serviceType,
            String description,
            LocalDateTime requestDate,
            AuthorizationStatus status,
            UUID requestedBy,
            boolean deleted) {

        this.id = id;
        this.patientId = patientId;
        this.serviceType = serviceType;
        this.description = description;
        this.requestDate = requestDate;
        this.status = status;
        this.requestedBy = requestedBy;
        this.deleted = deleted;
    }

    /**
     * Constructor sin argumentos (requerido por algunos frameworks)
     */
    protected MedicalAuthorization() {
    }

    // ==========================================
    // MÉTODOS DE NEGOCIO
    // ==========================================

    /**
     * Aprueba la autorización médica
     * Solo se puede aprobar si está en estado PENDIENTE o EN_REVISION
     */
    public void approve() {
        if (this.deleted) {
            throw new IllegalStateException(
                    "No se puede aprobar una autorización eliminada: " + this.id
            );
        }

        if (this.status == AuthorizationStatus.APROBADA) {
            throw new IllegalStateException(
                    "La autorización ya está aprobada: " + this.id
            );
        }

        if (this.status == AuthorizationStatus.RECHAZADA) {
            throw new IllegalStateException(
                    "No se puede aprobar una autorización que ya fue rechazada: " + this.id
            );
        }

        this.status = AuthorizationStatus.APROBADA;
    }

    /**
     * Rechaza la autorización médica
     * Solo se puede rechazar si está en estado PENDIENTE o EN_REVISION
     */
    public void reject() {
        if (this.deleted) {
            throw new IllegalStateException(
                    "No se puede rechazar una autorización eliminada: " + this.id
            );
        }

        if (this.status == AuthorizationStatus.APROBADA) {
            throw new IllegalStateException(
                    "No se puede rechazar una autorización que ya fue aprobada: " + this.id
            );
        }

        if (this.status == AuthorizationStatus.RECHAZADA) {
            throw new IllegalStateException(
                    "La autorización ya está rechazada: " + this.id
            );
        }

        this.status = AuthorizationStatus.RECHAZADA;
    }

    /**
     * Coloca la autorización en estado de revisión
     * Solo se puede revisar si está en estado PENDIENTE
     */
    public void markAsUnderReview() {
        if (this.deleted) {
            throw new IllegalStateException(
                    "No se puede revisar una autorización eliminada: " + this.id
            );
        }

        if (this.status != AuthorizationStatus.PENDIENTE) {
            throw new IllegalStateException(
                    "Solo se pueden revisar autorizaciones en estado PENDIENTE: " + this.id
            );
        }

        this.status = AuthorizationStatus.EN_REVISION;
    }

    /**
     * Marca la autorización como eliminada (soft delete)
     */
    public void delete() {
        if (this.deleted) {
            throw new IllegalStateException(
                    "La autorización ya está eliminada: " + this.id
            );
        }

        // Solo se pueden eliminar autorizaciones PENDIENTES o RECHAZADAS
        if (this.status == AuthorizationStatus.APROBADA) {
            throw new IllegalStateException(
                    "No se puede eliminar una autorización aprobada: " + this.id
            );
        }

        if (this.status == AuthorizationStatus.EN_REVISION) {
            throw new IllegalStateException(
                    "No se puede eliminar una autorización en revisión: " + this.id
            );
        }

        this.deleted = true;
    }

    /**
     * Verifica si la autorización pertenece a un paciente específico
     */
    public boolean belongsToPatient(UUID patientId) {
        return this.patientId.equals(patientId);
    }

    /**
     * Verifica si la autorización está en un estado final (aprobada o rechazada)
     */
    public boolean isFinalState() {
        return this.status == AuthorizationStatus.APROBADA
                || this.status == AuthorizationStatus.RECHAZADA;
    }

    /**
     * Verifica si la autorización puede ser modificada
     * Solo se pueden modificar autorizaciones en estado PENDIENTE
     */
    public boolean canBeModified() {
        return this.status == AuthorizationStatus.PENDIENTE && !this.deleted;
    }

    /**
     * Obtiene el porcentaje mínimo de cobertura requerido según el tipo de servicio
     *
     * @return Porcentaje de cobertura mínima (0-100)
     */
    public int getMinimumCoverageRequired() {
        return switch (this.serviceType) {
            case CONSULTA -> 70;       // 70%
            case PROCEDIMIENTO -> 80;  // 80%
            case CIRUGIA -> 90;        // 90%
        };
    }

    /**
     * Actualiza la descripción de la autorización
     * Solo se puede actualizar si está en estado PENDIENTE
     */
    public void updateDescription(String newDescription) {
        if (!canBeModified()) {
            throw new IllegalStateException(
                    "Solo se pueden actualizar autorizaciones en estado PENDIENTE: " + this.id
            );
        }

        validateDescription(newDescription);
        this.description = newDescription.trim();
    }

    // ==========================================
    // VALIDACIONES PRIVADAS
    // ==========================================

    private void validatePatientId(UUID patientId) {
        if (patientId == null) {
            throw new IllegalArgumentException("El ID del paciente es obligatorio");
        }
    }

    private void validateServiceType(ServiceType serviceType) {
        if (serviceType == null) {
            throw new IllegalArgumentException("El tipo de servicio es obligatorio");
        }
    }

    private void validateDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción es obligatoria");
        }

        if (description.trim().length() < 10) {
            throw new IllegalArgumentException(
                    "La descripción debe tener al menos 10 caracteres"
            );
        }

        if (description.trim().length() > 500) {
            throw new IllegalArgumentException(
                    "La descripción no puede exceder 500 caracteres"
            );
        }
    }

    private void validateRequestedBy(UUID requestedBy) {
        if (requestedBy == null) {
            throw new IllegalArgumentException(
                    "El ID del solicitante es obligatorio"
            );
        }
    }

    // ==========================================
    // GETTERS Y SETTERS
    // ==========================================

    public UUID getId() {
        return id;
    }

    public UUID getPatientId() {
        return patientId;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public AuthorizationStatus getStatus() {
        return status;
    }

    public UUID getRequestedBy() {
        return requestedBy;
    }

    public boolean isDeleted() {
        return deleted;
    }

    // ==========================================
    // EQUALS, HASHCODE, TOSTRING
    // ==========================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MedicalAuthorization that = (MedicalAuthorization) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "MedicalAuthorization{" +
                "id=" + id +
                ", patientId=" + patientId +
                ", serviceType=" + serviceType +
                ", status=" + status +
                ", requestDate=" + requestDate +
                ", deleted=" + deleted +
                '}';
    }
}
