package com.meditrack.authorization.domain.ports.in.command;

import com.meditrack.authorization.domain.enums.UserRole;

import java.util.UUID;

/**
 * Command: Datos para registrar un nuevo usuario
 */
public class RegisterUserCommand {

    private final String username;
    private final String email;
    private final String password;
    private final UserRole role;
    private final UUID patientId;  // Opcional, solo para ROLE_PACIENTE

    public RegisterUserCommand(
            String username,
            String email,
            String password,
            UserRole role,
            UUID patientId) {

        // Validaciones
        validateUsername(username);
        validateEmail(email);
        validatePassword(password);
        validateRole(role);

        this.username = username.trim().toLowerCase();
        this.email = email.trim().toLowerCase();
        this.password = password;
        this.role = role;
        this.patientId = patientId;
    }

    // Constructor sin patientId (para médicos y admins)
    public RegisterUserCommand(
            String username,
            String email,
            String password,
            UserRole role) {
        this(username, email, password, role, null);
    }

    private void validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El username es obligatorio");
        }

        if (username.trim().length() < 3) {
            throw new IllegalArgumentException(
                    "El username debe tener al menos 3 caracteres"
            );
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }

        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("El email no tiene un formato válido");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }

        if (password.length() < 6) {
            throw new IllegalArgumentException(
                    "La contraseña debe tener al menos 6 caracteres"
            );
        }
    }

    private void validateRole(UserRole role) {
        if (role == null) {
            throw new IllegalArgumentException("El rol es obligatorio");
        }
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }

    public UUID getPatientId() {
        return patientId;
    }
}