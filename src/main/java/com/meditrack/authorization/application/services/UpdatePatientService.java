package com.meditrack.authorization.application.services;

import com.meditrack.authorization.domain.models.Patient;
import com.meditrack.authorization.domain.ports.in.command.UpdatePatientCommand;
import com.meditrack.authorization.domain.ports.in.useCase.UpdatePatientUseCase;
import com.meditrack.authorization.domain.ports.out.CurrentUserPort;
import com.meditrack.authorization.domain.ports.out.PatientRepositoryPort;
import com.meditrack.authorization.domain.ports.out.UserRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Servicio: Actualizar Paciente
 * Implementa el caso de uso UpdatePatientUseCase
 */
@Service
public class UpdatePatientService implements UpdatePatientUseCase {

    private final PatientRepositoryPort patientRepository;
    private final UserRepositoryPort userRepository;
    private final CurrentUserPort currentUserPort;

    public UpdatePatientService(
            PatientRepositoryPort patientRepository,
            UserRepositoryPort userRepository,
            CurrentUserPort currentUserPort) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.currentUserPort = currentUserPort;
    }

    @Override
    @Transactional
    public Patient execute(UpdatePatientCommand command) {

        // 1. Obtener el usuario actual
        UUID currentUserId = currentUserPort.getCurrentUserId();

        // 2. Buscar el paciente
        Patient patient = patientRepository.findByIdAndNotDeleted(command.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Paciente no encontrado: " + command.getPatientId()
                ));

        // 3. Verificar permisos (paciente solo puede actualizar su propia info)
        userRepository.findById(currentUserId).ifPresent(user -> {
            if (!user.canAccessPatient(patient.getId())) {
                throw new IllegalStateException(
                        "No tienes permiso para actualizar este paciente"
                );
            }
        });

        // 4. Actualizar el paciente (dominio)
        patient.update(
                command.getFirstName(),
                command.getLastName(),
                command.getEmail(),
                command.getPhone()
        );

        // 5. Guardar cambios
        Patient updatedPatient = patientRepository.save(patient);

        // 6. Actualizar el email del usuario asociado si cambió
        userRepository.findByPatientId(patient.getId()).ifPresent(user -> {
            if (!user.getEmail().equals(command.getEmail())) {
                user.updateEmail(command.getEmail());
                userRepository.save(user);
            }
        });

        // 7. Log de la actualización
        System.out.println("Paciente actualizado: " + updatedPatient.getDocumentNumber());

        return updatedPatient;
    }
}