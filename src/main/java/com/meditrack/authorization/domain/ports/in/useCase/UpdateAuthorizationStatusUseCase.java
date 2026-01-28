package com.meditrack.authorization.domain.ports.in.useCase;

import com.meditrack.authorization.domain.models.MedicalAuthorization;
import com.meditrack.authorization.domain.ports.in.command.UpdateAuthorizationStatusCommand;

/**
 * Puerto de entrada: Caso de uso Actualizar Estado de Autorización
 */
public interface UpdateAuthorizationStatusUseCase {

    /**
     * Actualiza el estado de una autorización médica
     *
     * @param command Datos del cambio de estado
     * @return Autorización actualizada
     * @throws IllegalArgumentException si la autorización no existe o el cambio no es válido
     */
    MedicalAuthorization execute(UpdateAuthorizationStatusCommand command);
}