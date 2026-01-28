package com.meditrack.authorization.infrastructure.adapters.in.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * DTO: Request para evaluar una autorización médica
 */
@Schema(description = "Datos para evaluar una autorización con el servicio de seguros")
public class EvaluateAuthorizationRequest {

    @Schema(description = "Costo estimado del servicio", example = "250000", required = true)
    @NotNull(message = "El costo estimado es obligatorio")
    @Positive(message = "El costo estimado debe ser mayor a cero")
    private BigDecimal estimatedCost;

    // Constructores
    public EvaluateAuthorizationRequest() {
    }

    public EvaluateAuthorizationRequest(BigDecimal estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    // Getters y Setters
    public BigDecimal getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(BigDecimal estimatedCost) {
        this.estimatedCost = estimatedCost;
    }
}