package com.meditrack.authorization.application.services;

import com.meditrack.authorization.domain.enums.UserRole;
import com.meditrack.authorization.domain.exceptions.DuplicateResourceException;
import com.meditrack.authorization.domain.models.Patient;
import com.meditrack.authorization.domain.models.User;
import com.meditrack.authorization.domain.ports.in.command.RegisterPatientCommand;
import com.meditrack.authorization.domain.ports.in.useCase.RegisterPatientUseCase;
import com.meditrack.authorization.domain.ports.out.PasswordEncoderPort;
import com.meditrack.authorization.domain.ports.out.PatientRepositoryPort;
import com.meditrack.authorization.domain.ports.out.UserRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio: Registrar Paciente
 * Implementa el caso de uso RegisterPatientUseCase
 */
@Service
public class RegisterPatientService implements RegisterPatientUseCase {

    private final PatientRepositoryPort patientRepository;
    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;

    public RegisterPatientService(
            PatientRepositoryPort patientRepository,
            UserRepositoryPort userRepository,
            PasswordEncoderPort passwordEncoder) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Patient execute(RegisterPatientCommand command) {

        if (patientRepository.existsByDocumentNumber(command.getDocumentNumber())) {
            throw new DuplicateResourceException(
                    "Paciente", "documento", command.getDocumentNumber()
            );
        }

        if (userRepository.existsByUsername(command.getUsername())) {
            throw new DuplicateResourceException(
                    "Usuario", "username", command.getUsername()
            );
        }

        if (userRepository.existsByEmail(command.getEmail())) {
            throw new DuplicateResourceException(
                    "Usuario", "email", command.getEmail()
            );
        }

        // 4. Crear el paciente (dominio)
        Patient patient = new Patient(
                command.getDocumentNumber(),
                command.getFirstName(),
                command.getLastName(),
                command.getEmail(),
                command.getPhone(),
                command.getAffiliationType(),
                command.getAffiliationDate()
        );

        // 5. Guardar el paciente en la BD
        Patient savedPatient = patientRepository.save(patient);

        // 6. Encriptar la contraseña
        String encodedPassword = passwordEncoder.encode(command.getPassword());

        // 7. Crear el usuario asociado con ROLE_PACIENTE
        User user = new User(
                command.getUsername(),
                command.getEmail(),
                encodedPassword,
                UserRole.ROLE_PACIENTE,
                savedPatient.getId()  // Vincular con el paciente
        );

        // 8. Guardar el usuario en la BD
        userRepository.save(user);

        // 9. Log del registro
        System.out.println("Paciente registrado: " + savedPatient.getDocumentNumber() +
                " - Usuario: " + user.getUsername());

        return savedPatient;
    }
}