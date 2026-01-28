package com.meditrack.authorization.domain.ports.in.useCase;


import java.util.UUID;

/**
 * Puerto de entrada: Caso de uso Desactivar Paciente
 */
public interface DeactivatePatientUseCase {

    /**
     * Desactiva un paciente (soft delete)
     *
     * @param patientId ID del paciente a desactivar
     * @throws IllegalArgumentException si el paciente no existe
     */
    void execute(UUID patientId);
}