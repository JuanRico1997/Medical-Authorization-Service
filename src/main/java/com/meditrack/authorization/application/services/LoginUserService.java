package com.meditrack.authorization.application.services;

import com.meditrack.authorization.domain.models.User;
import com.meditrack.authorization.domain.ports.in.command.LoginUserCommand;
import com.meditrack.authorization.domain.ports.in.useCase.LoginUserUseCase;
import com.meditrack.authorization.domain.ports.out.JwtServicePort;
import com.meditrack.authorization.domain.ports.out.PasswordEncoderPort;
import com.meditrack.authorization.domain.ports.out.UserRepositoryPort;
import org.springframework.stereotype.Service;

/**
 * Servicio: Login de Usuario
 * Implementa el caso de uso LoginUserUseCase
 */
@Service
public class LoginUserService implements LoginUserUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final JwtServicePort jwtService;

    public LoginUserService(
            UserRepositoryPort userRepository,
            PasswordEncoderPort passwordEncoder,
            JwtServicePort jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public LoginResult execute(LoginUserCommand command) {

        // 1. Buscar el usuario por username
        User user = userRepository.findByUsername(command.getUsername())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Credenciales inválidas"
                ));

        // 2. Verificar que el usuario esté activo
        if (!user.isActive()) {
            throw new IllegalArgumentException(
                    "El usuario está desactivado"
            );
        }

        // 3. Verificar la contraseña
        if (!passwordEncoder.matches(command.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException(
                    "Credenciales inválidas"
            );
        }

        // 4. Generar el token JWT
        String token = jwtService.generateToken(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );

        // 5. Log del login
        System.out.println("Login exitoso: " + user.getUsername() + " con rol " + user.getRole());

        // 6. Devolver usuario y token
        return new LoginResult(user, token);
    }
}