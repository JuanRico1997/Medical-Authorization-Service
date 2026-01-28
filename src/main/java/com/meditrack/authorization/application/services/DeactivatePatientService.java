package com.meditrack.authorization.application.services;

import com.meditrack.authorization.domain.models.Patient;
import com.meditrack.authorization.domain.ports.in.useCase.DeactivatePatientUseCase;
import com.meditrack.authorization.domain.ports.out.CurrentUserPort;
import com.meditrack.authorization.domain.ports.out.PatientRepositoryPort;
import com.meditrack.authorization.domain.ports.out.UserRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Servicio: Desactivar Paciente
 * Implementa el caso de uso DeactivatePatientUseCase
 */
@Service
public class DeactivatePatientService implements DeactivatePatientUseCase {

    private final PatientRepositoryPort patientRepository;
    private final UserRepositoryPort userRepository;
    private final CurrentUserPort currentUserPort;

    public DeactivatePatientService(
            PatientRepositoryPort patientRepository,
            UserRepositoryPort userRepository,
            CurrentUserPort currentUserPort) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.currentUserPort = currentUserPort;
    }

    @Override
    @Transactional
    public void execute(UUID patientId) {

        // 1. Obtener el usuario actual
        UUID currentUserId = currentUserPort.getCurrentUserId();

        // 2. Verificar que el usuario sea admin (solo admins pueden desactivar)
        userRepository.findById(currentUserId).ifPresent(user -> {
            if (!user.isAdmin()) {
                throw new IllegalStateException(
                        "Solo los administradores pueden desactivar pacientes"
                );
            }
        });

        // 3. Buscar el paciente
        Patient patient = patientRepository.findByIdAndNotDeleted(patientId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Paciente no encontrado: " + patientId
                ));

        // 4. Desactivar el paciente (dominio)
        patient.deactivate();

        // 5. Guardar cambios
        patientRepository.save(patient);

        // 6. Desactivar el usuario asociado
        userRepository.findByPatientId(patientId).ifPresent(user -> {
            user.deactivate();
            userRepository.save(user);
        });

        // 7. Log de la desactivación
        System.out.println("Paciente desactivado: " + patient.getDocumentNumber());
    }
}