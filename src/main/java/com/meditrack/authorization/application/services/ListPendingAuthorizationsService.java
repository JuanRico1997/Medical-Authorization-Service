package com.meditrack.authorization.application.services;

import com.meditrack.authorization.domain.enums.AuthorizationStatus;
import com.meditrack.authorization.domain.models.MedicalAuthorization;
import com.meditrack.authorization.domain.ports.in.useCase.ListPendingAuthorizationsUseCase;
import com.meditrack.authorization.domain.ports.out.CurrentUserPort;
import com.meditrack.authorization.domain.ports.out.MedicalAuthorizationRepositoryPort;
import com.meditrack.authorization.domain.ports.out.UserRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Servicio: Listar Autorizaciones Pendientes
 */
@Service
public class ListPendingAuthorizationsService implements ListPendingAuthorizationsUseCase {

    private final MedicalAuthorizationRepositoryPort authorizationRepository;
    private final UserRepositoryPort userRepository;
    private final CurrentUserPort currentUserPort;

    public ListPendingAuthorizationsService(
            MedicalAuthorizationRepositoryPort authorizationRepository,
            UserRepositoryPort userRepository,
            CurrentUserPort currentUserPort) {
        this.authorizationRepository = authorizationRepository;
        this.userRepository = userRepository;
        this.currentUserPort = currentUserPort;
    }

    @Override
    public List<MedicalAuthorization> execute() {

        // 1. Obtener el usuario actual
        UUID currentUserId = currentUserPort.getCurrentUserId();

        // 2. Verificar permisos (solo admin y médico pueden ver pendientes)
        userRepository.findById(currentUserId).ifPresent(user -> {
            if (user.isPatient()) {
                throw new IllegalStateException(
                        "Los pacientes no pueden acceder a la lista de autorizaciones pendientes"
                );
            }
        });

        // 3. Obtener autorizaciones pendientes
        return authorizationRepository.findByStatus(AuthorizationStatus.PENDIENTE);
    }
}