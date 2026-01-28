package com.meditrack.authorization.application.services;

import com.meditrack.authorization.domain.models.Patient;
import com.meditrack.authorization.domain.ports.in.useCase.ListPatientsUseCase;
import com.meditrack.authorization.domain.ports.out.CurrentUserPort;
import com.meditrack.authorization.domain.ports.out.PatientRepositoryPort;
import com.meditrack.authorization.domain.ports.out.UserRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Servicio: Listar Pacientes
 * Implementa el caso de uso ListPatientsUseCase
 */
@Service
public class ListPatientsService implements ListPatientsUseCase {

    private final PatientRepositoryPort patientRepository;
    private final UserRepositoryPort userRepository;
    private final CurrentUserPort currentUserPort;

    public ListPatientsService(
            PatientRepositoryPort patientRepository,
            UserRepositoryPort userRepository,
            CurrentUserPort currentUserPort) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.currentUserPort = currentUserPort;
    }

    @Override
    public List<Patient> execute() {

        // 1. Obtener el usuario actual
        UUID currentUserId = currentUserPort.getCurrentUserId();

        // 2. Verificar el rol del usuario
        return userRepository.findById(currentUserId)
                .map(user -> {
                    // Si es admin o médico, puede ver todos los pacientes
                    if (user.isAdmin() || user.isDoctor()) {
                        return patientRepository.findAllActive();
                    }

                    // Si es paciente, solo puede ver su propia información
                    if (user.isPatient() && user.hasPatient()) {
                        return patientRepository.findByIdAndNotDeleted(user.getPatientId())
                                .map(List::of)
                                .orElse(List.of());
                    }

                    // Otros casos (no debería ocurrir)
                    return List.<Patient>of();
                })
                .orElse(List.of());
    }
}