package com.meditrack.authorization.infrastructure.adapters.out.persistence.entity;

import com.meditrack.authorization.domain.enums.AuthorizationStatus;
import com.meditrack.authorization.domain.enums.ServiceType;
import com.meditrack.authorization.domain.models.MedicalAuthorization;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad JPA: Autorización Médica
 * Mapea la tabla 'medical_authorizations' en la base de datos
 */
@Entity
@Table(name = "medical_authorizations")
public class MedicalAuthorizationEntity {

    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "patient_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID patientId;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_type", nullable = false, length = 20)
    private ServiceType serviceType;

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @Column(name = "request_date", nullable = false)
    private LocalDateTime requestDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private AuthorizationStatus status;

    @Column(name = "requested_by", nullable = false, columnDefinition = "BINARY(16)")
    private UUID requestedBy;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    // Relación ManyToOne con Patient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", insertable = false, updatable = false)
    private PatientEntity patient;

    // Relación OneToOne con CoverageEvaluation
    @OneToOne(mappedBy = "authorization", cascade = CascadeType.ALL, orphanRemoval = false)
    private CoverageEvaluationEntity coverageEvaluation;

    // ==========================================
    // CONSTRUCTORES
    // ==========================================

    public MedicalAuthorizationEntity() {
    }

    public MedicalAuthorizationEntity(
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

    // ==========================================
    // CONVERSIÓN: DOMAIN <-> ENTITY
    // ==========================================

    /**
     * Convierte de modelo de dominio a entidad JPA
     */
    public static MedicalAuthorizationEntity fromDomain(MedicalAuthorization authorization) {
        return new MedicalAuthorizationEntity(
                authorization.getId(),
                authorization.getPatientId(),
                authorization.getServiceType(),
                authorization.getDescription(),
                authorization.getRequestDate(),
                authorization.getStatus(),
                authorization.getRequestedBy(),
                authorization.isDeleted()
        );
    }

    /**
     * Convierte de entidad JPA a modelo de dominio
     */
    public MedicalAuthorization toDomain() {
        return new MedicalAuthorization(
                this.id,
                this.patientId,
                this.serviceType,
                this.description,
                this.requestDate,
                this.status,
                this.requestedBy,
                this.deleted
        );
    }

    // ==========================================
    // GETTERS Y SETTERS
    // ==========================================

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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public PatientEntity getPatient() {
        return patient;
    }

    public void setPatient(PatientEntity patient) {
        this.patient = patient;
    }

    public CoverageEvaluationEntity getCoverageEvaluation() {
        return coverageEvaluation;
    }

    public void setCoverageEvaluation(CoverageEvaluationEntity coverageEvaluation) {
        this.coverageEvaluation = coverageEvaluation;
    }
}