package com.meditrack.authorization.application.services;

import com.meditrack.authorization.domain.models.Patient;
import com.meditrack.authorization.domain.ports.in.GetPatientByIdQuery;
import com.meditrack.authorization.domain.ports.in.useCase.GetPatientByIdUseCase;
import com.meditrack.authorization.domain.ports.out.CurrentUserPort;
import com.meditrack.authorization.domain.ports.out.PatientRepositoryPort;
import com.meditrack.authorization.domain.ports.out.UserRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Servicio: Obtener Paciente por ID
 * Implementa el caso de uso GetPatientByIdUseCase
 */
@Service
public class GetPatientByIdService implements GetPatientByIdUseCase {

    private final PatientRepositoryPort patientRepository;
    private final UserRepositoryPort userRepository;
    private final CurrentUserPort currentUserPort;

    public GetPatientByIdService(
            PatientRepositoryPort patientRepository,
            UserRepositoryPort userRepository,
            CurrentUserPort currentUserPort) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.currentUserPort = currentUserPort;
    }

    @Override
    public Patient execute(GetPatientByIdQuery query) {

        // 1. Obtener el usuario actual
        UUID currentUserId = currentUserPort.getCurrentUserId();

        // 2. Buscar el paciente
        Patient patient = patientRepository.findByIdAndNotDeleted(query.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Paciente no encontrado: " + query.getPatientId()
                ));

        // 3. Verificar permisos
        userRepository.findById(currentUserId).ifPresent(user -> {
            if (!user.canAccessPatient(patient.getId())) {
                throw new IllegalStateException(
                        "No tienes permiso para ver este paciente"
                );
            }
        });

        return patient;
    }
}