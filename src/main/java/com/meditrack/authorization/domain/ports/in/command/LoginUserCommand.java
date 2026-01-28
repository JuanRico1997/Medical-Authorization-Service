package com.meditrack.authorization.domain.ports.in.command;

/**
 * Command: Datos para iniciar sesión
 */
public class LoginUserCommand {

    private final String username;
    private final String password;

    public LoginUserCommand(String username, String password) {
        // Validaciones
        validateUsername(username);
        validatePassword(password);

        this.username = username.trim().toLowerCase();
        this.password = password;
    }

    private void validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El username es obligatorio");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}