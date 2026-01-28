package com.meditrack.authorization.domain.ports.in.useCase;

import com.meditrack.authorization.domain.models.MedicalAuthorization;
import com.meditrack.authorization.domain.ports.in.query.GetAuthorizationByIdQuery;

/**
 * Puerto de entrada: Caso de uso Obtener Autorización por ID
 */
public interface GetAuthorizationByIdUseCase {

    /**
     * Obtiene una autorización médica por su ID
     *
     * @param query Query con el ID
     * @return Autorización encontrada
     * @throws IllegalArgumentException si no existe
     */
    MedicalAuthorization execute(GetAuthorizationByIdQuery query);
}