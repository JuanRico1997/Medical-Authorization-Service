package com.meditrack.authorization.domain.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad de dominio: Evaluación de Cobertura
 * Representa el resultado de la evaluación de cobertura médica
 * obtenida del servicio externo de validación de seguros
 *
 * POJO puro sin dependencias de frameworks
 */
public class CoverageEvaluation {

    private UUID id;
    private UUID authorizationId;
    private int coveragePercentage;    // Porcentaje de cobertura: 0-100
    private BigDecimal copayAmount;    // Monto de copago
    private boolean isApproved;        // Si la cobertura es suficiente
    private LocalDateTime evaluationDate;
    private String insuranceResponse;  // JSON completo de la respuesta del seguro

    // ==========================================
    // CONSTRUCTORES
    // ==========================================

    /**
     * Constructor para crear una nueva evaluación de cobertura
     */
    public CoverageEvaluation(
            UUID authorizationId,
            int coveragePercentage,
            BigDecimal copayAmount,
            boolean isApproved,
            String insuranceResponse) {

        // Validaciones de negocio
        validateAuthorizationId(authorizationId);
        validateCoveragePercentage(coveragePercentage);
        validateCopayAmount(copayAmount);

        this.id = UUID.randomUUID();
        this.authorizationId = authorizationId;
        this.coveragePercentage = coveragePercentage;
        this.copayAmount = copayAmount;
        this.isApproved = isApproved;
        this.evaluationDate = LocalDateTime.now();
        this.insuranceResponse = insuranceResponse;
    }

    /**
     * Constructor para reconstruir desde persistencia
     */
    public CoverageEvaluation(
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

    /**
     * Constructor sin argumentos (requerido por algunos frameworks)
     */
    protected CoverageEvaluation() {
    }

    // ==========================================
    // MÉTODOS DE NEGOCIO
    // ==========================================

    /**
     * Verifica si la cobertura cumple con el mínimo requerido
     *
     * @param minimumRequired Porcentaje mínimo requerido (0-100)
     * @return true si la cobertura es suficiente
     */
    public boolean meetsCoverageRequirement(int minimumRequired) {
        if (minimumRequired < 0 || minimumRequired > 100) {
            throw new IllegalArgumentException(
                    "El porcentaje mínimo debe estar entre 0 y 100: " + minimumRequired
            );
        }

        return this.coveragePercentage >= minimumRequired;
    }

    /**
     * Verifica si el copago excede el máximo permitido
     *
     * @param maxCopayPercentage Porcentaje máximo de copago permitido (0-100)
     * @return true si el copago excede el límite
     */
    public boolean exceedsMaxCopay(int maxCopayPercentage) {
        if (maxCopayPercentage < 0 || maxCopayPercentage > 100) {
            throw new IllegalArgumentException(
                    "El porcentaje máximo debe estar entre 0 y 100: " + maxCopayPercentage
            );
        }

        // Calcular el porcentaje de copago
        int copayPercentage = 100 - this.coveragePercentage;

        return copayPercentage > maxCopayPercentage;
    }

    /**
     * Obtiene el porcentaje de copago (complemento de la cobertura)
     *
     * @return Porcentaje de copago (0-100)
     */
    public int getCopayPercentage() {
        return 100 - this.coveragePercentage;
    }

    /**
     * Verifica si la evaluación pertenece a una autorización específica
     */
    public boolean belongsToAuthorization(UUID authorizationId) {
        return this.authorizationId.equals(authorizationId);
    }

    /**
     * Obtiene un resumen legible de la evaluación
     */
    public String getSummary() {
        return String.format(
                "Cobertura: %d%%, Copago: %d%% ($%s), Estado: %s",
                this.coveragePercentage,
                getCopayPercentage(),
                this.copayAmount.toString(),
                this.isApproved ? "APROBADO" : "RECHAZADO"
        );
    }

    // ==========================================
    // VALIDACIONES PRIVADAS
    // ==========================================

    private void validateAuthorizationId(UUID authorizationId) {
        if (authorizationId == null) {
            throw new IllegalArgumentException(
                    "El ID de la autorización es obligatorio"
            );
        }
    }

    private void validateCoveragePercentage(int coveragePercentage) {
        if (coveragePercentage < 0 || coveragePercentage > 100) {
            throw new IllegalArgumentException(
                    "El porcentaje de cobertura debe estar entre 0 y 100: " + coveragePercentage
            );
        }
    }

    private void validateCopayAmount(BigDecimal copayAmount) {
        if (copayAmount == null) {
            throw new IllegalArgumentException("El monto de copago es obligatorio");
        }

        if (copayAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "El monto de copago no puede ser negativo: " + copayAmount
            );
        }
    }

    // ==========================================
    // GETTERS Y SETTERS
    // ==========================================

    public UUID getId() {
        return id;
    }

    public UUID getAuthorizationId() {
        return authorizationId;
    }

    public int getCoveragePercentage() {
        return coveragePercentage;
    }

    public BigDecimal getCopayAmount() {
        return copayAmount;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public LocalDateTime getEvaluationDate() {
        return evaluationDate;
    }

    public String getInsuranceResponse() {
        return insuranceResponse;
    }

    // ==========================================
    // EQUALS, HASHCODE, TOSTRING
    // ==========================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CoverageEvaluation that = (CoverageEvaluation) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "CoverageEvaluation{" +
                "id=" + id +
                ", authorizationId=" + authorizationId +
                ", coveragePercentage=" + coveragePercentage +
                ", copayAmount=" + copayAmount +
                ", isApproved=" + isApproved +
                ", evaluationDate=" + evaluationDate +
                '}';
    }
}