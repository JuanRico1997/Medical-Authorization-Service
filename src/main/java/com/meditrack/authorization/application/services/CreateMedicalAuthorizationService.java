package com.meditrack.authorization.application.services;

import com.meditrack.authorization.domain.exceptions.BusinessRuleException;
import com.meditrack.authorization.domain.exceptions.ResourceNotFoundException;
import com.meditrack.authorization.domain.models.MedicalAuthorization;
import com.meditrack.authorization.domain.models.Patient;
import com.meditrack.authorization.domain.ports.in.command.CreateMedicalAuthorizationCommand;
import com.meditrack.authorization.domain.ports.in.useCase.CreateMedicalAuthorizationUseCase;
import com.meditrack.authorization.domain.ports.out.MedicalAuthorizationRepositoryPort;
import com.meditrack.authorization.domain.ports.out.PatientRepositoryPort;
import com.meditrack.authorization.domain.ports.out.UserRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio: Crear Autorización Médica
 */
@Service
public class CreateMedicalAuthorizationService implements CreateMedicalAuthorizationUseCase {

    private final MedicalAuthorizationRepositoryPort authorizationRepository;
    private final PatientRepositoryPort patientRepository;
    private final UserRepositoryPort userRepository;

    public CreateMedicalAuthorizationService(
            MedicalAuthorizationRepositoryPort authorizationRepository,
            PatientRepositoryPort patientRepository,
            UserRepositoryPort userRepository) {
        this.authorizationRepository = authorizationRepository;
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public MedicalAuthorization execute(CreateMedicalAuthorizationCommand command) {

        // 1. Verificar que el paciente existe
        Patient patient = patientRepository.findByIdAndNotDeleted(command.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Paciente", command.getPatientId()
                ));

        // 2. Verificar que el usuario solicitante existe
        userRepository.findById(command.getRequestedBy())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario", command.getRequestedBy()
                ));

        // 3. Verificar que el paciente está activo
        if (!patient.isActive()) {
            throw new BusinessRuleException(
                    "El paciente no está activo y no puede solicitar autorizaciones"
            );
        }

        // 4. Crear la autorización (dominio)
        MedicalAuthorization authorization = new MedicalAuthorization(
                command.getPatientId(),
                command.getServiceType(),
                command.getDescription(),
                command.getRequestedBy()
        );

        // 5. Guardar en la base de datos
        MedicalAuthorization savedAuthorization = authorizationRepository.save(authorization);

        // 6. Log
        System.out.println("Autorización médica creada: " + savedAuthorization.getId() +
                " - Paciente: " + patient.getFullName() +
                " - Servicio: " + command.getServiceType());

        return savedAuthorization;
    }
}