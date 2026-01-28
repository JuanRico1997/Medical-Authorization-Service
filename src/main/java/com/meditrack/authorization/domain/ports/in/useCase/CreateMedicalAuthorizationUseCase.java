package com.meditrack.authorization.domain.ports.in.useCase;

import com.meditrack.authorization.domain.models.MedicalAuthorization;
import com.meditrack.authorization.domain.ports.in.command.CreateMedicalAuthorizationCommand;

/**
 * Puerto de entrada: Caso de uso Crear Autorización Médica
 */
public interface CreateMedicalAuthorizationUseCase {

    /**
     * Crea una nueva autorización médica
     *
     * @param command Datos de la autorización
     * @return Autorización creada
     * @throws IllegalArgumentException si el paciente no existe o no puede solicitar autorizaciones
     */
    MedicalAuthorization execute(CreateMedicalAuthorizationCommand command);
}