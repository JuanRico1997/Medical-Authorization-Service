package com.meditrack.authorization.domain.ports.in.useCase;

import com.meditrack.authorization.domain.models.Patient;

import java.util.List;

/**
 * Puerto de entrada: Caso de uso Listar Pacientes
 */
public interface ListPatientsUseCase {

    /**
     * Lista todos los pacientes activos (no eliminados)
     *
     * @return Lista de pacientes
     */
    List<Patient> execute();
}