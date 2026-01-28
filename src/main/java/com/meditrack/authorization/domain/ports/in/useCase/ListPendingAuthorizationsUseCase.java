package com.meditrack.authorization.domain.ports.in.useCase;
import com.meditrack.authorization.domain.models.MedicalAuthorization;

import java.util.List;

/**
 * Puerto de entrada: Caso de uso Listar Autorizaciones Pendientes
 */
public interface ListPendingAuthorizationsUseCase {

    /**
     * Lista todas las autorizaciones pendientes
     * Usado por médicos para ver qué autorizaciones requieren atención
     *
     * @return Lista de autorizaciones pendientes
     */
    List<MedicalAuthorization> execute();
}