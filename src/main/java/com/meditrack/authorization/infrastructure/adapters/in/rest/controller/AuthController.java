package com.meditrack.authorization.infrastructure.adapters.in.rest.controller;

import com.meditrack.authorization.domain.models.User;
import com.meditrack.authorization.domain.ports.in.command.LoginUserCommand;
import com.meditrack.authorization.domain.ports.in.useCase.LoginUserUseCase;
import com.meditrack.authorization.domain.ports.in.command.RegisterUserCommand;
import com.meditrack.authorization.domain.ports.in.useCase.RegisterUserUseCase;
import com.meditrack.authorization.infrastructure.adapters.in.rest.dto.AuthResponse;
import com.meditrack.authorization.infrastructure.adapters.in.rest.dto.LoginRequest;
import com.meditrack.authorization.infrastructure.adapters.in.rest.dto.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST: Autenticación
 * Endpoints públicos para registro y login
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints de autenticación y registro de usuarios")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;

    public AuthController(
            RegisterUserUseCase registerUserUseCase,
            LoginUserUseCase loginUserUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.loginUserUseCase = loginUserUseCase;
    }

    /**
     * POST /api/auth/register
     * Registra un nuevo usuario en el sistema
     */
    @PostMapping("/register")
    @Operation(
            summary = "Registrar nuevo usuario",
            description = "Crea un nuevo usuario en el sistema y devuelve un token JWT para autenticación automática"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuario registrado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de registro inválidos (username o email duplicado)",
                    content = @Content
            )
    })
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {

        // 1. Crear el command desde el request
        RegisterUserCommand command = new RegisterUserCommand(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                request.getRole(),
                request.getPatientId()
        );

        // 2. Ejecutar el caso de uso
        User registeredUser = registerUserUseCase.execute(command);

        // 3. Generar token para el usuario registrado (auto-login)
        LoginUserCommand loginCommand = new LoginUserCommand(
                request.getUsername(),
                request.getPassword()
        );
        LoginUserUseCase.LoginResult loginResult = loginUserUseCase.execute(loginCommand);

        // 4. Crear la respuesta
        AuthResponse response = AuthResponse.fromUser(registeredUser, loginResult.getToken());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * POST /api/auth/login
     * Inicia sesión y devuelve un token JWT
     */
    @PostMapping("/login")
    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica un usuario con username y password, devuelve un token JWT"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login exitoso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Credenciales inválidas",
                    content = @Content
            )
    })
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {

        // 1. Crear el command desde el request
        LoginUserCommand command = new LoginUserCommand(
                request.getUsername(),
                request.getPassword()
        );

        // 2. Ejecutar el caso de uso
        LoginUserUseCase.LoginResult loginResult = loginUserUseCase.execute(command);

        // 3. Crear la respuesta
        AuthResponse response = AuthResponse.fromUser(
                loginResult.getUser(),
                loginResult.getToken()
        );

        return ResponseEntity.ok(response);
    }
}