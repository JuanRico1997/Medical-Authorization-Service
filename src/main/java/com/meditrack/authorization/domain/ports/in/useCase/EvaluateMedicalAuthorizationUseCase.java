package com.meditrack.authorization.domain.ports.in.useCase;

import com.meditrack.authorization.domain.models.CoverageEvaluation;
import com.meditrack.authorization.domain.ports.in.command.EvaluateMedicalAuthorizationCommand;

/**
 * Puerto de entrada: Caso de uso Evaluar Autorización Médica
 */
public interface EvaluateMedicalAuthorizationUseCase {

    /**
     * Evalúa una autorización médica consultando al servicio de seguros
     * y crea la evaluación de cobertura
     *
     * @param command Datos para la evaluación
     * @return Evaluación de cobertura creada
     * @throws IllegalArgumentException si la autorización no existe o ya fue evaluada
     */
    CoverageEvaluation execute(EvaluateMedicalAuthorizationCommand command);
}
