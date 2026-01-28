package com.meditrack.authorization.domain.exceptions;

/**
 * Excepción: Acceso no autorizado
 */
public class UnauthorizedAccessException extends RuntimeException {

    public UnauthorizedAccessException(String message) {
        super(message);
    }

    public UnauthorizedAccessException() {
        super("No tienes permiso para realizar esta acción");
    }
}