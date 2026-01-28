package com.meditrack.authorization.application.services;

import com.meditrack.authorization.domain.exceptions.ResourceNotFoundException;
import com.meditrack.authorization.domain.exceptions.UnauthorizedAccessException;
import com.meditrack.authorization.domain.models.MedicalAuthorization;
import com.meditrack.authorization.domain.ports.in.query.ListAuthorizationsByPatientQuery;
import com.meditrack.authorization.domain.ports.in.useCase.ListAuthorizationsByPatientUseCase;
import com.meditrack.authorization.domain.ports.out.CurrentUserPort;
import com.meditrack.authorization.domain.ports.out.MedicalAuthorizationRepositoryPort;
import com.meditrack.authorization.domain.ports.out.PatientRepositoryPort;
import com.meditrack.authorization.domain.ports.out.UserRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Servicio: Listar Autorizaciones por Paciente
 */
@Service
public class ListAuthorizationsByPatientService implements ListAuthorizationsByPatientUseCase {

    private final MedicalAuthorizationRepositoryPort authorizationRepository;
    private final PatientRepositoryPort patientRepository;
    private final UserRepositoryPort userRepository;
    private final CurrentUserPort currentUserPort;

    public ListAuthorizationsByPatientService(
            MedicalAuthorizationRepositoryPort authorizationRepository,
            PatientRepositoryPort patientRepository,
            UserRepositoryPort userRepository,
            CurrentUserPort currentUserPort) {
        this.authorizationRepository = authorizationRepository;
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.currentUserPort = currentUserPort;
    }

    @Override
    public List<MedicalAuthorization> execute(ListAuthorizationsByPatientQuery query) {

        // 1. Verificar que el paciente existe
        patientRepository.findByIdAndNotDeleted(query.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Paciente", query.getPatientId()
                ));

        // 2. Obtener el usuario actual
        UUID currentUserId = currentUserPort.getCurrentUserId();

        // 3. Verificar permisos
        userRepository.findById(currentUserId).ifPresent(user -> {
            // Paciente solo puede ver sus propias autorizaciones
            if (user.isPatient() && user.hasPatient()) {
                if (!query.getPatientId().equals(user.getPatientId())) {
                    throw new UnauthorizedAccessException(
                            "No tienes permiso para ver las autorizaciones de este paciente"
                    );
                }
            }
            // Admin y Médico pueden ver todas
        });

        // 4. Obtener las autorizaciones
        return authorizationRepository.findByPatientId(query.getPatientId());
    }
}