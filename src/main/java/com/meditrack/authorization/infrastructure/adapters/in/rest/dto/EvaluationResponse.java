package com.meditrack.authorization.infrastructure.adapters.in.rest.dto;

import com.meditrack.authorization.domain.models.CoverageEvaluation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO: Response con información de una evaluación de cobertura
 */
@Schema(description = "Resultado de la evaluación de cobertura")
public class EvaluationResponse {

    @Schema(description = "ID de la evaluación", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "ID de la autorización", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID authorizationId;

    @Schema(description = "Porcentaje de cobertura", example = "80")
    private int coveragePercentage;

    @Schema(description = "Monto de copago", example = "30000")
    private BigDecimal copayAmount;

    @Schema(description = "Si fue aprobada", example = "true")
    private boolean approved;

    @Schema(description = "Fecha de evaluación", example = "2024-01-28T10:30:00")
    private LocalDateTime evaluationDate;

    // Constructores
    public EvaluationResponse() {
    }

    public EvaluationResponse(UUID id, UUID authorizationId, int coveragePercentage,
                              BigDecimal copayAmount, boolean approved, LocalDateTime evaluationDate) {
        this.id = id;
        this.authorizationId = authorizationId;
        this.coveragePercentage = coveragePercentage;
        this.copayAmount = copayAmount;
        this.approved = approved;
        this.evaluationDate = evaluationDate;
    }

    /**
     * Crea un EvaluationResponse desde un CoverageEvaluation del dominio
     */
    public static EvaluationResponse fromDomain(CoverageEvaluation evaluation) {
        return new EvaluationResponse(
                evaluation.getId(),
                evaluation.getAuthorizationId(),
                evaluation.getCoveragePercentage(),
                evaluation.getCopayAmount(),
                evaluation.isApproved(),
                evaluation.getEvaluationDate()
        );
    }

    // Getters y Setters
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
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public LocalDateTime getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(LocalDateTime evaluationDate) {
        this.evaluationDate = evaluationDate;
    }
}