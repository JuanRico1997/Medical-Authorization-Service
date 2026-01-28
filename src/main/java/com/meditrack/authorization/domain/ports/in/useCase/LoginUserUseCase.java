package com.meditrack.authorization.domain.ports.in.useCase;

import com.meditrack.authorization.domain.models.User;
import com.meditrack.authorization.domain.ports.in.command.LoginUserCommand;

/**
 * Puerto de entrada: Caso de uso Login de Usuario
 */
public interface LoginUserUseCase {

    /**
     * Autentica un usuario y genera un token JWT
     *
     * @param command Credenciales del usuario
     * @return Array con [0] = User, [1] = Token JWT
     * @throws IllegalArgumentException si las credenciales son inválidas
     */
    LoginResult execute(LoginUserCommand command);

    /**
     * Resultado del login
     */
    class LoginResult {
        private final User user;
        private final String token;

        public LoginResult(User user, String token) {
            this.user = user;
            this.token = token;
        }

        public User getUser() {
            return user;
        }

        public String getToken() {
            return token;
        }
    }
}