package com.meditrack.authorization.application.services;

import com.meditrack.authorization.domain.exceptions.ResourceNotFoundException;
import com.meditrack.authorization.domain.exceptions.UnauthorizedAccessException;
import com.meditrack.authorization.domain.models.MedicalAuthorization;
import com.meditrack.authorization.domain.ports.in.query.GetAuthorizationByIdQuery;
import com.meditrack.authorization.domain.ports.in.useCase.GetAuthorizationByIdUseCase;
import com.meditrack.authorization.domain.ports.out.CurrentUserPort;
import com.meditrack.authorization.domain.ports.out.MedicalAuthorizationRepositoryPort;
import com.meditrack.authorization.domain.ports.out.UserRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Servicio: Obtener Autorización por ID
 */
@Service
public class GetAuthorizationByIdService implements GetAuthorizationByIdUseCase {

    private final MedicalAuthorizationRepositoryPort authorizationRepository;
    private final UserRepositoryPort userRepository;
    private final CurrentUserPort currentUserPort;

    public GetAuthorizationByIdService(
            MedicalAuthorizationRepositoryPort authorizationRepository,
            UserRepositoryPort userRepository,
            CurrentUserPort currentUserPort) {
        this.authorizationRepository = authorizationRepository;
        this.userRepository = userRepository;
        this.currentUserPort = currentUserPort;
    }

    @Override
    public MedicalAuthorization execute(GetAuthorizationByIdQuery query) {

        // 1. Obtener el usuario actual
        UUID currentUserId = currentUserPort.getCurrentUserId();

        // 2. Buscar la autorización
        MedicalAuthorization authorization = authorizationRepository.findByIdAndNotDeleted(
                query.getAuthorizationId()
        ).orElseThrow(() -> new ResourceNotFoundException(
                "Autorización", query.getAuthorizationId()
        ));

        // 3. Verificar permisos
        userRepository.findById(currentUserId).ifPresent(user -> {
            // Paciente solo puede ver sus propias autorizaciones
            if (user.isPatient() && user.hasPatient()) {
                if (!authorization.getPatientId().equals(user.getPatientId())) {
                    throw new UnauthorizedAccessException(
                            "No tienes permiso para ver esta autorización"
                    );
                }
            }
            // Admin y Médico pueden ver todas
        });

        return authorization;
    }
}