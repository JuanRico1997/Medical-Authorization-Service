package com.meditrack.authorization.domain.ports.in.useCase;

import com.meditrack.authorization.domain.models.Patient;
import com.meditrack.authorization.domain.ports.in.GetPatientByIdQuery;

/**
 * Puerto de entrada: Caso de uso Obtener Paciente por ID
 */
public interface GetPatientByIdUseCase {

    /**
     * Obtiene un paciente por su ID
     *
     * @param query Query con el ID del paciente
     * @return Paciente encontrado
     * @throws IllegalArgumentException si el paciente no existe
     */
    Patient execute(GetPatientByIdQuery query);
}