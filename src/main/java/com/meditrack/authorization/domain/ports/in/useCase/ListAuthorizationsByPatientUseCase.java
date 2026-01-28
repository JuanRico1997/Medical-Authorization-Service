package com.meditrack.authorization.domain.ports.in.useCase;
import com.meditrack.authorization.domain.models.MedicalAuthorization;
import com.meditrack.authorization.domain.ports.in.query.ListAuthorizationsByPatientQuery;

import java.util.List;

/**
 * Puerto de entrada: Caso de uso Listar Autorizaciones por Paciente
 */
public interface ListAuthorizationsByPatientUseCase {

    /**
     * Lista todas las autorizaciones de un paciente
     *
     * @param query Query con el ID del paciente
     * @return Lista de autorizaciones
     */
    List<MedicalAuthorization> execute(ListAuthorizationsByPatientQuery query);
}