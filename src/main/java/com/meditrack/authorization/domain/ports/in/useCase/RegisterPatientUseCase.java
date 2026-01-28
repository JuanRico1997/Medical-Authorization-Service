package com.meditrack.authorization.domain.ports.in.useCase;

import com.meditrack.authorization.domain.models.Patient;
import com.meditrack.authorization.domain.ports.in.command.RegisterPatientCommand;

/**
 * Puerto de entrada: Caso de uso Registrar Paciente
 */
public interface RegisterPatientUseCase {

    /**
     * Registra un nuevo paciente en el sistema
     * Crea automáticamente el usuario asociado con ROLE_PACIENTE
     *
     * @param command Datos del paciente a registrar
     * @return Paciente registrado
     * @throws IllegalArgumentException si el documento ya existe
     */
    Patient execute(RegisterPatientCommand command);
}
