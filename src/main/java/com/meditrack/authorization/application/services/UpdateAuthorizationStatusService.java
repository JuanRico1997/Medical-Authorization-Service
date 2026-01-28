package com.meditrack.authorization.application.services;

import com.meditrack.authorization.domain.enums.AuthorizationStatus;
import com.meditrack.authorization.domain.exceptions.ResourceNotFoundException;
import com.meditrack.authorization.domain.exceptions.UnauthorizedAccessException;
import com.meditrack.authorization.domain.models.MedicalAuthorization;
import com.meditrack.authorization.domain.ports.in.command.UpdateAuthorizationStatusCommand;
import com.meditrack.authorization.domain.ports.in.useCase.UpdateAuthorizationStatusUseCase;
import com.meditrack.authorization.domain.ports.out.CurrentUserPort;
import com.meditrack.authorization.domain.ports.out.MedicalAuthorizationRepositoryPort;
import com.meditrack.authorization.domain.ports.out.UserRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Servicio: Actualizar Estado de Autorización
 */
@Service
public class UpdateAuthorizationStatusService implements UpdateAuthorizationStatusUseCase {

    private final MedicalAuthorizationRepositoryPort authorizationRepository;
    private final UserRepositoryPort userRepository;
    private final CurrentUserPort currentUserPort;

    public UpdateAuthorizationStatusService(
            MedicalAuthorizationRepositoryPort authorizationRepository,
            UserRepositoryPort userRepository,
            CurrentUserPort currentUserPort) {
        this.authorizationRepository = authorizationRepository;
        this.userRepository = userRepository;
        this.currentUserPort = currentUserPort;
    }

    @Override
    @Transactional
    public MedicalAuthorization execute(UpdateAuthorizationStatusCommand command) {

        // 1. Obtener el usuario actual
        UUID currentUserId = currentUserPort.getCurrentUserId();

        // 2. Verificar que sea admin (solo admins pueden cambiar estados manualmente)
        userRepository.findById(currentUserId).ifPresent(user -> {
            if (!user.isAdmin()) {
                throw new UnauthorizedAccessException(
                        "Solo los administradores pueden cambiar el estado de autorizaciones"
                );
            }
        });

        // 3. Buscar la autorización
        MedicalAuthorization authorization = authorizationRepository.findByIdAndNotDeleted(
                command.getAuthorizationId()
        ).orElseThrow(() -> new ResourceNotFoundException(
                "Autorización", command.getAuthorizationId()
        ));

        // 4. Actualizar el estado
        AuthorizationStatus newStatus = command.getNewStatus();

        switch (newStatus) {
            case APROBADA:
                authorization.approve();
                break;
            case RECHAZADA:
                authorization.reject();
                break;
            case EN_REVISION:
                authorization.putUnderReview();
                break;
            default:
                throw new IllegalArgumentException("Estado no válido: " + newStatus);
        }

        // 5. Guardar cambios
        MedicalAuthorization updatedAuthorization = authorizationRepository.save(authorization);

        // 6. Log
        System.out.println("Estado de autorización actualizado: " +
                updatedAuthorization.getId() + " → " + newStatus);

        return updatedAuthorization;
    }
}