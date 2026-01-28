package com.meditrack.authorization.domain.ports.in.command;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Command: Datos para evaluar una autorización médica
 */
public class EvaluateMedicalAuthorizationCommand {

    private final UUID authorizationId;
    private final BigDecimal estimatedCost;

    public EvaluateMedicalAuthorizationCommand(UUID authorizationId, BigDecimal estimatedCost) {

        if (authorizationId == null) {
            throw new IllegalArgumentException("El ID de la autorización es obligatorio");
        }

        if (estimatedCost == null) {
            throw new IllegalArgumentException("El costo estimado es obligatorio");
        }

        if (estimatedCost.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El costo estimado debe ser mayor a cero");
        }

        this.authorizationId = authorizationId;
        this.estimatedCost = estimatedCost;
    }

    // Getters
    public UUID getAuthorizationId() {
        return authorizationId;
    }

    public BigDecimal getEstimatedCost() {
        return estimatedCost;
    }
}