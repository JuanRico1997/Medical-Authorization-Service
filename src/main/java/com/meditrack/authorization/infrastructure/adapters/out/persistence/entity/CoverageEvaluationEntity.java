package com.meditrack.authorization.infrastructure.adapters.out.persistence.entity;

import com.meditrack.authorization.domain.models.CoverageEvaluation;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad JPA: Evaluación de Cobertura
 * Mapea la tabla 'coverage_evaluations' en la base de datos
 */
@Entity
@Table(name = "coverage_evaluations")
public class CoverageEvaluationEntity {

    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "authorization_id", nullable = false, unique = true, columnDefinition = "BINARY(16)")
    private UUID authorizationId;

    @Column(name = "coverage_percentage", nullable = false)
    private int coveragePercentage;

    @Column(name = "copay_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal copayAmount;

    @Column(name = "is_approved", nullable = false)
    private boolean isApproved;

    @Column(name = "evaluation_date", nullable = false)
    private LocalDateTime evaluationDate;

    @Column(name = "insurance_response", columnDefinition = "TEXT")
    private String insuranceResponse;

    // Relación OneToOne con MedicalAuthorization
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorization_id", insertable = false, updatable = false)
    private MedicalAuthorizationEntity authorization;

    // ==========================================
    // CONSTRUCTORES
    // ==========================================

    public CoverageEvaluationEntity() {
    }

    public CoverageEvaluationEntity(
            UUID id,
            UUID authorizationId,
            int coveragePercentage,
            BigDecimal copayAmount,
            boolean isApproved,
            LocalDateTime evaluationDate,
            String insuranceResponse) {
        this.id = id;
        this.authorizationId = authorizationId;
        this.coveragePercentage = coveragePercentage;
        this.copayAmount = copayAmount;
        this.isApproved = isApproved;
        this.evaluationDate = evaluationDate;
        this.insuranceResponse = insuranceResponse;
    }

    // ==========================================
    // CONVERSIÓN: DOMAIN <-> ENTITY
    // ==========================================

    /**
     * Convierte de modelo de dominio a entidad JPA
     */
    public static CoverageEvaluationEntity fromDomain(CoverageEvaluation evaluation) {
        return new CoverageEvaluationEntity(
                evaluation.getId(),
                evaluation.getAuthorizationId(),
                evaluation.getCoveragePercentage(),
                evaluation.getCopayAmount(),
                evaluation.isApproved(),
                evaluation.getEvaluationDate(),
                evaluation.getInsuranceResponse()
        );
    }

    /**
     * Convierte de entidad JPA a modelo de dominio
     */
    public CoverageEvaluation toDomain() {
        return new CoverageEvaluation(
                this.id,
                this.authorizationId,
                this.coveragePercentage,
                this.copayAmount,
                this.isApproved,
                this.evaluationDate,
                this.insuranceResponse
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

    public UUID getAuthorizationId() {
        return authorizationId;
    }

    public void setAuthorizationId(UUID authorizationId) {
        this.authorizationId = authorizationId;
    }

    public int getCoveragePercentage() {
        return coveragePercentage;
    }

    public void setCoveragePercentage(int coveragePercentage) {
        this.coveragePercentage = coveragePercentage;
    }

    public BigDecimal getCopayAmount() {
        return copayAmount;
    }

    public void setCopayAmount(BigDecimal copayAmount) {
        this.copayAmount = copayAmount;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public LocalDateTime getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(LocalDateTime evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    public String getInsuranceResponse() {
        return insuranceResponse;
    }

    public void setInsuranceResponse(String insuranceResponse) {
        this.insuranceResponse = insuranceResponse;
    }

    public MedicalAuthorizationEntity getAuthorization() {
        return authorization;
    }

    public void setAuthorization(MedicalAuthorizationEntity authorization) {
        this.authorization = authorization;
    }
}