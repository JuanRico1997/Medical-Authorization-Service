package com.meditrack.authorization.domain.ports.in.useCase;
import com.meditrack.authorization.domain.models.Patient;
import com.meditrack.authorization.domain.ports.in.command.UpdatePatientCommand;

/**
 * Puerto de entrada: Caso de uso Actualizar Paciente
 */
public interface UpdatePatientUseCase {

    /**
     * Actualiza la información de un paciente
     *
     * @param command Datos a actualizar
     * @return Paciente actualizado
     * @throws IllegalArgumentException si el paciente no existe
     */
    Patient execute(UpdatePatientCommand command);
}
