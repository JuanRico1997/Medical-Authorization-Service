package com.meditrack.authorization.domain.ports.in.useCase;

import com.meditrack.authorization.domain.models.User;
import com.meditrack.authorization.domain.ports.in.command.RegisterUserCommand;

/**
 * Puerto de entrada: Caso de uso Registrar Usuario
 */
public interface RegisterUserUseCase {

    /**
     * Registra un nuevo usuario en el sistema
     *
     * @param command Datos del usuario a registrar
     * @return Usuario registrado
     * @throws IllegalArgumentException si el username o email ya existen
     */
    User execute(RegisterUserCommand command);
}