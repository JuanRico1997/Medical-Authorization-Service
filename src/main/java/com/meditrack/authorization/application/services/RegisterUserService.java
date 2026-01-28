package com.meditrack.authorization.application.services;

import com.meditrack.authorization.domain.exceptions.DuplicateResourceException;
import com.meditrack.authorization.domain.models.User;
import com.meditrack.authorization.domain.ports.in.command.RegisterUserCommand;
import com.meditrack.authorization.domain.ports.in.useCase.RegisterUserUseCase;
import com.meditrack.authorization.domain.ports.out.PasswordEncoderPort;
import com.meditrack.authorization.domain.ports.out.UserRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio: Registrar Usuario
 * Implementa el caso de uso RegisterUserUseCase
 */
@Service
public class RegisterUserService implements RegisterUserUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;

    public RegisterUserService(
            UserRepositoryPort userRepository,
            PasswordEncoderPort passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User execute(RegisterUserCommand command) {

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

        // 3. Encriptar la contraseña
        String encodedPassword = passwordEncoder.encode(command.getPassword());

        // 4. Crear el usuario (dominio)
        User user = new User(
                command.getUsername(),
                command.getEmail(),
                encodedPassword,
                command.getRole(),
                command.getPatientId()
        );

        // 5. Guardar en la base de datos
        User savedUser = userRepository.save(user);

        // 6. Log del registro
        System.out.println("Usuario registrado: " + savedUser.getUsername() + " con rol " + savedUser.getRole());

        return savedUser;
    }
}